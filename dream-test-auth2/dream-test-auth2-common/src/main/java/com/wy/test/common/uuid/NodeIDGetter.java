package com.wy.test.common.uuid;

public class NodeIDGetter {

	private static Object lock = new Object();

	private static byte[] nodeID = null;

	private NodeIDGetter() {
		throw new Error();
	}

	private static native void getNodeID(byte[] nodeID);

	public static byte[] getNodeID() {
		if (nodeID == null) {
			synchronized (lock) {
				if (nodeID == null) {
					try {
						byte[] data =
								new UUID("00000000-0000-0000-0000-" + System.getProperty("org.apache.tsik.uuid.nodeid"))
										.toByteArray();
						nodeID = new byte[6];
						System.arraycopy(data, 10, nodeID, 0, 6);
						return nodeID;
					} catch (Exception ex) {
						// phooey.
					}

					try {
						System.loadLibrary("NodeIDGetter");
						nodeID = new byte[6];
						getNodeID(nodeID);
					} catch (LinkageError ex) {
						// phooey again.
					}

					nodeID = UUIDRandomness.randomNodeID();
				}
			}
		}
		return nodeID;
	}
}
