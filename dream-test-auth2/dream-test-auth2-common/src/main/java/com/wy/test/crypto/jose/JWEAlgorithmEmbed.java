package com.wy.test.crypto.jose;

import com.google.common.base.Strings;
import com.nimbusds.jose.JWEAlgorithm;

/**
 * Wrapper class for Nimbus JOSE objects to fit into JPA
 * 
 * @author 飞花梦影
 * @date 2024-07-14 21:33:35
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class JWEAlgorithmEmbed {

	public static final JWEAlgorithmEmbed NONE = getForAlgorithmName("none");

	private JWEAlgorithm algorithm;

	public JWEAlgorithmEmbed() {

	}

	public JWEAlgorithmEmbed(JWEAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	public static JWEAlgorithmEmbed getForAlgorithmName(String algorithmName) {
		JWEAlgorithmEmbed ent = new JWEAlgorithmEmbed();
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
	 * Set the name of this algorithm. Calls JWEAlgorithm.parse()
	 * 
	 * @param algorithmName
	 */
	public void setAlgorithmName(String algorithmName) {
		if (!Strings.isNullOrEmpty(algorithmName)) {
			algorithm = JWEAlgorithm.parse(algorithmName);
		} else {
			algorithm = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JWEAlgorithmEmbed [algorithm=" + algorithm + "]";
	}

	/**
	 * @return the algorithm
	 */
	public JWEAlgorithm getAlgorithm() {
		return algorithm;
	}

	/**
	 * @param algorithm the algorithm to set
	 */
	public void setAlgorithm(JWEAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

}
