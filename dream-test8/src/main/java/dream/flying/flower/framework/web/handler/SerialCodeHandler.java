package dream.flying.flower.framework.web.handler;

import java.util.List;

/**
 * 序列化编码
 *
 * @author 飞花梦影
 * @date 2024-03-29 22:30:22
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface SerialCodeHandler {

	int DEFAULT_LENGTH = 6;

	int DEFAULT_BATCH_NUM = 10;

	/**
	 * 生成序列号,实现类需自行指定默认长度
	 * 
	 * @param prefix 业务编码前缀
	 * @return 序列编码
	 */
	default String obtainCode(String prefix) {
		return obtainCode(prefix, DEFAULT_LENGTH);
	}

	/**
	 * 生成序列号
	 * 
	 * @param prefix 业务编码前缀
	 * @param length 编码长度,不包含prefix
	 * @return 序列编码
	 */
	String obtainCode(String prefix, int length);

	/**
	 * 带当前日期的序列号,实现类需自行指定默认长度
	 * 
	 * @param prefix 业务编码前缀
	 * @return 序列编码
	 */
	default String obtainCodeWithDate(String prefix) {
		return obtainCodeWithDate(prefix, DEFAULT_LENGTH);
	}

	/**
	 * 带当前日期的序列号
	 * 
	 * @param prefix 业务编码前缀
	 * @param length 编码长度,不包含前缀
	 * @return 序列编码
	 */
	String obtainCodeWithDate(String prefix, int length);

	/**
	 * 批量获得序列号,实现类需自行指定默认数量
	 * 
	 * @param redisKey redis key
	 * @param prefix 业务编码前缀
	 * @return 格式如prefix20230529000001
	 */
	default List<String> obtainCodesWithDate(String prefix) {
		return obtainCodesWithDate(prefix, DEFAULT_LENGTH);
	}

	/**
	 * 批量获得序列号,实现类需自行指定默认数量
	 * 
	 * @param prefix 业务编码前缀
	 * @param length 编码长度,不包含前缀
	 * @return 格式如prefix20230529000001
	 */
	default List<String> obtainCodesWithDate(String prefix, int length) {
		return obtainCodesWithDate(prefix, length, DEFAULT_BATCH_NUM);
	}

	/**
	 * 批量获得序列号
	 * 
	 * @param prefix 业务编码前缀
	 * @param length 编码长度,不包含前缀
	 * @param delta 批量数
	 * @return 格式如prefix20230529000001
	 */
	List<String> obtainCodesWithDate(String prefix, int length, long delta);
}