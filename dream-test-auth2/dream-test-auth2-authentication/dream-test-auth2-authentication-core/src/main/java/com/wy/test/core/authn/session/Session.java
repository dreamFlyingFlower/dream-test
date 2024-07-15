/*
 * Copyright [2020] [MaxKey of copyright http://www.maxkey.top]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.wy.test.core.authn.session;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;

import org.springframework.security.core.Authentication;

import com.wy.test.entity.apps.Apps;
import com.wy.test.web.WebContext;

public class Session implements Serializable {

	private static final long serialVersionUID = 9008067569150338296L;

	public static final String SESSION_PREFIX = "OT";

	public static final int MAX_EXPIRY_DURATION = 60 * 5; // default 5 minutes.

	public String id;

	public LocalDateTime startTimestamp;

	public LocalDateTime lastAccessTime;

	public LocalDateTime expiredTime;

	public Authentication authentication;

	private HashMap<String, Apps> authorizedApps = new HashMap<String, Apps>();

	public Session() {
		super();
		this.id = WebContext.genId();
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

	public HashMap<String, Apps> getAuthorizedApps() {
		return authorizedApps;
	}

	public void setAuthorizedApps(HashMap<String, Apps> authorizedApps) {
		this.authorizedApps = authorizedApps;
	}

	public void setAuthorizedApp(Apps authorizedApp) {
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
