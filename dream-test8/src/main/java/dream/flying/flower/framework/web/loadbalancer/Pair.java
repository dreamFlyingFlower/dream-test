package dream.flying.flower.framework.web.loadbalancer;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-06-03 17:13:08
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@NoArgsConstructor
@AllArgsConstructor
public class Pair<E1, E2> implements Serializable {

	private static final long serialVersionUID = 2L;

	private E1 mFirst;

	private E2 mSecond;

	/**
	 * Get the first value from the pair.
	 *
	 * @return the first value
	 */
	public E1 first() {
		return mFirst;
	}

	/**
	 * Get the second value from the pair.
	 *
	 * @return the second value
	 */
	public E2 second() {
		return mSecond;
	}

	/**
	 * Set the first value of the pair.
	 *
	 * @param first the new first value
	 */
	public void setFirst(E1 first) {
		mFirst = first;
	}

	/**
	 * Set the second value of the pair.
	 *
	 * @param second the new second value
	 */
	public void setSecond(E2 second) {
		mSecond = second;
	}
}