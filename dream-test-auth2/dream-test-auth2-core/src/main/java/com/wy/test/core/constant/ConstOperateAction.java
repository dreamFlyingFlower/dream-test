package com.wy.test.core.constant;

import java.util.concurrent.ConcurrentHashMap;

public final class ConstOperateAction {

	public static final String CREATE = "create";

	public static final String DELETE = "delete";

	public static final String UPDATE = "update";

	public static final String CHANGE_PASSWORD = "change_password";

	public static final String ADD_MEMBER = "add_member";

	public static final String DELETE_MEMBER = "delete_member";

	public static final String ENABLE = "enable";

	public static final String DISABLE = "disable";

	public static final String INACTIVE = "inactive";

	public static final String LOCK = "lock";

	public static final String UNLOCK = "unlock";

	public static final String VIEW = "view";

	public static ConcurrentHashMap<Integer, String> statusActon;

	static {
		statusActon = new ConcurrentHashMap<Integer, String>();
		statusActon.put(ConstStatus.ACTIVE, ENABLE);
		statusActon.put(ConstStatus.INACTIVE, INACTIVE);
		statusActon.put(ConstStatus.DISABLED, DISABLE);
		statusActon.put(ConstStatus.LOCK, LOCK);
		statusActon.put(ConstStatus.UNLOCK, UNLOCK);
		statusActon.put(ConstStatus.DELETE, DELETE);
	}
}