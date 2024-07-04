package dream.flying.flower.framework.web.entity.node;

import java.io.Serializable;

/**
 * 基础节点接口,应对数据库中的treePath字段
 * 
 * @author 飞花梦影
 * @date 2022-11-12 15:29:46
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public interface BasePathNode<ID extends Serializable, T> extends BaseNode<ID, T> {

	/**
	 * 如果数据库设计有tree_path字段可覆盖此方法来生成tree_path路径
	 *
	 * @return 获取树路径
	 */
	default String getTreePath() {
		return null;
	}
}