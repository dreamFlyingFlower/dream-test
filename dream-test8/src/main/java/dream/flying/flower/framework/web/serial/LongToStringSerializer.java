package dream.flying.flower.framework.web.serial;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 根据Long类型大小进行序列化
 *
 * @author 飞花梦影
 * @date 2022-12-09 17:44:54
 */
public class LongToStringSerializer extends JsonSerializer<Long> {

	private static final long LIMIT = 1L << 53;

	@Override
	public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		if (value > LIMIT - 1) {
			gen.writeString(value.toString());
		} else {
			gen.writeNumber(value);
		}
	}
}