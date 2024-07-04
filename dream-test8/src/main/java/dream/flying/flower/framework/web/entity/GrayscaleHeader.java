package dream.flying.flower.framework.web.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 灰度发布字段
 *
 * @author 飞花梦影
 * @date 2023-08-30 14:16:10
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GrayscaleHeader {

	private String uid;

	private String token;

	private String ip;

	private String tag;

	private String mtest;
}