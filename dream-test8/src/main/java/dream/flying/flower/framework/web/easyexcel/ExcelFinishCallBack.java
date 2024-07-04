package dream.flying.flower.framework.web.easyexcel;

import java.util.List;

/**
 * excel读取数据完成
 *
 * @author 飞花梦影
 * @date 2023-08-08 09:30:09
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public interface ExcelFinishCallBack<T> {

	/**
	 * 导出后置处理数据 Do after all analysed.
	 *
	 * @param result the result
	 */
	void doAfterAllAnalysed(List<T> result);

	/**
	 * Do save batch.
	 *
	 * @param result the result
	 */
	default void doSaveBatch(List<T> result) {
	}
}