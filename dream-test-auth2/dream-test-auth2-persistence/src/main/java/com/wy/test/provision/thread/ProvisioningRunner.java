package com.wy.test.provision.thread;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.dromara.mybatis.jpa.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.wy.test.core.entity.ChangePassword;
import com.wy.test.core.entity.Connectors;
import com.wy.test.core.entity.Organizations;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.web.HttpRequestAdapter;
import com.wy.test.core.web.WebContext;
import com.wy.test.crypto.password.PasswordReciprocal;
import com.wy.test.entity.Message;
import com.wy.test.persistence.service.ConnectorsService;
import com.wy.test.provision.ProvisionAction;
import com.wy.test.provision.ProvisionMessage;
import com.wy.test.provision.ProvisionTopic;
import com.wy.test.util.DateUtils;
import com.wy.test.util.JsonUtils;
import com.wy.test.util.ObjectTransformer;

public class ProvisioningRunner {

	private static final Logger _logger = LoggerFactory.getLogger(ProvisioningRunner.class);

	static final String PROVISION_SELECT_STATEMENT =
			"select * from mxk_history_provisions where connected = 0 order by sendtime asc limit 500";

	static final String PROVISION_UPDATE_STATEMENT =
			"update mxk_history_provisions set connected = connected + 1  where  id = ?";

	static final String PROVISION_LOG_INSERT_STATEMENT =
			"insert into mxk_history_connector(id,conname,topic,actiontype,sourceid,sourcename,synctime,result,instid) values (? , ? , ? , ? , ? , ?  , ?  , ?  , ? )";

	JdbcTemplate jdbcTemplate;

	ConnectorsService connectorsService;

	public ProvisioningRunner(ConnectorsService connectorsService, JdbcTemplate jdbcTemplate) {
		this.connectorsService = connectorsService;
		this.jdbcTemplate = jdbcTemplate;
	}

	public void provisions() {
		try {
			List<Connectors> listConnectors = connectorsService.query(new Query().eq("status", 1).eq("justintime", 1));
			List<ProvisionMessage> listProvisionMessage =
					jdbcTemplate.query(PROVISION_SELECT_STATEMENT, new ProvisionMessageRowMapper());
			for (ProvisionMessage msg : listProvisionMessage) {
				_logger.debug("Provision message {}", msg);
				for (Connectors connector : listConnectors) {
					_logger.debug("Provision message to connector {}", connector);
					provision(msg, connector);
				}
			}
		} catch (Exception e) {
			_logger.error("provisions Exception", e);
		}
	}

	public void provision(ProvisionMessage provisionMessage, Connectors connector) {
		if (Integer.parseInt(connector.getInstId()) == provisionMessage.getInstId()) {
			String url = connector.getProviderUrl();
			if (!url.endsWith("/")) {
				url = url + "/";
			}
			String resultMessage = "";
			String objectId = "";
			String objectName = "";
			if (provisionMessage.getTopic().equalsIgnoreCase(ProvisionTopic.USERINFO_TOPIC)) {
				UserInfo user = (UserInfo) ObjectTransformer.deserialize(provisionMessage.getContent());
				user.setPassword(null);
				user.setDecipherable(null);
				objectId = user.getId();
				objectName = user.getDisplayName() + "(" + user.getUsername() + ")";
				resultMessage = provisionUser(user, url, provisionMessage.getActionType(), connector);
				provisionLog(connector.getConnName(), "Users", provisionMessage.getActionType(), objectId, objectName,
						resultMessage, provisionMessage.getInstId());
			} else if (provisionMessage.getTopic().equalsIgnoreCase(ProvisionTopic.PASSWORD_TOPIC)) {
				ChangePassword changePassword =
						(ChangePassword) ObjectTransformer.deserialize(provisionMessage.getContent());
				objectId = changePassword.getUserId();
				objectName = changePassword.getDisplayName() + "(" + changePassword.getUsername() + ")";
				resultMessage =
						provisionChangePassword(changePassword, url, provisionMessage.getActionType(), connector);
				provisionLog(connector.getConnName(), "Password", provisionMessage.getActionType(), objectId,
						objectName, resultMessage, provisionMessage.getInstId());
			} else if (provisionMessage.getTopic().equalsIgnoreCase(ProvisionTopic.ORG_TOPIC)) {
				Organizations organization =
						(Organizations) ObjectTransformer.deserialize(provisionMessage.getContent());
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
		Message<?> resultMsg = null;
		String result = "success";

		if (resultMessage != null) {
			resultMsg = JsonUtils.stringToObject(resultMessage, Message.class);
		}

		if (resultMsg == null || resultMsg.getCode() != 0) {
			result = "fail";
		}

		jdbcTemplate.update(PROVISION_LOG_INSERT_STATEMENT, WebContext.genId(), conName, topic,
				actionType.replace("_ACTION", "").toLowerCase(), sourceId, sourceName,
				DateUtils.getCurrentDateTimeAsString(), result, instid);
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

	String provisionUser(UserInfo user, String baseUrl, String actionType, Connectors connector) {
		baseUrl = baseUrl + "Users/" + getActionType(actionType);
		_logger.debug("URL {} ", baseUrl);
		return new HttpRequestAdapter().addHeaderAuthorizationBasic(connector.getPrincipal(),
				PasswordReciprocal.getInstance().decoder(connector.getCredentials())).post(baseUrl, user);
	}

	String provisionOrganization(Organizations organization, String baseUrl, String actionType, Connectors connector) {
		baseUrl = baseUrl + "Organizations/" + getActionType(actionType);
		_logger.debug("URL {} ", baseUrl);
		return new HttpRequestAdapter()
				.addHeaderAuthorizationBasic(connector.getPrincipal(),
						PasswordReciprocal.getInstance().decoder(connector.getCredentials()))
				.post(baseUrl, organization);
	}

	String provisionChangePassword(ChangePassword changePassword, String baseUrl, String actionType,
			Connectors connector) {
		baseUrl = baseUrl + "Users/changePassword";
		_logger.debug("URL {} ", baseUrl);
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
