package dream.flying.flower.framework.web.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import dream.flying.flower.ConstDate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * DTO基类,视情况继承
 * 
 * @author 飞花梦影
 * @date 2020-11-23 10:55:25
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Schema(description = "DTO基类,视情况继承")
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	private Long id;

	/**
	 * 创建者
	 */
	private Long createUser;

	/**
	 * 创建时间,格式为yyyy-MM-dd HH:mm:ss
	 */
	@JsonFormat(pattern = ConstDate.DATETIME)
	private Date createTime;

	/**
	 * 更新者
	 */
	private Long updateUser;

	/**
	 * 更新时间,格式为yyyy-MM-dd HH:mm:ss
	 */
	@JsonFormat(pattern = ConstDate.DATETIME)
	private Date updateTime;
}