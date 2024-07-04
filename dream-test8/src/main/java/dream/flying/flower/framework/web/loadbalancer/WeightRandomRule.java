package dream.flying.flower.framework.web.loadbalancer;

import java.util.List;

/**
 * 随机权重算法 FIXME
 *
 * @author 飞花梦影
 * @date 2024-06-03 16:42:32
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class WeightRandomRule extends RandomRule<WeightBalancerServer> {

	// 初始化随机数生产器
	static java.util.Random random = new java.util.Random();

	@Override
	public WeightBalancerServer choose(Object key) {
		return chooseWeight(getLoadBalancer(), key);
	}

	public WeightBalancerServer chooseWeight(LoadBalancer<WeightBalancerServer> loadBalancer, Object key) {
		if (loadBalancer == null) {
			return null;
		}
		// 计算总权重值
		int weightTotal = 0;

		List<WeightBalancerServer> allBalancerServers = loadBalancer.getAllServers();
		int serverCount = allBalancerServers.size();
		if (serverCount == 0) {
			return null;
		}

		List<WeightBalancerServer> reachableServers = loadBalancer.getReachableServers();

		for (BalancerServer balancerServer : reachableServers) {
			if (balancerServer instanceof WeightBalancerServer) {
				WeightBalancerServer weightBalancerServer = (WeightBalancerServer) balancerServer;
				weightTotal += weightBalancerServer.getWeight();
			}
		}

		int index = chooseRandomInt(weightTotal);

		return chooseRandomServer(allBalancerServers, index);
	}

	protected WeightBalancerServer chooseRandomServer(List<WeightBalancerServer> allBalancerServers, int index) {
		for (WeightBalancerServer allBalancerServer : allBalancerServers) {
			WeightBalancerServer weightBalancerServer = (WeightBalancerServer) allBalancerServer;
			// 获取每个节点的权重值
			Integer weight = weightBalancerServer.getWeight();
			// 如果权重值大于产生的随机数,则代表此次随机分配应该落入该节点
			if (weight > index) {
				// 直接返回对应的节点去处理本次请求并终止循环
				return allBalancerServer;
			}
			// 如果当前节点的权重值小于随机索引,则用随机索引减去当前节点的权重值,
			// 继续循环权重列表,与其他的权重值进行对比,
			// 最终该请求总会落入到某个IP的权重值范围内
			index = index - weight;
		}
		return null;
	}
}