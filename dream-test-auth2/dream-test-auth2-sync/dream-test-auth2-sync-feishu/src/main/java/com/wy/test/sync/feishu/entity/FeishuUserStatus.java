package com.wy.test.sync.feishu.entity;

public class FeishuUserStatus {

	boolean is_frozen;

	boolean is_resigned;

	boolean is_activated;

	boolean is_exited;

	boolean is_unjoin;

	public FeishuUserStatus() {
		super();
	}

	public boolean isIs_frozen() {
		return is_frozen;
	}

	public void setIs_frozen(boolean is_frozen) {
		this.is_frozen = is_frozen;
	}

	public boolean isIs_resigned() {
		return is_resigned;
	}

	public void setIs_resigned(boolean is_resigned) {
		this.is_resigned = is_resigned;
	}

	public boolean isIs_activated() {
		return is_activated;
	}

	public void setIs_activated(boolean is_activated) {
		this.is_activated = is_activated;
	}

	public boolean isIs_exited() {
		return is_exited;
	}

	public void setIs_exited(boolean is_exited) {
		this.is_exited = is_exited;
	}

	public boolean isIs_unjoin() {
		return is_unjoin;
	}

	public void setIs_unjoin(boolean is_unjoin) {
		this.is_unjoin = is_unjoin;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FeishuUserStatus [is_frozen=");
		builder.append(is_frozen);
		builder.append(", is_resigned=");
		builder.append(is_resigned);
		builder.append(", is_activated=");
		builder.append(is_activated);
		builder.append(", is_exited=");
		builder.append(is_exited);
		builder.append(", is_unjoin=");
		builder.append(is_unjoin);
		builder.append("]");
		return builder.toString();
	}

}
