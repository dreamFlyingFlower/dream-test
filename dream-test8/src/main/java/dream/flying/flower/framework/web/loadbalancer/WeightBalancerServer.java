package dream.flying.flower.framework.web.loadbalancer;

import lombok.Getter;
import lombok.Setter;

/**
 * 表示典型服务器(或可寻址节点)的类,即 Host:port 标识符
 *
 * @author 飞花梦影
 * @date 2024-06-03 16:16:12
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@Setter
public abstract class WeightBalancerServer extends BalancerServer {

	private Integer weight;

	private Integer currentWeight;

	public WeightBalancerServer(String scheme, String host, int port) {
		super(scheme, host, port);
		this.weight = 100;
	}

	public WeightBalancerServer(String scheme, String host, int port, int weight) {
		super(scheme, host, port);
		this.weight = weight;
	}
}