package dream.flying.flower.framework.web.loadbalancer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * 轮询权重算法 FIXME
 *
 * @author 飞花梦影
 * @date 2024-06-03 16:42:32
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public class WeightRoundRobinRule extends AbstractBalancerRule<WeightBalancerServer> {

	// 初始化存储每个节点的权重容器
	private static Map<String, WeightServer> weightMap = new HashMap<>();

	// 计算总权重值,只需要计算一次,因此放在静态代码块中执行
	private static int weightTotal = 0;

	public WeightRoundRobinRule(LoadBalancer<WeightBalancerServer> lb) {
		setLoadBalancer(lb);
		sumWeightTotal();
	}

	// 求和总权重值,后续动态伸缩节点时,再次调用该方法即可
	public void sumWeightTotal() {
		List<WeightBalancerServer> allServers = getLoadBalancer().getAllServers();
		for (WeightBalancerServer balancerServer : allServers) {
			weightTotal += balancerServer.getWeight();
		}
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

		// 每次请求时，更改动态权重值
		for (WeightServer weight : weightMap.values()) {
			weight.setCurrentWeight(weight.getCurrentWeight() + weight.getWeight());
		}

		// 判断权重容器中最大的动态权重值
		WeightServer maxCurrentWeight = null;
		for (WeightServer weight : weightMap.values()) {
			if (maxCurrentWeight == null || weight.getCurrentWeight() > maxCurrentWeight.getCurrentWeight()) {
				maxCurrentWeight = weight;
			}
		}

		// 最后用最大的动态权重值减去所有节点的总权重值
		maxCurrentWeight.setCurrentWeight(maxCurrentWeight.getCurrentWeight() - weightTotal);

		// 返回最大的动态权重值对应的节点IP
		return maxCurrentWeight.getServer();
	}
}