package com.wy.test.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import dream.flying.flower.framework.mybatis.plus.entity.AbstractStringEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 邮件
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("auth_email_sender")
public class EmailSenderEntity extends AbstractStringEntity {

	private static final long serialVersionUID = 3689854324601731505L;

	private String account;

	private String credentials;

	private String smtpHost;

	private Integer port;

	private int sslSwitch;

	private String sender;

	private String encoding;

	private String protocol;

	private int status;

	private String instId;

	private String remark;
}