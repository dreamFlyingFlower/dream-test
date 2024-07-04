package dream.flying.flower.framework.web.loadbalancer;

/**
 * 为负载均衡定义规则的接口,规则可以看作是负载均衡的策略
 *
 * @author 飞花梦影
 * @date 2024-06-03 16:14:29
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface BalancerRule<S extends BalancerServer> {

	/*
	 * choose one alive server from lb.allServers or lb.upServers according to key
	 * 
	 * @return choosen Server object. NULL is returned if none server is available
	 */
	S choose(Object key);

	void setLoadBalancer(LoadBalancer<S> loadBalancer);

	LoadBalancer<S> getLoadBalancer();
}