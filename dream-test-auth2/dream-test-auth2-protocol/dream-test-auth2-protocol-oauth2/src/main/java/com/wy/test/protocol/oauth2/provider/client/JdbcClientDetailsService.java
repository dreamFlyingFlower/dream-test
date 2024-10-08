package com.wy.test.protocol.oauth2.provider.client;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.util.DefaultJdbcListFactory;
import org.springframework.security.oauth2.common.util.JdbcListFactory;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.entity.oauth2.ClientDetails;
import com.wy.test.core.entity.oauth2.client.BaseClientDetails;
import com.wy.test.protocol.oauth2.provider.ClientDetailsService;
import com.wy.test.protocol.oauth2.provider.ClientRegistrationService;

import lombok.extern.slf4j.Slf4j;

/**
 * Basic, JDBC implementation of the client details service.
 */
@Slf4j
public class JdbcClientDetailsService implements ClientDetailsService, ClientRegistrationService {

	protected final static Cache<String, ClientDetails> detailsCache =
			Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(200000).build();

	private JsonMapper mapper = createJsonMapper();

	private static final String CLIENT_FIELDS_FOR_UPDATE = "resource_ids, scope, "
			+ "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
			+ "refresh_token_validity, additional_information, auto_approve, approval_prompt , "
			+ "algorithm, algorithm_key, encryption_type, signature, signature_key, subject, "
			+ "user_info_response, issuer, audience, pkce, protocol , inst_id ";

	private static final String CLIENT_FIELDS = "client_secret, " + CLIENT_FIELDS_FOR_UPDATE;

	private static final String BASE_FIND_STATEMENT =
			"select client_id, " + CLIENT_FIELDS + " from auth_app_oauth_client_detail";

	private static final String DEFAULT_FIND_STATEMENT = BASE_FIND_STATEMENT + " order by client_id";

	private static final String DEFAULT_SELECT_STATEMENT = BASE_FIND_STATEMENT + " where client_id = ?";

	private static final String DEFAULT_INSERT_STATEMENT = "insert into auth_app_oauth_client_detail (" + CLIENT_FIELDS
			+ ", client_id) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private static final String DEFAULT_UPDATE_STATEMENT = "update auth_app_oauth_client_detail " + "set "
			+ CLIENT_FIELDS_FOR_UPDATE.replaceAll(", ", "=?, ") + "=? where client_id = ?";

	private static final String DEFAULT_UPDATE_SECRET_STATEMENT =
			"update auth_app_oauth_client_detail " + "set client_secret = ? where client_id = ?";

	private static final String DEFAULT_DELETE_STATEMENT =
			"delete from auth_app_oauth_client_detail where client_id = ?";

	private RowMapper<ClientDetails> rowMapper = new ClientDetailsRowMapper();

	private String deleteClientDetailsSql = DEFAULT_DELETE_STATEMENT;

	private String findClientDetailsSql = DEFAULT_FIND_STATEMENT;

	private String updateClientDetailsSql = DEFAULT_UPDATE_STATEMENT;

	private String updateClientSecretSql = DEFAULT_UPDATE_SECRET_STATEMENT;

	private String insertClientDetailsSql = DEFAULT_INSERT_STATEMENT;

	private String selectClientDetailsSql = DEFAULT_SELECT_STATEMENT;

	private PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();

	private final JdbcTemplate jdbcTemplate;

	private JdbcListFactory listFactory;

	public JdbcClientDetailsService(DataSource dataSource) {
		Assert.notNull(dataSource, "DataSource required");
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.listFactory = new DefaultJdbcListFactory(new NamedParameterJdbcTemplate(jdbcTemplate));
	}

	/**
	 * @param passwordEncoder the password encoder to set
	 */
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public ClientDetails loadClientByClientId(String clientId, boolean cached) {
		// cache in memory
		ClientDetails details = null;
		try {
			if (cached) {
				details = detailsCache.getIfPresent(clientId);
				if (details == null) {
					details =
							jdbcTemplate.queryForObject(selectClientDetailsSql, new ClientDetailsRowMapper(), clientId);
					detailsCache.put(clientId, details);
				}
			} else {
				details = jdbcTemplate.queryForObject(selectClientDetailsSql, new ClientDetailsRowMapper(), clientId);
			}
		} catch (EmptyResultDataAccessException e) {
			throw new NoSuchClientException("No client with requested id: " + clientId);
		}
		return details;
	}

