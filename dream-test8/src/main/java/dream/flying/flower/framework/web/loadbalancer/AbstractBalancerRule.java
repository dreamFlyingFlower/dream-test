package dream.flying.flower.framework.web.loadbalancer;

/**
 * 提供用于设置和获取负载均衡器的默认实现的类
 *
 * @author 飞花梦影
 * @date 2024-06-03 16:30:57
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public abstract class AbstractBalancerRule<S extends BalancerServer> implements BalancerRule<S> {

	private LoadBalancer<S> loadBalancer;

	@Override
	public void setLoadBalancer(LoadBalancer<S> loadBalancer) {
		this.loadBalancer = loadBalancer;
	}

	@Override
	public LoadBalancer<S> getLoadBalancer() {
		return loadBalancer;
	}
}