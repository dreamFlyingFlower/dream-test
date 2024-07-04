package dream.flying.flower.framework.web.query;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import dream.flying.flower.ConstDate;
import dream.flying.flower.framework.web.entity.AbstractPager;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 基础查询参数
 *
 * @author 飞花梦影
 * @date 2022-05-15 16:14:31
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractQuery extends AbstractPager {

	private static final long serialVersionUID = 5878601031846402790L;

	/**
	 * 主键ID
	 */
	@Schema(description = "主键ID")
	private Serializable id;

	/**
	 * 主键ID列表
	 */
	@Schema(description = "主键ID列表")
	@JsonProperty(access = Access.WRITE_ONLY)
	private List<Serializable> ids;

	/**
	 * 查询日期的开始时间
	 */
	@Schema(description = "查询日期的开始时间")
	@JsonProperty(access = Access.WRITE_ONLY)
	@JsonFormat(pattern = ConstDate.DATE)
	private String beignDate;

	/**
	 * 查询日期的结束时间
	 */
	@Schema(description = "查询日期的结束时间")
	@JsonProperty(access = Access.WRITE_ONLY)
	@JsonFormat(pattern = ConstDate.DATE)
	private String endDate;

	/**
	 * 查询创建日期的开始时间
	 */
	@Schema(description = "查询创建日期的开始时间")
	@JsonProperty(access = Access.WRITE_ONLY)
	@JsonFormat(pattern = ConstDate.DATETIME)
	private String beginCreateTime;

	/**
	 * 查询创建日期的结束时间
	 */
	@Schema(description = "查询创建日期的结束时间")
	@JsonProperty(access = Access.WRITE_ONLY)
	@JsonFormat(pattern = ConstDate.DATETIME)
	private String endCreateTime;

	/**
	 * 查询更新时间的开始时间
	 */
	@Schema(description = "查询更新时间的开始时间")
	@JsonProperty(access = Access.WRITE_ONLY)
	@JsonFormat(pattern = ConstDate.DATETIME)
	private String beginUpdateTime;

	/**
	 * 查询更新时间的结束时间
	 */
	@Schema(description = "查询更新时间的结束时间")
	@JsonProperty(access = Access.WRITE_ONLY)
	@JsonFormat(pattern = ConstDate.DATETIME)
	private String endUpdateTime;

	/**
	 * 创建人ID
	 */
	@Schema(description = "创建人ID")
	private Long createUser;

	/**
	 * 最近一次更新用户ID
	 */
	@Schema(description = "最近一次更新用户ID")
	private Long updateUser;
}