	@Override
	public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
		try {
			jdbcTemplate.update(insertClientDetailsSql, getFields(clientDetails));
		} catch (DuplicateKeyException e) {
			throw new ClientAlreadyExistsException("Client already exists: " + clientDetails.getClientId(), e);
		}
	}

	@Override
	public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
		int count = jdbcTemplate.update(updateClientDetailsSql, getFieldsForUpdate(clientDetails));
		if (count != 1) {
			throw new NoSuchClientException("No client found with id = " + clientDetails.getClientId());
		}
		detailsCache.invalidate(clientDetails.getClientId());
	}

	@Override
	public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
		int count = jdbcTemplate.update(updateClientSecretSql, passwordEncoder.encode(secret), clientId);
		if (count != 1) {
			throw new NoSuchClientException("No client found with id = " + clientId);
		}
	}

	@Override
	public void removeClientDetails(String clientId) throws NoSuchClientException {
		int count = jdbcTemplate.update(deleteClientDetailsSql, clientId);
		if (count != 1) {
			throw new NoSuchClientException("No client found with id = " + clientId);
		}
	}

	@Override
	public List<ClientDetails> listClientDetails() {
		return listFactory.getList(findClientDetailsSql, Collections.<String, Object>emptyMap(), rowMapper);
	}

	private Object[] getFields(ClientDetails clientDetails) {
		Object[] fieldsForUpdate = getFieldsForUpdate(clientDetails);
		Object[] fields = new Object[fieldsForUpdate.length + 1];
		System.arraycopy(fieldsForUpdate, 0, fields, 1, fieldsForUpdate.length);
		fields[0] = clientDetails.getClientSecret() != null ? passwordEncoder.encode(clientDetails.getClientSecret())
				: null;
		return fields;
	}

	private Object[] getFieldsForUpdate(ClientDetails clientDetails) {
		String json = null;
		try {
			json = mapper.write(clientDetails.getAdditionalInformation());
		} catch (Exception e) {
			log.warn("Could not serialize additional information: " + clientDetails, e);
		}
		return new Object[] {
				clientDetails.getResourceIds() != null
						? StringUtils.collectionToCommaDelimitedString(clientDetails.getResourceIds())
						: null,
				clientDetails.getScope() != null
						? StringUtils.collectionToCommaDelimitedString(clientDetails.getScope())
						: null,
				clientDetails.getAuthorizedGrantTypes() != null
						? StringUtils.collectionToCommaDelimitedString(clientDetails.getAuthorizedGrantTypes())
						: null,
				clientDetails.getRegisteredRedirectUri() != null
						? StringUtils.collectionToCommaDelimitedString(clientDetails.getRegisteredRedirectUri())
						: null,
				clientDetails.getAuthorities() != null
						? StringUtils.collectionToCommaDelimitedString(clientDetails.getAuthorities())
						: null,
				clientDetails.getAccessTokenValiditySeconds(), clientDetails.getRefreshTokenValiditySeconds(), json,
				getAutoApproveScopes(clientDetails), clientDetails.getApprovalPrompt(), clientDetails.getAlgorithm(),
				clientDetails.getAlgorithmKey(), clientDetails.getEncryptionMethod(), clientDetails.getSignature(),
				clientDetails.getSignatureKey(), clientDetails.getSubject(), clientDetails.getUserInfoResponse(),
				clientDetails.getIssuer(), clientDetails.getAudience(), clientDetails.getPkce(),
				clientDetails.getProtocol(), clientDetails.getInstId(),

				clientDetails.getClientId()

		};
	}

	private String getAutoApproveScopes(ClientDetails clientDetails) {
		if (clientDetails.isAutoApprove("true")) {
			return "true"; // all scopes autoapproved
		}
		Set<String> scopes = new HashSet<String>();
		for (String scope : clientDetails.getScope()) {
			if (clientDetails.isAutoApprove(scope)) {
				scopes.add(scope);
			}
		}
		return StringUtils.collectionToCommaDelimitedString(scopes);
	}

	public void setSelectClientDetailsSql(String selectClientDetailsSql) {
		this.selectClientDetailsSql = selectClientDetailsSql;
	}

	public void setDeleteClientDetailsSql(String deleteClientDetailsSql) {
		this.deleteClientDetailsSql = deleteClientDetailsSql;
	}

	public void setUpdateClientDetailsSql(String updateClientDetailsSql) {
		this.updateClientDetailsSql = updateClientDetailsSql;
	}

	public void setUpdateClientSecretSql(String updateClientSecretSql) {
		this.updateClientSecretSql = updateClientSecretSql;
	}

	public void setInsertClientDetailsSql(String insertClientDetailsSql) {
		this.insertClientDetailsSql = insertClientDetailsSql;
	}

	public void setFindClientDetailsSql(String findClientDetailsSql) {
		this.findClientDetailsSql = findClientDetailsSql;
	}

	/**
	 * @param listFactory the list factory to set
	 */
	public void setListFactory(JdbcListFactory listFactory) {
		this.listFactory = listFactory;
	}

	/**
	 * @param rowMapper the rowMapper to set
	 */
	public void setRowMapper(RowMapper<ClientDetails> rowMapper) {
		this.rowMapper = rowMapper;
	}

	/**
	 * Row mapper for ClientDetails.
	 * 
	 * @author Dave Syer
	 * 
	 */
	private static class ClientDetailsRowMapper implements RowMapper<ClientDetails> {

		private JsonMapper mapper = createJsonMapper();

		@Override
		public ClientDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
			BaseClientDetails details = new BaseClientDetails(rs.getString(1), rs.getString(3), rs.getString(4),
					rs.getString(5), rs.getString(7), rs.getString(6));
			details.setClientSecret(rs.getString(2));
			if (rs.getObject(8) != null) {
				details.setAccessTokenValiditySeconds(rs.getInt(8));
			}
			if (rs.getObject(9) != null) {
				details.setRefreshTokenValiditySeconds(rs.getInt(9));
			}

			details.setAlgorithm(rs.getString("algorithm"));
			details.setAlgorithmKey(rs.getString("algorithm_key"));
			details.setEncryptionMethod(rs.getString("encryption_type"));

			details.setSignature(rs.getString("signature"));
			details.setSignatureKey(rs.getString("signature_key"));
			details.setSubject(rs.getString("subject"));
			details.setUserInfoResponse(rs.getString("user_info_response"));
			details.setAudience(rs.getString("audience"));
			details.setIssuer(rs.getString("issuer"));
			details.setApprovalPrompt(rs.getString("approval_prompt"));
			details.setPkce(rs.getString("pkce"));
			details.setProtocol(rs.getString("protocol"));
			details.setInstId(rs.getString("inst_id"));
			String json = rs.getString(10);
			if (json != null) {
				try {
					@SuppressWarnings("unchecked")
					Map<String, Object> additionalInformation = mapper.read(json, Map.class);
					details.setAdditionalInformation(additionalInformation);
				} catch (Exception e) {
					log.warn("Could not decode JSON for additional information: " + details, e);
				}
			}
			String scopes = rs.getString(11);
			if (scopes != null) {
				details.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(scopes));
			}
			return details;
		}
	}

	interface JsonMapper {

		String write(Object input) throws Exception;

		<T> T read(String input, Class<T> type) throws Exception;
	}

	private static JsonMapper createJsonMapper() {
		if (ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", null)) {
			return new Jackson2Mapper();
		}
		return new NotSupportedJsonMapper();
	}

	private static class Jackson2Mapper implements JsonMapper {

		private com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();

		@Override
		public String write(Object input) throws Exception {
			return mapper.writeValueAsString(input);
		}

		@Override
		public <T> T read(String input, Class<T> type) throws Exception {
			return mapper.readValue(input, type);
		}
	}

	private static class NotSupportedJsonMapper implements JsonMapper {

		@Override
		public String write(Object input) throws Exception {
			throw new UnsupportedOperationException(
					"Neither Jackson 1 nor 2 is available so JSON conversion cannot be done");
		}

		@Override
		public <T> T read(String input, Class<T> type) throws Exception {
			throw new UnsupportedOperationException(
					"Neither Jackson 1 nor 2 is available so JSON conversion cannot be done");
		}
	}

}
