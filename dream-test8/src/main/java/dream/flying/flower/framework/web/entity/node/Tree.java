package dream.flying.flower.framework.web.entity.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import dream.flying.flower.collection.ListHelper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 树形结构数据,若需要额外的参数,可继承本类
 * 
 * @author 飞花梦影
 * @date 2022-09-05 14:11:48
 * @git {@link https://github.com/dreamFlyingFlower }
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Tree<T> extends AbstractNode<Long, T> {

	private static final long serialVersionUID = 4859821462706409650L;

	/**
	 * 树形结构中的扩展数据
	 */
	private Map<String, Object> extra;

	/**
	 * 将列表转成树形结构,但是
	 * 
	 * @param <T>
	 * @param datas
	 * @return
	 */
	public static <T extends Tree<T>> List<Tree<T>> trans2Tree(List<T> datas) {
		if (ListHelper.isEmpty(datas)) {
			return new ArrayList<>();
		}
		Map<Long, Tree<T>> map = datas.stream().collect(Collectors.toMap(k -> k.getId(), v -> v));
		List<Tree<T>> rets = new ArrayList<>();
		datas.forEach(t -> {
			Tree<T> parentTree = map.get(t.getParentId());
			if (Objects.isNull(parentTree)) {
				rets.add(parentTree);
			} else {
				List<T> children = parentTree.getChildren();
				if (ListHelper.isEmpty(children)) {
					children = new ArrayList<T>();
					parentTree.setChildren(children);
				}
				children.add(t);
			}
		});
		return rets;
	}
}