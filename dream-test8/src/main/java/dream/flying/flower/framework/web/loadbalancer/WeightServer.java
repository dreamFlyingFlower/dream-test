package dream.flying.flower.framework.web.loadbalancer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
public abstract class WeightServer {

	// 节点信息
	private WeightBalancerServer server;

	// 节点权重值
	private Integer weight;

	// 动态权重值
	private Integer currentWeight;
}