package dream.flying.flower.framework.web.entity.node;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 基础节点类
 *
 * @author 飞花梦影
 * @date 2022-11-12 15:30:41
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AbstractPathNode<ID extends Serializable, T> extends AbstractNode<ID, T> {

	private static final long serialVersionUID = -8691190695474401332L;

	private String treePath;
}