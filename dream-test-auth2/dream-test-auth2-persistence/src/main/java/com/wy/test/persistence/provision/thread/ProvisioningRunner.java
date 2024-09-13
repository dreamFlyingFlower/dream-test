package com.wy.test.persistence.provision.thread;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wy.test.core.entity.ChangePassword;
import com.wy.test.core.entity.ConnectorEntity;
import com.wy.test.core.entity.OrgEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.web.HttpRequestAdapter;
import com.wy.test.core.web.AuthWebContext;
import com.wy.test.persistence.provision.ProvisionAction;
import com.wy.test.persistence.provision.ProvisionMessage;
import com.wy.test.persistence.provision.ProvisionTopic;
import com.wy.test.persistence.service.ConnectorService;

import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.helper.DateTimeHelper;
import dream.flying.flower.lang.SerializableHelper;
import dream.flying.flower.result.Result;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProvisioningRunner {

	static final String PROVISION_SELECT_STATEMENT =
			"select * from auth_history_provisions where connected = 0 order by sendtime asc limit 500";

	static final String PROVISION_UPDATE_STATEMENT =
			"update auth_history_provisions set connected = connected + 1  where  id = ?";

	static final String PROVISION_LOG_INSERT_STATEMENT =
			"insert into auth_history_connector(id,conname,topic,actiontype,sourceid,sourcename,synctime,result,instid) values (? , ? , ? , ? , ? , ?  , ?  , ?  , ? )";

	JdbcTemplate jdbcTemplate;

	ConnectorService connectorService;

	public ProvisioningRunner(ConnectorService connectorService, JdbcTemplate jdbcTemplate) {
		this.connectorService = connectorService;
		this.jdbcTemplate = jdbcTemplate;
	}

	public void provisions() {
		try {
			List<ConnectorEntity> listConnectors = connectorService.list(new LambdaQueryWrapper<ConnectorEntity>()
					.eq(ConnectorEntity::getStatus, 1).eq(ConnectorEntity::getJustInTime, 1));
			List<ProvisionMessage> listProvisionMessage =
					jdbcTemplate.query(PROVISION_SELECT_STATEMENT, new ProvisionMessageRowMapper());
			for (ProvisionMessage msg : listProvisionMessage) {
				log.debug("Provision message {}", msg);
				for (ConnectorEntity connector : listConnectors) {
					log.debug("Provision message to connector {}", connector);
					provision(msg, connector);
				}
			}
		} catch (Exception e) {
			log.error("provisions Exception", e);
		}
	}

	public void provision(ProvisionMessage provisionMessage, ConnectorEntity connector) {
		if (Integer.parseInt(connector.getInstId()) == provisionMessage.getInstId()) {
			String url = connector.getProviderUrl();
			if (!url.endsWith("/")) {
				url = url + "/";
			}
			String resultMessage = "";
			String objectId = "";
			String objectName = "";
			if (provisionMessage.getTopic().equalsIgnoreCase(ProvisionTopic.USERINFO_TOPIC)) {
				UserEntity user = (UserEntity) SerializableHelper.deserializeHex(provisionMessage.getContent());
				user.setPassword(null);
				user.setDecipherable(null);
				objectId = user.getId();
				objectName = user.getDisplayName() + "(" + user.getUsername() + ")";
				resultMessage = provisionUser(user, url, provisionMessage.getActionType(), connector);
				provisionLog(connector.getConnName(), "Users", provisionMessage.getActionType(), objectId, objectName,
						resultMessage, provisionMessage.getInstId());
			} else if (provisionMessage.getTopic().equalsIgnoreCase(ProvisionTopic.PASSWORD_TOPIC)) {
				ChangePassword changePassword =
						(ChangePassword) SerializableHelper.deserializeHex(provisionMessage.getContent());
				objectId = changePassword.getUserId();
				objectName = changePassword.getDisplayName() + "(" + changePassword.getUsername() + ")";
				resultMessage =
						provisionChangePassword(changePassword, url, provisionMessage.getActionType(), connector);
				provisionLog(connector.getConnName(), "Password", provisionMessage.getActionType(), objectId,
						objectName, resultMessage, provisionMessage.getInstId());
			} else if (provisionMessage.getTopic().equalsIgnoreCase(ProvisionTopic.ORG_TOPIC)) {
				OrgEntity organization = (OrgEntity) SerializableHelper.deserializeHex(provisionMessage.getContent());
				objectId = organization.getId();
				objectName = organization.getOrgName();
				resultMessage = provisionOrganization(organization, url, provisionMessage.getActionType(), connector);
				provisionLog(connector.getConnName(), "Organizations", provisionMessage.getActionType(), objectId,
						objectName, resultMessage, provisionMessage.getInstId());
			}

			jdbcTemplate.update(PROVISION_UPDATE_STATEMENT, provisionMessage.getId());
		}
	}

	public void provisionLog(String conName, String topic, String actionType, String sourceId, String sourceName,
			String resultMessage, int instid) {
		Result<?> resultMsg = null;
		String result = "success";

		if (resultMessage != null) {
			resultMsg = JsonHelpers.read(resultMessage, Result.class);
		}

		if (resultMsg == null || resultMsg.getCode() != 0) {
			result = "fail";
		}

		jdbcTemplate.update(PROVISION_LOG_INSERT_STATEMENT, AuthWebContext.genId(), conName, topic,
				actionType.replace("_ACTION", "").toLowerCase(), sourceId, sourceName, DateTimeHelper.formatDateTime(),
				result, instid);
	}

	public String getActionType(String actionType) {
		if (actionType.equalsIgnoreCase(ProvisionAction.CREATE_ACTION)) {
			return "create";
		} else if (actionType.equalsIgnoreCase(ProvisionAction.UPDATE_ACTION)) {
			return "update";
		} else if (actionType.equalsIgnoreCase(ProvisionAction.DELETE_ACTION)) {
			return "delete";
		}
		return "";
	}

	String provisionUser(UserEntity user, String baseUrl, String actionType, ConnectorEntity connector) {
		baseUrl = baseUrl + "Users/" + getActionType(actionType);
		log.debug("URL {} ", baseUrl);
		return new HttpRequestAdapter().addHeaderAuthorizationBasic(connector.getPrincipal(),
				PasswordReciprocal.getInstance().decoder(connector.getCredentials())).post(baseUrl, user);
	}

	String provisionOrganization(OrgEntity organization, String baseUrl, String actionType, ConnectorEntity connector) {
		baseUrl = baseUrl + "Organizations/" + getActionType(actionType);
		log.debug("URL {} ", baseUrl);
		return new HttpRequestAdapter()
				.addHeaderAuthorizationBasic(connector.getPrincipal(),
						PasswordReciprocal.getInstance().decoder(connector.getCredentials()))
				.post(baseUrl, organization);
	}

	String provisionChangePassword(ChangePassword changePassword, String baseUrl, String actionType,
			ConnectorEntity connector) {
		baseUrl = baseUrl + "Users/changePassword";
		log.debug("URL {} ", baseUrl);
		return new HttpRequestAdapter()
				.addHeaderAuthorizationBasic(connector.getPrincipal(),
						PasswordReciprocal.getInstance().decoder(connector.getCredentials()))
				.post(baseUrl, changePassword);
	}

	public class ProvisionMessageRowMapper implements RowMapper<ProvisionMessage> {

		@Override
		public ProvisionMessage mapRow(ResultSet rs, int rowNum) throws SQLException {
			ProvisionMessage msg = new ProvisionMessage();
			msg.setId(rs.getString("id"));
			msg.setActionType(rs.getString("actiontype"));
			msg.setTopic(rs.getString("topic"));
			msg.setContent(rs.getString("content"));
			msg.setConnected(rs.getInt("connected"));
			msg.setInstId(rs.getInt("instid"));
			return msg;
		}
	}
}