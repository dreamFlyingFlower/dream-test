package dream.flying.flower.framework.web.entity.node;

import java.io.Serializable;

/**
 * 基础节点接口
 * 
 * @author 飞花梦影
 * @date 2022-11-12 15:29:46
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface BaseNode<ID extends Serializable, T> extends Serializable {

	/**
	 * 当前节点标识
	 * 
	 * @return 当前节点标识
	 */
	ID getId();

	/**
	 * 上级节点标识
	 * 
	 * @return 上级节点标识
	 */
	ID getParentId();
}