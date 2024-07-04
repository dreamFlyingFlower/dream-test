package dream.flying.flower.framework.web.loadbalancer;

import lombok.Getter;

/**
 * 表示典型服务器(或可寻址节点)的类,即 Host:port 标识符
 *
 * @author 飞花梦影
 * @date 2024-06-03 16:16:12
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
public class BalancerServer {

	/**
	 * Additional meta information of a server, which contains information of the targeting application, as well as
	 * server identification specific for a deployment environment, for example, AWS.
	 */
	public static interface MetaInfo {

		/**
		 * @return the name of application that runs on this server, null if not available
		 */
		public String getAppName();

		/**
		 * @return the group of the server, for example, auto scaling group ID in AWS. Null if not available
		 */
		public String getServerGroup();

		/**
		 * @return A virtual address used by the server to register with discovery service. Null if not available
		 */
		public String getServiceIdForDiscovery();

		/**
		 * @return ID of the server
		 */
		public String getInstanceId();
	}

	public static final String UNKNOWN_ZONE = "UNKNOWN";

	private String host;

	private int port = 80;

	private String scheme;

	private volatile String id;

	private volatile boolean isAliveFlag;

	private String zone = UNKNOWN_ZONE;

	private volatile boolean readyToServe = true;

	private MetaInfo simpleMetaInfo = new MetaInfo() {

		@Override
		public String getAppName() {
			return null;
		}

		@Override
		public String getServerGroup() {
			return null;
		}

		@Override
		public String getServiceIdForDiscovery() {
			return null;
		}

		@Override
		public String getInstanceId() {
			return id;
		}
	};

	public BalancerServer(String host, int port) {
		this(null, host, port);
	}

	public BalancerServer(String scheme, String host, int port) {
		this.scheme = scheme;
		this.host = host;
		this.port = port;
		this.id = host + ":" + port;
		isAliveFlag = false;
	}

	public BalancerServer(String id) {
		setId(id);
		isAliveFlag = false;
	}

	public void setAlive(boolean isAliveFlag) {
		this.isAliveFlag = isAliveFlag;
	}

	public boolean isAlive() {
		return isAliveFlag;
	}

	static public String normalizeId(String id) {
		Pair<String, Integer> hostPort = getHostPort(id);
		if (hostPort == null) {
			return null;
		} else {
			return hostPort.first() + ":" + hostPort.second();
		}
	}

	private static String getScheme(String id) {
		if (id != null) {
			if (id.toLowerCase().startsWith("http://")) {
				return "http";
			} else if (id.toLowerCase().startsWith("https://")) {
				return "https";
			}
		}
		return null;
	}

	static Pair<String, Integer> getHostPort(String id) {
		if (id != null) {
			String host = null;
			int port = 80;

			if (id.toLowerCase().startsWith("http://")) {
				id = id.substring(7);
				port = 80;
			} else if (id.toLowerCase().startsWith("https://")) {
				id = id.substring(8);
				port = 443;
			}

			if (id.contains("/")) {
				int slash_idx = id.indexOf("/");
				id = id.substring(0, slash_idx);
			}

			int colon_idx = id.indexOf(':');

			if (colon_idx == -1) {
				host = id; // default
			} else {
				host = id.substring(0, colon_idx);
				try {
					port = Integer.parseInt(id.substring(colon_idx + 1));
				} catch (NumberFormatException e) {
					throw e;
				}
			}
			return new Pair<String, Integer>(host, port);
		} else {
			return null;
		}

	}

	public void setId(String id) {
		Pair<String, Integer> hostPort = getHostPort(id);
		if (hostPort != null) {
			this.id = hostPort.first() + ":" + hostPort.second();
			this.host = hostPort.first();
			this.port = hostPort.second();
			this.scheme = getScheme(id);
		} else {
			this.id = null;
		}
	}

	public void setSchemea(String scheme) {
		this.scheme = scheme;
	}

	public void setPort(int port) {
		this.port = port;

		if (host != null) {
			id = host + ":" + port;
		}
	}

	public void setHost(String host) {
		if (host != null) {
			this.host = host;
			id = host + ":" + port;
		}
	}

	public String getHostPort() {
		return host + ":" + port;
	}

	public MetaInfo getMetaInfo() {
		return simpleMetaInfo;
	}

	@Override
	public String toString() {
		return this.getId();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BalancerServer)) {
			return false;
		}
		BalancerServer svc = (BalancerServer) obj;
		return svc.getId().equals(this.getId());

	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash + (null == this.getId() ? 0 : this.getId().hashCode());
		return hash;
	}

	public final String getZone() {
		return zone;
	}

	public final void setZone(String zone) {
		this.zone = zone;
	}

	public final boolean isReadyToServe() {
		return readyToServe;
	}

	public final void setReadyToServe(boolean readyToServe) {
		this.readyToServe = readyToServe;
	}
}