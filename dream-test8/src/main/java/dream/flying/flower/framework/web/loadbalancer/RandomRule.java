package dream.flying.flower.framework.web.loadbalancer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 在现有服务器之间随机分配流量的负载平衡策略
 *
 * @author 飞花梦影
 * @date 2024-06-03 16:35:14
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class RandomRule<S extends BalancerServer> extends AbstractBalancerRule<S> {

	@Override
	public S choose(Object key) {
		return choose(getLoadBalancer(), key);
	}

	/**
	 * Randomly choose from all living servers
	 */
	public S choose(LoadBalancer<S> loadBalancer, Object key) {
		if (loadBalancer == null) {
			return null;
		}
		S server = null;

		while (server == null) {
			if (Thread.interrupted()) {
				return null;
			}
			List<S> upList = loadBalancer.getReachableServers();
			List<S> allList = loadBalancer.getAllServers();

			int serverCount = allList.size();
			if (serverCount == 0) {
				/*
				 * No servers. End regardless of pass, because subsequent passes only get more restrictive.
				 */
				return null;
			}

			int index = chooseRandomInt(serverCount);
			server = upList.get(index);

			if (server == null) {
				/*
				 * The only time this should happen is if the server list were somehow trimmed. This is a transient
				 * condition. Retry after yielding.
				 */
				Thread.yield();
				continue;
			}

			if (server.isAlive()) {
				return server;
			}

			// Shouldn't actually happen.. but must be transient or a bug.
			server = null;
			Thread.yield();
		}

		return server;

	}

	protected int chooseRandomInt(int serverCount) {
		return ThreadLocalRandom.current().nextInt(serverCount);
	}
}