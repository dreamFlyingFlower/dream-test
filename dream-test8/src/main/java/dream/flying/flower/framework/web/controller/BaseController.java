package dream.flying.flower.framework.web.controller;

import java.util.List;
import java.util.Optional;

import dream.flying.flower.common.StatusMsg;
import dream.flying.flower.enums.TipEnum;
import dream.flying.flower.result.Result;

/**
 * 基础控制层
 *
 * @author 飞花梦影
 * @date 2022-05-15 16:00:41
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public interface BaseController {

	default <T> Result<T> ok() {
		return Result.ok();
	}

	default <T> Result<T> ok(T data) {
		return Result.ok(data);
	}

	default <T> Result<T> error() {
		return Result.error();
	}

	default Result<?> error(String msg) {
		return Result.error(Optional.ofNullable(msg).orElse(TipEnum.TIP_FAIL.getMsg()));
	}

	default <T> Result<T> error(TipEnum tipEnum) {
		return Result.error(Optional.ofNullable(tipEnum).orElse(TipEnum.TIP_FAIL));
	}

	default Result<?> page(List<?> datas, Long pageIndex, Long pageSize, Long total) {
		return Result.page(datas, pageIndex, pageSize, total);
	}

	default Result<?> result(Boolean flag) {
		return flag == null || !flag ? error() : ok();
	}

	default Result<?> result(StatusMsg statusMsg) {
		return Result.result(Optional.ofNullable(statusMsg).orElse(TipEnum.TIP_SUCCESS));
	}

	default Result<?> result(TipEnum tipEnum) {
		return Result.result(Optional.ofNullable(tipEnum).orElse(TipEnum.TIP_SUCCESS));
	}
}