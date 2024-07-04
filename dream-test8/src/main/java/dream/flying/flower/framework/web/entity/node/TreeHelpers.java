package dream.flying.flower.framework.web.entity.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;

import dream.flying.flower.framework.core.constant.ConstCore;
import lombok.extern.slf4j.Slf4j;

/**
 * 树形结构数据,若需要额外的参数,可继承本类
 * 
 * @author 飞花梦影
 * @date 2022-09-05 14:11:48
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Slf4j
class TreeHelpers<T> {

	private static final List<Serializable> IDS = Collections.singletonList(ConstCore.ROOT_ID);

	public static <T extends AbstractPathNode<Serializable, T>> List<T> buildTree(List<T> dataList) {
		return buildTree(dataList, IDS, (data) -> data, (item) -> true);
	}

	public static <T extends AbstractPathNode<Serializable, T>> List<T> buildTree(List<T> dataList,
			Function<T, T> map) {
		return buildTree(dataList, IDS, map, (item) -> true);
	}

	public static <T extends AbstractPathNode<Serializable, T>> List<T> buildTree(List<T> dataList, Function<T, T> map,
			Predicate<T> filter) {
		return buildTree(dataList, IDS, map, filter);
	}

	public static <T extends AbstractPathNode<Serializable, T>> List<T> buildTree(List<T> dataList,
			List<Serializable> ids) {
		return buildTree(dataList, ids, (data) -> data, (item) -> true);
	}

	public static <T extends AbstractPathNode<Serializable, T>> List<T> buildTree(List<T> dataList,
			List<Serializable> ids, Function<T, T> map) {
		return buildTree(dataList, ids, map, (item) -> true);
	}

	/**
	 * 数据集合构建成树形结构 （ 注: 如果最开始的 ids 不在 dataList 中,不会进行任何处理 ）
	 *
	 * @param datas 数据集合
	 * @param ids 父元素的 Id 集合
	 * @param map 调用者提供 Function<T, T> 由调用着决定数据最终呈现形势
	 * @param filter 调用者提供 Predicate<T> false 表示过滤 （ 注: 如果将父元素过滤掉等于剪枝 ）
	 * @param <T> extends ITreeNode
	 * @return
	 */
	public static <T extends AbstractPathNode<Serializable, T>> List<T> buildTree(List<T> datas, List<Serializable> ids,
			Function<T, T> map, Predicate<T> filter) {
		if (CollectionUtils.isEmpty(ids)) {
			return new ArrayList<>();
		}
		// 将数据分为 父子结构
		Map<Boolean, List<T>> parentChildrenPartition = datas.stream().filter(filter)
				.collect(Collectors.partitioningBy(item -> ids.contains(item.getParentId())));

		List<T> parent = parentChildrenPartition.getOrDefault(true, Collections.emptyList());
		List<T> children = parentChildrenPartition.getOrDefault(false, Collections.emptyList());
		// 如果未分出或过滤了父元素则将子元素返回
		if (parent.size() == 0) {
			return children;
		}
		// 使用有序集合存储下一次变量的 ids
		List<Serializable> nextIds = new ArrayList<>(datas.size());
		// 遍历父元素 以及修改父元素内容
		List<T> collectParent = parent.stream().map(map).collect(Collectors.toList());
		for (T parentItem : collectParent) {
			// 如果子元素已经加完,直接进入下一轮循环
			if (nextIds.size() == children.size()) {
				break;
			}
			// 过滤出 parent.id == children.parentId 的元素
			children.stream().filter(childrenItem -> parentItem.getId().equals(childrenItem.getParentId()))
					.forEach(childrenItem -> {
						// 这次的子元素为下一次的父元素
						nextIds.add(childrenItem.getParentId());
						// 添加子元素到 parentItem.children 中
						try {
							parentItem.getChildren().add(childrenItem);
						} catch (Exception e) {
							log.warn("TreeNodeUtil 发生错误, 传入参数中 children 不能为 null,解决方法: \n"
									+ "方法一、在map（推荐）或filter中初始化 \n" + "方法二、List<T> children = new ArrayList<>() \n"
									+ "方法三、初始化块对属性赋初值\n" + "方法四、构造时对属性赋初值");
						}
					});
		}
		buildTree(children, nextIds, map, filter);
		return parent;
	}

	/**
	 * 生成路径 treePath 路径
	 *
	 * @param currentId 当前元素的 id
	 * @param getById 用户返回一个 T
	 * @param <T>
	 * @return
	 */
	public static <T extends BasePathNode<T, Serializable>> String generateTreePath(Serializable currentId,
			Function<Serializable, T> getById) {
		StringBuffer treePath = new StringBuffer();
		if (ConstCore.ROOT_ID.equals(currentId)) {
			// 如果当前节点是父节点直接返回
			treePath.append(currentId);
		} else {
			// 调用者将当前元素的父元素查出来,方便后续拼接
			T byId = getById.apply(currentId);
			// 父元素的 treePath + "," + 父元素的id
			if (!ObjectUtils.isEmpty(byId)) {
				treePath.append(byId.getTreePath()).append(",").append(byId.getId());
			}
		}
		return treePath.toString();
	}
}