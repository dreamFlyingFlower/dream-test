package dream.flying.flower.framework.web.loadbalancer;

import java.util.List;

/**
 * 定义软件负载平衡器操作的接口.典型的负载均衡器至少需要一组服务器进行负载平衡,一种将特定服务器标记为不轮换的方法,以及一个从现有服务器列表中选择服务器的调用
 *
 * @author 飞花梦影
 * @date 2024-06-03 16:28:35
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface LoadBalancer<S extends BalancerServer> {

	/**
	 * Initial list of servers. This API also serves to add additional ones at a later time The same logical server
	 * (host:port) could essentially be added multiple times (helpful in cases where you want to give more "weightage"
	 * perhaps ..)
	 * 
	 * @param newServers new servers to add
	 */
	void addServers(List<S> newServers);

	/**
	 * Choose a server from load balancer.
	 * 
	 * @param key An object that the load balancer may use to determine which server to return. null if the load
	 *        balancer does not use this parameter.
	 * @return server chosen
	 */
	S chooseServer(Object key);

	/**
	 * To be called by the clients of the load balancer to notify that a Server is down else, the LB will think its
	 * still Alive until the next Ping cycle - potentially (assuming that the LB Impl does a ping)
	 * 
	 * @param server Server to mark as down
	 */
	void markServerDown(S server);

	/**
	 * @return Only the servers that are up and reachable.
	 */
	List<S> getReachableServers();

	/**
	 * @return All known servers, both reachable and unreachable.
	 */
	List<S> getAllServers();
}