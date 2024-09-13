package com.wy.test.authentication.core.session;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.security.core.Authentication;

import com.wy.test.core.vo.AppVO;
import com.wy.test.core.web.AuthWebContext;

public class Session implements Serializable {

	private static final long serialVersionUID = 9008067569150338296L;

	public static final String SESSION_PREFIX = "OT";

	public static final int MAX_EXPIRY_DURATION = 60 * 5; // default 5 minutes.

	public String id;

	public LocalDateTime startTimestamp;

	public LocalDateTime lastAccessTime;

	public LocalDateTime expiredTime;

	public Authentication authentication;

	private HashMap<String, AppVO> authorizedApps = new HashMap<>();

	public Session() {
		super();
		this.id = AuthWebContext.genId();
		;
		this.startTimestamp = LocalDateTime.now();
		this.lastAccessTime = LocalDateTime.now();
	}

	public Session(String sessionId) {
		super();
		this.id = sessionId;
		this.startTimestamp = LocalDateTime.now();
		this.lastAccessTime = LocalDateTime.now();
	}

	public Session(String sessionId, Authentication authentication) {
		super();
		this.id = sessionId;
		this.authentication = authentication;
		this.startTimestamp = LocalDateTime.now();
		this.lastAccessTime = LocalDateTime.now();
	}

	public String getId() {
		return id;
	}

	public String getFormattedId() {
		return SESSION_PREFIX + id;
	}

	public void setId(String sessionId) {
		this.id = sessionId;
	}

	public LocalDateTime getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(LocalDateTime startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public LocalDateTime getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(LocalDateTime lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public LocalDateTime getExpiredTime() {
		return expiredTime;
	}

	public void setExpiredTime(LocalDateTime expiredTime) {
		this.expiredTime = expiredTime;
	}

	public Authentication getAuthentication() {
		return authentication;
	}

	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}

	public HashMap<String, AppVO> getAuthorizedApps() {
		return authorizedApps;
	}

	public void setAuthorizedApps(HashMap<String, AppVO> authorizedApps) {
		this.authorizedApps = authorizedApps;
	}

	public void setAuthorizedApp(AppVO authorizedApp) {
		this.authorizedApps.put(authorizedApp.getId(), authorizedApp);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Session [id=");
		builder.append(id);
		builder.append(", startTimestamp=");
		builder.append(startTimestamp);
		builder.append(", lastAccessTime=");
		builder.append(lastAccessTime);
		builder.append("]");
		return builder.toString();
	}

}
