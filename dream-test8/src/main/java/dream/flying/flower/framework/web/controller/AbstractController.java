package dream.flying.flower.framework.web.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import dream.flying.flower.collection.ListHelper;
import dream.flying.flower.enums.TipEnum;
import dream.flying.flower.framework.web.query.AbstractQuery;
import dream.flying.flower.framework.web.service.BaseService;
import dream.flying.flower.framework.web.valid.ValidAdds;
import dream.flying.flower.framework.web.valid.ValidEdits;
import dream.flying.flower.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

/**
 * 通用修改控制层API
 * 
 * @auther 飞花梦影
 * @date 2021-05-04 20:24:29
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public abstract class AbstractController<T, V, Q extends AbstractQuery, S extends BaseService<T, V, Q>>
		extends AbstractQueryController<T, V, Q, S> {

	/**
	 * 数据新增,只会新增非空值
	 * 
	 * @param v 需要新增的数据
	 * @param bindingResult 参数校验结果
	 * @return 新增后的数据
	 */
	@Operation(summary = "新增", description = "数据新增,只会新增非空值")
	@PostMapping("add")
	// @PreAuthorize("hasAuthority('sys:attachment:save')")
	public Result<?> add(@Parameter(description = "需要新增的数据") @Validated(ValidAdds.class) @RequestBody V v,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			FieldError error = bindingResult.getFieldError();
			return error(error.getField() + error.getDefaultMessage());
		}
		return ok(baseService.add(v));
	}

	/**
	 * 批量数据新增,只会新增非空值
	 * 
	 * @param vs 需要新增的数据列表
	 * @param bindingResult 参数校验结果
	 * @return 新增后的数据
	 */
	@Operation(summary = "批量新增", description = "批量数据新增,只会新增非空值")
	@PostMapping("adds")
	public Result<?> adds(@Parameter(description = "需要新增的数据列表") @RequestBody List<V> vs) {
		if (ListHelper.isEmpty(vs)) {
			return error(TipEnum.TIP_PARAM_NOT_NULL);
		}
		return ok(baseService.adds(vs));
	}

	/**
	 * 根据主键删除单条数据
	 * 
	 * @param id 主键编号
	 * @return 影响行数
	 */
	@Operation(summary = "删除", description = "根据主键删除单条数据")
	@DeleteMapping("delete/{id}")
	public Result<?> delete(@Parameter(description = "主键编号") @PathVariable Serializable id) {
		return ok(baseService.delete(id));
	}

	/**
	 * 根据主键批量删除数据
	 * 
	 * @param ids 主键编号列表
	 * @return 影响行数
	 */
	@Operation(summary = "批量删除", description = "根据主键批量删除数据")
	@DeleteMapping("deletes")
	public Result<?> deletes(@Parameter(description = "主键编号列表") @RequestBody List<Serializable> ids) {
		return ok(baseService.deletes(ids));
	}

	/**
	 * 修改,只修改参数中的非空值
	 * 
	 * @param v 需要修改的数据
	 * @param bindingResult 参数校验结果
	 * @return 修改后的结果
	 */
	@Operation(summary = "修改", description = "修改,只修改参数中的非空值")
	@PostMapping("edit")
	public Result<?> edit(@Parameter(description = "需要修改的数据") @Validated(ValidEdits.class) @RequestBody V v,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			FieldError error = bindingResult.getFieldError();
			return error(error.getField() + error.getDefaultMessage());
		}
		return ok(baseService.edit(v));
	}

	/**
	 * 批量修改,只修改参数中的非空值
	 * 
	 * @param vs 需要修改的数据列表
	 * @param bindingResult 参数校验结果
	 * @return 修改后的结果
	 */
	@Operation(summary = "批量修改", description = "批量修改,只修改参数中的非空值")
	@PostMapping("edits")
	public Result<?> edits(@Parameter(description = "需要修改的数据列表") @RequestBody List<V> vs) {
		if (ListHelper.isEmpty(vs)) {
			return error(TipEnum.TIP_PARAM_NOT_NULL);
		}
		return ok(baseService.edits(vs));
	}
}