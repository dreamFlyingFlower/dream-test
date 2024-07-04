package dream.flying.flower.framework.web.enums;

import dream.flying.flower.framework.web.serial.LongToStringSerializer;

/**
 * Long类型序列化方式,见 {@link LongToStringSerializer}
 *
 * @author 飞花梦影
 * @date 2022-12-09 17:51:25
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public enum SerializeLong {

	/** 默认,超过界面的序列化为String */
	DEFAULT,
	/** 保持原样,不做处理 */
	NOT,
	/** 总是序列化为String */
	ALWAYS
}