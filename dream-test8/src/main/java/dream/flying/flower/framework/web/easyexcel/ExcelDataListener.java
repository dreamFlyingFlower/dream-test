package dream.flying.flower.framework.web.easyexcel;

import java.util.LinkedList;
import java.util.List;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

/**
 * excel读取监听器
 *
 * @author 飞花梦影
 * @date 2023-08-08 09:29:24
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public class ExcelDataListener<T> extends AnalysisEventListener<T> {

	/**
	 * 定义一个保存Excel所有记录的集合
	 */
	private final List<T> list = new LinkedList<>();

	/**
	 * 回调接口
	 */
	private final ExcelFinishCallBack<T> callBack;

	/**
	 * 构造 ExcelFinishCallBack
	 *
	 * @param callBack ExcelFinishCallBack
	 */
	public ExcelDataListener(ExcelFinishCallBack<T> callBack) {
		this.callBack = callBack;
	}

	/**
	 * 这个每一条数据解析都会来调用,在这里可以做一些其他的操作(过滤,分批入库..)
	 *
	 * @param data one row value. is same as {@link AnalysisContext#readRowHolder()}
	 * @param context context
	 */
	@Override
	public void invoke(T data, AnalysisContext context) {
		list.add(data);
		if (list.size() == 500) {
			System.out.println(("自己逻辑..."));
			this.callBack.doSaveBatch(list);
			list.clear();
		}
	}

	/**
	 * 所有数据解析完成了都会来调用,解析完成之后将所有数据存入回调接口中
	 *
	 * @param context context
	 */
	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		this.callBack.doAfterAllAnalysed(this.list);
	}
}