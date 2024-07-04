package dream.flying.flower.framework.web.entity.node;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import dream.flying.flower.collection.ListHelper;
import dream.flying.flower.framework.web.valid.ValidEdit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
public abstract class AbstractNode<ID extends Serializable, T> implements BaseNode<ID, T> {

	private static final long serialVersionUID = -8691190695474401332L;

	/**
	 * 当前节点唯一标识
	 */
	@Schema(description = "当前节点唯一标识")
	@NotNull(groups = ValidEdit.class)
	@JsonSerialize(using = ToStringSerializer.class)
	protected ID id;

	/**
	 * 当前节点上级标识
	 */
	@Schema(description = "上级组织机构,顶层组织机构为0")
	@NotNull(message = "上级ID不能为空")
	@JsonSerialize(using = ToStringSerializer.class)
	protected ID parentId;

	/**
	 * 节点需要展示的内容
	 */
	@Schema(description = "节点需要展示的内容")
	protected String label;

	/**
	 * 下级数据列表
	 */
	@Schema(description = "下级数据列表")
	@JsonInclude(Include.NON_EMPTY)
	protected List<T> children;

	/**
	 * 是否有子节点
	 * 
	 * @return true->有,false->无
	 */
	protected Boolean hasChildren() {
		return ListHelper.isEmpty(children);
	}

	/**
	 * 树形结构中的扩展数据
	 * 
	 * @return Map<String, Object>
	 */
	protected Map<String, Object> getExtra() {
		return null;
	};
}