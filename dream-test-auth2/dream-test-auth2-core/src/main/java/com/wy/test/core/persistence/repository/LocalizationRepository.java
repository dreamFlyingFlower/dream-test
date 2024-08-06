package com.wy.test.core.persistence.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.constants.ConstTimeInterval;
import com.wy.test.core.entity.LocalizationEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LocalizationRepository {

	private static final String INSERT_STATEMENT =
			"insert into auth_localization (id, property,langzh,langen,status,description,instid)values(?,?,?,?,?,?,?)";

	private static final String UPDATE_STATEMENT = "update auth_localization set langzh = ? , langen =? where id = ?";

	private static final String DELETE_STATEMENT = "delete from  auth_localization where id = ?";

	private static final String SELECT_STATEMENT =
			"select * from  auth_localization where ( id = ? ) or (property = ? and instid = ?)";

	private static final Pattern PATTERN_HTML = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);

	protected InstitutionsRepository institutionService;

	JdbcTemplate jdbcTemplate;

	protected static final Cache<String, String> localizationStore =
			Caffeine.newBuilder().expireAfterWrite(ConstTimeInterval.ONE_HOUR, TimeUnit.SECONDS).build();

	public LocalizationRepository() {

	}

	public String getLocale(String code, String htmlTag, Locale locale, String inst) {
		String message = "";
		htmlTag = (htmlTag == null || htmlTag.equalsIgnoreCase("true")) ? "tag" : "rtag";

		if (code.equals("global.logo")) {
			message = institutionService.get(inst).getLogo();
		} else if (code.equals("global.title")) {
			message = getFromStore(code, htmlTag, locale, inst);
			if (message == null) {
				message = institutionService.get(inst).getFrontTitle();
			}
		} else if (code.equals("global.consoleTitle")) {
			message = getFromStore(code, htmlTag, locale, inst);
			if (message == null) {
				message = institutionService.get(inst).getConsoleTitle();
			}
		} else {
			message = getFromStore(code, htmlTag, locale, inst);
		}
		if (htmlTag.equalsIgnoreCase("rtag")) {
			message = clearHTMLToString(message);
		}
		log.trace("{} = {}", code, message);
		return message == null ? "" : message;
	}

	public String clearHTMLToString(String message) {
		return PATTERN_HTML.matcher(message).replaceAll("");
	}

	public String getFromStore(String code, String htmlTag, Locale locale, String inst) {
		String message = localizationStore.getIfPresent(code + "_" + locale.getLanguage() + "_" + inst);
		if (message != null)
			return message;
		LocalizationEntity localization = get(code, inst);
		if (localization != null) {
			localizationStore.put(code + "_en_" + inst, localization.getLangEn());
			localizationStore.put(code + "_zh_" + inst, localization.getLangZh());
			if (locale.getLanguage().equals("en")) {
				message = localization.getLangEn();
			} else {
				message = localization.getLangZh();
			}
			if (message != null)
				return message;
		}
		return message;
	}

	public void setInstitutionService(InstitutionsRepository institutionService) {
		this.institutionService = institutionService;
	}

	public boolean insert(LocalizationEntity localization) {
		return jdbcTemplate.update(INSERT_STATEMENT,
				new Object[] { localization.getId(), localization.getProperty(), localization.getLangZh(),
						localization.getLangEn(), localization.getStatus(), localization.getRemark(),
						localization.getInstId() },
				new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR,
						Types.VARCHAR, }) > 0;
	}

	public boolean update(LocalizationEntity localization) {
		jdbcTemplate.update(UPDATE_STATEMENT, localization.getLangZh(), localization.getLangEn(), localization.getId());
		return true;
	}

	public boolean remove(String id) {
		return jdbcTemplate.update(DELETE_STATEMENT, id) > 0;
	}

	public LocalizationEntity get(String property, String instId) {
		log.debug("load property from database , property {} ,instId {}", property, instId);
		List<LocalizationEntity> localizations =
				jdbcTemplate.query(SELECT_STATEMENT, new LocalizationRowMapper(), property, property, instId);
		return (localizations == null || localizations.size() == 0) ? null : localizations.get(0);
	}

	public LocalizationRepository(JdbcTemplate jdbcTemplate, InstitutionsRepository institutionService) {
		super();
		this.institutionService = institutionService;
		this.jdbcTemplate = jdbcTemplate;
	}

	public class LocalizationRowMapper implements RowMapper<LocalizationEntity> {

		@Override
		public LocalizationEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			LocalizationEntity localization = new LocalizationEntity();
			localization.setId(rs.getString("id"));
			localization.setProperty(rs.getString("property"));
			localization.setLangZh(rs.getString("langzh"));
			localization.setLangEn(rs.getString("langen"));
			localization.setStatus(rs.getInt("status"));
			localization.setRemark(rs.getString("description"));
			localization.setInstId(rs.getString("instid"));
			return localization;
		}
	}
}