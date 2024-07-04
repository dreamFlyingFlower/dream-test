package dream.flying.flower.framework.web.controller;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import dream.flying.flower.framework.web.query.AbstractQuery;
import dream.flying.flower.framework.web.service.BaseService;
import dream.flying.flower.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

/**
 * 通用查询控制层API
 * 
 * @auther 飞花梦影
 * @date 2021-05-04 20:24:29
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public abstract class AbstractQueryController<T, V, Q extends AbstractQuery, S extends BaseService<T, V, Q>>
		implements BaseController {

	@Autowired
	protected S baseService;

	/**
	 * 根据主键查找数据
	 * 
	 * @param id 主键编号
	 * @return 详情数据
	 */
	@Operation(summary = "查询详情", description = "根据主键查找数据")
	@GetMapping("getInfo/{id}")
	public Result<?> getInfo(@Parameter(description = "主键") @PathVariable Serializable id) {
		return ok(baseService.getInfo(id));
	}

	/**
	 * 根据主键列表查找数据
	 * 
	 * @param ids 主键编号列表
	 * @return 列表数据
	 */
	@Operation(summary = "批量查询详情", description = "根据主键列表查找数据")
	@GetMapping("getInfos/{ids}")
	public Result<?> getInfos(@Parameter(description = "主键列表") @PathVariable List<Serializable> ids) {
		return ok(baseService.getInfos(ids));
	}

	/**
	 * 根据参数中的非空值做条件查询是否有重复值
	 * 
	 * @param q 实体类对象
	 * @return data值大于0表示有重复值
	 */
	@Operation(summary = "是否重复", description = "根据参数中的非空值做条件查询是否有重复值")
	@PostMapping("hasValue")
	public Result<?> hasValue(@Parameter(description = "实体参数") @RequestBody Q q) {
		return ok(baseService.hasValue(q));
	}

	/**
	 * 根据非空参数为条件查询分页/不分页数据
	 * 
	 * @param q 参数对象
	 * @return 数据集合
	 */
	@Operation(summary = "分页-不分页", description = "根据非空参数为条件查询分页/不分页数据")
	@GetMapping("listPage")
	public Result<List<V>> listPage(@Parameter(description = "查询参数") Q q) {
		return baseService.listPage(q);
	}
}