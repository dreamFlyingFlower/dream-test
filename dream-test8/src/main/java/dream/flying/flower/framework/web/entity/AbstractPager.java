package dream.flying.flower.framework.web.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fhs.core.trans.vo.TransPojo;

import dream.flying.flower.ConstPager;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 分页参数,所有实体类和分页参数类都可继承该类
 * 
 * @author 飞花梦影
 * @date 2020-11-23 10:54:02
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Schema(description = "分页参数,所有实体类和分页参数类都可继承该类")
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractPager implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	/**
	 * 分页,从第几页开始查询
	 */
	@Schema(description = "分页,从第几页开始查询")
	@JsonProperty(access = Access.WRITE_ONLY)
	private Integer pageIndex;

	/**
	 * 分页,每页查询数据条数
	 */
	@Schema(description = "分页,每页查询数据条数")
	@JsonProperty(access = Access.WRITE_ONLY)
	private Integer pageSize;

	/**
	 * 排序字段,多个用逗号隔开
	 */
	@Schema(description = "排序字段,多个用逗号隔开")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String order;

	/**
	 * 升序或降序,升级asc,降序desc
	 */
	@Schema(description = "升序或降序,升级asc,降序desc")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String sortType;

	/**
	 * 是否分页,当pageIndex不存在或小于0时,不分页;默认每页显示10条数据
	 * 
	 * @return true分页,false不分页
	 */
	public boolean hasPager() {
		if (pageIndex == null || pageIndex <= 0) {
			return false;
		}
		if (pageSize == null || pageSize <= 0) {
			pageSize = ConstPager.PAGE_SIZE_NUM;
		}
		return true;
	}
}