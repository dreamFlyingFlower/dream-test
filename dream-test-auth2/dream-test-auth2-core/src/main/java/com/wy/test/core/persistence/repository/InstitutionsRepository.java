package com.wy.test.core.persistence.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.entity.InstitutionEntity;

public class InstitutionsRepository {

	private static Logger _logger = LoggerFactory.getLogger(InstitutionsRepository.class);

	private static final String SELECT_STATEMENT =
			"select * from  mxk_institutions where id = ? or domain = ? or consoledomain = ?";

	private static final String DEFAULT_INSTID = "1";

	protected static final Cache<String, InstitutionEntity> institutionsStore =
			Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).build();

	// id domain mapping
	protected static final ConcurrentHashMap<String, String> mapper = new ConcurrentHashMap<String, String>();

	protected JdbcTemplate jdbcTemplate;

	public InstitutionsRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public InstitutionEntity get(String instIdOrDomain) {
		_logger.trace(" instId {}", instIdOrDomain);
		InstitutionEntity inst = getByInstIdOrDomain(instIdOrDomain);
		if (inst == null) {// use default inst
			inst = getByInstIdOrDomain(DEFAULT_INSTID);
			institutionsStore.put(instIdOrDomain, inst);
		}
		return inst;
	}

	private InstitutionEntity getByInstIdOrDomain(String instIdOrDomain) {
		_logger.trace(" instId {}", instIdOrDomain);
		InstitutionEntity inst = institutionsStore
				.getIfPresent(mapper.get(instIdOrDomain) == null ? DEFAULT_INSTID : mapper.get(instIdOrDomain));
		if (inst == null) {
			List<InstitutionEntity> institutions = jdbcTemplate.query(SELECT_STATEMENT, new InstitutionsRowMapper(),
					instIdOrDomain, instIdOrDomain, instIdOrDomain);

			if (institutions != null && institutions.size() > 0) {
				inst = institutions.get(0);
			}
			if (inst != null) {
				institutionsStore.put(inst.getDomain(), inst);
				institutionsStore.put(inst.getConsoleDomain(), inst);
				mapper.put(inst.getId(), inst.getDomain());
			}
		}

		return inst;
	}

	public class InstitutionsRowMapper implements RowMapper<InstitutionEntity> {

		@Override
		public InstitutionEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			InstitutionEntity institution = new InstitutionEntity();
			institution.setId(rs.getString("id"));
			institution.setName(rs.getString("name"));
			institution.setFullName(rs.getString("fullname"));
			institution.setLogo(rs.getString("logo"));
			institution.setDomain(rs.getString("domain"));
			institution.setFrontTitle(rs.getString("fronttitle"));
			institution.setConsoleDomain(rs.getString("consoledomain"));
			institution.setConsoleTitle(rs.getString("consoletitle"));
			institution.setCaptcha(rs.getString("captcha"));
			institution.setDefaultUri(rs.getString("defaultUri"));
			return institution;
		}
	}
}
