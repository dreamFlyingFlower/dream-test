package com.wy.test.core.vo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fhs.core.trans.vo.TransPojo;

import dream.flying.flower.ConstDate;
import dream.flying.flower.framework.web.valid.ValidAdd;
import dream.flying.flower.framework.web.valid.ValidEdit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 记住我
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "记住我")
public class RememberMeVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private Long id;

	@Schema(description = "用户ID")
	@Size(max = 32, message = "用户ID最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String userId;

	@Schema(description = "用户名")
	@Size(max = 32, message = "用户名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String username;

	@Schema(description = "上次登录时间")
	@JsonFormat(pattern = ConstDate.DATETIME)
	private Date lastLoginTime;

	@Schema(description = "过期时间")
	@JsonFormat(pattern = ConstDate.DATETIME)
	private Date expirationTime;
}