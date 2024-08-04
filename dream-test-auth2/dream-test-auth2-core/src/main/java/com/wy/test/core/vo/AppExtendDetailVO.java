package com.wy.test.core.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 扩展应用表
 *
 * @author 飞花梦影
 * @date 2024-08-04 20:25:49
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@Schema(description = "扩展应用表")
public class AppExtendDetailVO extends AppVO {

	private static final long serialVersionUID = -6799943557131140977L;

}