package dream.flying.flower.framework.web.loadbalancer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

/**
 * 最著名和最基本的负载均衡策略,即循环规则
 *
 * @author 飞花梦影
 * @date 2024-06-03 16:32:13
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public class SimpleRoundRobinRule extends AbstractBalancerRule<BalancerServer> {

	private AtomicInteger nextServerCyclicCounter;

	public SimpleRoundRobinRule() {
		nextServerCyclicCounter = new AtomicInteger(0);
	}

	public SimpleRoundRobinRule(LoadBalancer<BalancerServer> lb) {
		this();
		setLoadBalancer(lb);
	}

	@Override
	public BalancerServer choose(Object key) {
		return choose(getLoadBalancer(), key);
	}

	public BalancerServer choose(LoadBalancer<BalancerServer> lb, Object key) {
		if (lb == null) {
			log.warn("no load balancer");
			return null;
		}

		BalancerServer server = null;
		int count = 0;
		while (server == null && count++ < 10) {
			List<BalancerServer> reachableServers = lb.getReachableServers();
			List<BalancerServer> allServers = lb.getAllServers();
			int upCount = reachableServers.size();
			int serverCount = allServers.size();

			if ((upCount == 0) || (serverCount == 0)) {
				log.warn("No up servers available from load balancer: " + lb);
				return null;
			}

			int nextServerIndex = incrementAndGetModulo(serverCount);
			server = allServers.get(nextServerIndex);

			if (server == null) {
				/* Transient. */
				Thread.yield();
				continue;
			}

			if (server.isAlive() && (server.isReadyToServe())) {
				return (server);
			}

			// Next.
			server = null;
		}

		if (count >= 10) {
			log.warn("No available alive servers after 10 tries from load balancer: " + lb);
		}
		return server;
	}

	/**
	 * Inspired by the implementation of {@link AtomicInteger#incrementAndGet()}.
	 *
	 * @param modulo The modulo to bound the value of the counter.
	 * @return The next value.
	 */
	private int incrementAndGetModulo(int modulo) {
		for (;;) {
			int current = nextServerCyclicCounter.get();
			int next = (current + 1) % modulo;
			if (nextServerCyclicCounter.compareAndSet(current, next))
				return next;
		}
	}
}