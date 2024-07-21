package com.wy.test.common.crypto.jose;

import com.google.common.base.Strings;
import com.nimbusds.jose.JWSAlgorithm;

/**
 * Wrapper class for Nimbus JOSE objects to fit into JPA
 *
 * @author 飞花梦影
 * @date 2024-07-14 21:33:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class JWSAlgorithmEmbed {

	public static final JWSAlgorithmEmbed NONE = getForAlgorithmName("none");

	private JWSAlgorithm algorithm;

	public JWSAlgorithmEmbed() {

	}

	public JWSAlgorithmEmbed(JWSAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * 
	 * @param algorithmName
	 * @return null if algorithmName is empty or null
	 */
	public static JWSAlgorithmEmbed getForAlgorithmName(String algorithmName) {
		JWSAlgorithmEmbed ent = new JWSAlgorithmEmbed();
		ent.setAlgorithmName(algorithmName);
		if (ent.getAlgorithm() == null) {
			return null;
		} else {
			return ent;
		}
	}

	/**
	 * Get the name of this algorithm, return null if no algorithm set.
	 * 
	 * @return
	 */

	public String getAlgorithmName() {
		if (algorithm != null) {
			return algorithm.getName();
		} else {
			return null;
		}
	}

	/**
	 * Set the name of this algorithm. Calls JWSAlgorithm.parse()
	 * 
	 * @param algorithmName
	 */
	public void setAlgorithmName(String algorithmName) {
		if (!Strings.isNullOrEmpty(algorithmName)) {
			algorithm = JWSAlgorithm.parse(algorithmName);
		} else {
			algorithm = null;
		}
	}

	/**
	 * @return the algorithm
	 */

	public JWSAlgorithm getAlgorithm() {
		return algorithm;
	}

	/**
	 * @param algorithm the algorithm to set
	 */
	public void setAlgorithm(JWSAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JWSAlgorithmEmbed [algorithm=" + algorithm + "]";
	}

}
