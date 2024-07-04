package dream.flying.flower.framework.web.loadbalancer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

/**
 * 轮询权重算法 FIXME
 *
 * @author 飞花梦影
 * @date 2024-06-03 16:42:32
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public class WeightSimpleRoundRobinRule extends AbstractBalancerRule<WeightBalancerServer> {

	private AtomicInteger nextServerCyclicCounter;

	public WeightSimpleRoundRobinRule() {
		nextServerCyclicCounter = new AtomicInteger(0);
	}

	public WeightSimpleRoundRobinRule(LoadBalancer<WeightBalancerServer> lb) {
		this();
		setLoadBalancer(lb);
	}

	@Override
	public WeightBalancerServer choose(Object key) {
		return choose(getLoadBalancer(), key);
	}

	public WeightBalancerServer choose(LoadBalancer<WeightBalancerServer> loadBalancer, Object key) {
		if (loadBalancer == null) {
			log.warn("no load balancer");
			return null;
		}

		WeightBalancerServer server = null;
		int count = 0;
		int weightTotal = 0;
		while (server == null && count++ < 10) {
			List<WeightBalancerServer> reachableServers = loadBalancer.getReachableServers();
			List<WeightBalancerServer> allServers = loadBalancer.getAllServers();
			int upCount = reachableServers.size();
			int serverCount = allServers.size();

			if ((upCount == 0) || (serverCount == 0)) {
				log.warn("No up servers available from load balancer: " + loadBalancer);
				return null;
			}

			for (WeightBalancerServer weightBalancerServer : reachableServers) {
				weightTotal += weightBalancerServer.getWeight();
			}

			int nextServerIndex = incrementAndGetModulo(serverCount, weightTotal);
			server = allServers.get(nextServerIndex);

			if (server == null) {
				/* Transient. */
				Thread.yield();
				continue;
			}

			if (server.isAlive() && (server.isReadyToServe())) {
				return server;
			}

			// Next.
			server = null;
		}

		if (count >= 10) {
			log.warn("No available alive servers after 10 tries from load balancer: " + loadBalancer);
		}
		return server;
	}

	/**
	 * Inspired by the implementation of {@link AtomicInteger#incrementAndGet()}.
	 *
	 * @param modulo The modulo to bound the value of the counter.
	 * @return The next value.
	 */
	private int incrementAndGetModulo(int modulo, int weightTotal) {
		for (;;) {
			int current = nextServerCyclicCounter.get();
			int next = (current + 1) % modulo;
			if (nextServerCyclicCounter.compareAndSet(current, next))
				return next;
		}
	}
}