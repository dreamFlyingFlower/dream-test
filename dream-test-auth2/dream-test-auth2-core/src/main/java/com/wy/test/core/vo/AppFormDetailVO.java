package com.wy.test.core.vo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fhs.core.trans.vo.TransPojo;

import dream.flying.flower.framework.web.valid.ValidAdd;
import dream.flying.flower.framework.web.valid.ValidEdit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 表单信息
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "表单信息")
public class AppFormDetailVO extends AppVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "用户名")
	@Size(max = 32, message = "用户名最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String usernameMapping;

	@Schema(description = "密码")
	@Size(max = 128, message = "密码最大长度不能超过128", groups = { ValidAdd.class, ValidEdit.class })
	private String passwordMapping;

	@Schema(description = "重定向uri")
	@Size(max = 256, message = "重定向uri最大长度不能超过256", groups = { ValidAdd.class, ValidEdit.class })
	private String redirectUri;

	@Schema(description = "本地授权视图")
	@Size(max = 100, message = "本地授权视图最大长度不能超过100", groups = { ValidAdd.class, ValidEdit.class })
	private String authorizeView;

	@Schema(description = "密码加密算法")
	@Size(max = 32, message = "密码加密算法最大长度不能超过32", groups = { ValidAdd.class, ValidEdit.class })
	private String passwordAlgorithm;

	@Schema(description = "机构ID")
	@NotNull(message = "机构ID不能为空", groups = { ValidAdd.class })
	private String instId;

	@Schema(description = "机构名称")
	private String instName;
}