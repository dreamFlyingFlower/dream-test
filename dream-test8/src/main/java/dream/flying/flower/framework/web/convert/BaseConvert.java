package dream.flying.flower.framework.web.convert;

import java.util.List;

/**
 * 数据层映射类和数据传输类相互转换
 *
 * T->数据库实体类Entity,D->数据传输类DTO/VO
 * 
 * @author 飞花梦影
 * @date 2022-05-15 16:18:09
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public interface BaseConvert<T, D> {

	/**
	 * DTO,VO转Entity
	 *
	 * @param dto 数据传输对象
	 * @return 数据库映射对象
	 */
	T convert(D dto);

	/**
	 * DTO,VO集合转Entity集合
	 *
	 * @param dtos
	 * @return 数据传输对象集合
	 */
	List<T> convert(List<D> dtos);

	/**
	 * Entity转DTO,VO
	 *
	 * @param t 数据库映射对象
	 * @return 数据传输对象
	 */
	D convertt(T t);

	/**
	 * Entity集合转DTO集合
	 *
	 * @param ts 数据库映射对象集合
	 * @return 数据传输对象集合
	 */
	List<D> convertt(List<T> ts);
}