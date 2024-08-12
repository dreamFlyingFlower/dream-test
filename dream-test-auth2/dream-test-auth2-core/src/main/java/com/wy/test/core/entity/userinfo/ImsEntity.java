package com.wy.test.core.entity.userinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImsEntity {

	private String aim;

	private String gtalk;

	private String icq;

	private String xmpp;

	private String skype;

	private String qq;

	private String yahoo;

	private String sinaweibo;

	private String weixin;
}