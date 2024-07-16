package com.wy.test.core.authn.session;

import java.time.LocalDateTime;
import java.util.List;

import com.wy.test.entity.HistoryLogin;

public interface SessionManager {

	public void create(String sessionId, Session session);

	public Session remove(String sessionId);

	public Session get(String sessionId);

	public Session refresh(String sessionId, LocalDateTime refreshTime);

	public Session refresh(String sessionId);

	public List<HistoryLogin> querySessions();

	public int getValiditySeconds();

	public void terminate(String sessionId, String userId, String username);
}
