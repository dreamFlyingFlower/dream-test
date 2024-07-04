package dream.flying.flower.framework.web.service;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import dream.flying.flower.framework.web.query.AbstractQuery;
import dream.flying.flower.result.Result;

/**
 * 通用业务接口
 * 
 * @auther 飞花梦影
 * @date 2021-05-04 20:40:10
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface BaseService<T, V, Q extends AbstractQuery> {

	/**
	 * 数据新增,只会插入实体对象中的非空值
	 * 
	 * @param v 需要新增的数据
	 * @return 新增结果.默认返回boolean,true->新增成功,false->新增失败
	 */
	Object add(V v);

	/**
	 * 批量数据新增,只会插入实体对象中的非空值
	 * 
	 * @param vs 需要新增的数据列表
	 * @return 新增结果.默认返回boolean,true->新增成功,false->新增失败
	 */
	Object adds(List<V> vs);

	/**
	 * 检查数据是否重复
	 * 
	 * @param v 待检查的对象数据
	 * @return true->重复;false->不重复
	 */
	Boolean checkExist(V v);

	/**
	 * 根据实体类中的非空参数进行相等判断删除数据
	 * 
	 * @param v 实体数据
	 * @return 删除结果.默认返回boolean,true->删除成功,false->删除失败
	 */
	Boolean delete(V v);

	/**
	 * 根据主键删除单条数据
	 * 
	 * @param id 主键编号
	 * @return 删除结果.默认返回boolean,true->删除成功,false->删除失败
	 */
	Boolean delete(Serializable id);

	/**
	 * 根据主键删除多条数据
	 * 
	 * @param ids 主键编号列表
	 * @return 删除结果.默认返回boolean,true->删除成功,false->删除失败
	 */
	Boolean deletes(List<Serializable> ids);

	/**
	 * 数据修改,只会修改实体对象中的非空值
	 * 
	 * @param v 需要修改的数据
	 * @return 修改结果.默认返回boolean,true->修改成功,false->修改失败
	 */
	Boolean edit(V v);

	/**
	 * 批量数据修改,只会修改实体对象中的非空值
	 * 
	 * @param vs 需要修改的数据列表
	 * @return 修改结果.默认返回boolean,true->修改成功,false->修改失败
	 */
	Boolean edits(List<V> vs);

	/**
	 * 根据主键编号获得数据详情
	 * 
	 * @param id 主键编号
	 * @return 详情.默认返回实体类对象
	 */
	V getInfo(Serializable id);

	/**
	 * 根据主键列表编号获得数据详情
	 * 
	 * @param ids 主编编号列表
	 * @return 详情列表.默认返回实体类对象列表
	 */
	List<V> getInfos(List<Serializable> ids);

	/**
	 * 从缓存中获得值
	 * 
	 * @param key 缓存key
	 * @return 缓存中ID->D映射关系
	 */
	default Map<Long, V> getCache(String key) {
		return Collections.emptyMap();
	}

	/**
	 * 获得单条数据,该方法只适合数据库中设置唯一属性的字段
	 * 
	 * @param t 实体
	 * @return 实体数据
	 */
	T getOne(T t);

	/**
	 * 根据实体类中的非空参数以相等条件查询列表的第一条数据
	 * 
	 * @param t 实体对象
	 * @return 列表
	 */
	T listOne(T t);

	/**
	 * 根据实体类中的所有参数查询是否有重复值
	 * 
	 * @param q 实体类查询对象
	 * @return true->有重复值;false->无重复值
	 */
	boolean hasValue(Q q);

	/**
	 * 分页/不分页查询.根据实体类对象参数中的非空值进行相等查询
	 * 
	 * @param q 实体类查询参数
	 * @return 数据集合
	 */
	Result<List<V>> listPage(Q q);
}