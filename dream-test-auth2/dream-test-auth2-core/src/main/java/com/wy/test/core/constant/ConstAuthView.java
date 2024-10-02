package com.wy.test.core.constant;

/**
 * 视图地址常量 TODO 需要修改为可配置
 *
 * @author 飞花梦影
 * @date 2024-08-25 15:29:11
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ConstAuthView {

	/**
	 * 权限拒绝重定向URL
	 */
	String AUTHZ_REFUSED = "/authz/refused";

	/**
	 * 权限拒绝重定向视图URL
	 */
	String AUTHZ_REFUSED_VIEW = "authorize/authorize_refused";
}