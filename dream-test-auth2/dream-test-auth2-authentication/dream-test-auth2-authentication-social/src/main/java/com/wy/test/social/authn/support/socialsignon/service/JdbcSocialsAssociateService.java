package com.wy.test.social.authn.support.socialsignon.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.wy.test.core.constants.ConstDatabase;
import com.wy.test.core.entity.SocialAssociateEntity;

import dream.flying.flower.generator.GeneratorStrategyContext;

public class JdbcSocialsAssociateService implements SocialsAssociateService {

	private static final Logger _logger = LoggerFactory.getLogger(JdbcSocialsAssociateService.class);

	private static final String DEFAULT_DEFAULT_INSERT_STATEMENT =
			"insert into  auth_social_associate(id, userid , username , provider , socialuserid , accesstoken , socialuserinfo , exattribute , instid)values( ? , ? , ? , ? , ?, ? , ? , ?, ?)";

	private static final String DEFAULT_DEFAULT_INSERT_STATEMENT_ORACLE =
			"insert into  auth_social_associate(id, userid , username , provider , socialuserid , accesstoken , socialuserinfo , exattribute , instid)values( ? , ? , ? , ? , ?, ? , ? , ?, ?)";

	private static final String DEFAULT_DEFAULT_SIGNON_SELECT_STATEMENT =
			"select id, userid , username , provider , socialuserid , accesstoken , socialuserinfo , exattribute , createddate , updateddate , instid from auth_social_associate where provider = ?  and socialuserid = ? and instId = ?";

	private static final String DEFAULT_DEFAULT_BIND_SELECT_STATEMENT =
			"select id, userid , username , provider , socialuserid , accesstoken , socialuserinfo , exattribute , createddate , updateddate , instid from auth_social_associate where userid = ?";

	private static final String DEFAULT_DEFAULT_DELETE_STATEMENT =
			"delete from  auth_social_associate where  userid = ? and provider = ?";

	private static final String DEFAULT_DEFAULT_UPDATE_STATEMENT =
			"update auth_social_associate  set accesstoken  = ? , socialuserinfo = ? , exattribute = ? ,updateddate = ?  where id = ?";

	private final JdbcTemplate jdbcTemplate;

	public JdbcSocialsAssociateService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public boolean insert(SocialAssociateEntity socialsAssociate) {
		GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();
		socialsAssociate.setId(generatorStrategyContext.generate());
		jdbcTemplate.update(
				ConstDatabase.compare(ConstDatabase.ORACLE) ? DEFAULT_DEFAULT_INSERT_STATEMENT_ORACLE
						: DEFAULT_DEFAULT_INSERT_STATEMENT,
				new Object[] { socialsAssociate.getId(), socialsAssociate.getUserId(), socialsAssociate.getUsername(),
						socialsAssociate.getProvider(), socialsAssociate.getSocialUserId(),
						socialsAssociate.getAccessToken(), socialsAssociate.getSocialUserInfo(),
						socialsAssociate.getExtendAttribute(), socialsAssociate.getInstId() },
				new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
						Types.VARCHAR, Types.VARCHAR, Types.VARCHAR });
		return true;
	}

	@Override
	public boolean delete(SocialAssociateEntity socialsAssociate) {
		jdbcTemplate.update(DEFAULT_DEFAULT_DELETE_STATEMENT,
				new Object[] { socialsAssociate.getUserId(), socialsAssociate.getProvider() },
				new int[] { Types.VARCHAR, Types.VARCHAR });
		return true;
	}

	@Override
	public SocialAssociateEntity get(SocialAssociateEntity socialsAssociate) {
		List<SocialAssociateEntity> listsocialsAssociate = jdbcTemplate.query(DEFAULT_DEFAULT_SIGNON_SELECT_STATEMENT,
				new SocialsAssociateRowMapper(), socialsAssociate.getProvider(), socialsAssociate.getSocialUserId(),
				socialsAssociate.getInstId());
		_logger.debug("list socialsAssociate " + listsocialsAssociate);
		return (listsocialsAssociate.size() > 0) ? listsocialsAssociate.get(0) : null;
	}

	@Override
	public List<SocialAssociateEntity> query(SocialAssociateEntity socialsAssociate) {
		List<SocialAssociateEntity> listsocialsAssociate = jdbcTemplate.query(DEFAULT_DEFAULT_BIND_SELECT_STATEMENT,
				new SocialsAssociateRowMapper(), socialsAssociate.getUserId());
		_logger.debug("query bind  SocialSignOnUser " + listsocialsAssociate);
		return listsocialsAssociate;
	}

	@Override
	public boolean update(SocialAssociateEntity socialsAssociate) {
		jdbcTemplate.update(DEFAULT_DEFAULT_UPDATE_STATEMENT,
				new Object[] { socialsAssociate.getAccessToken(), socialsAssociate.getSocialUserInfo(),
						socialsAssociate.getExtendAttribute(), new Date(), socialsAssociate.getId() },
				new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP, Types.VARCHAR });
		return false;
	}

	private final class SocialsAssociateRowMapper implements RowMapper<SocialAssociateEntity> {

		@Override
		public SocialAssociateEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			SocialAssociateEntity socialsAssociate = new SocialAssociateEntity();
			socialsAssociate.setId(rs.getString(1));
			socialsAssociate.setUserId(rs.getString(2));
			socialsAssociate.setUsername(rs.getString(3));
			socialsAssociate.setProvider(rs.getString(4));
			socialsAssociate.setSocialUserId(rs.getString(5));
			socialsAssociate.setAccessToken(rs.getString(6));
			socialsAssociate.setSocialUserInfo(rs.getString(7));
			socialsAssociate.setExtendAttribute(rs.getString(8));
			socialsAssociate.setCreateTime(rs.getDate(9));
			socialsAssociate.setUpdateTime(rs.getDate(10));
			socialsAssociate.setInstId(rs.getString(11));
			return socialsAssociate;
		}
	}
}