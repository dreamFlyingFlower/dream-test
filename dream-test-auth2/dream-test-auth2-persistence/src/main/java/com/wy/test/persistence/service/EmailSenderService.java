package com.wy.test.persistence.service;

import com.wy.test.core.entity.EmailSenderEntity;
import com.wy.test.core.query.EmailSenderQuery;
import com.wy.test.core.vo.EmailSenderVO;

import dream.flying.flower.framework.mybatis.plus.service.BaseServices;

/**
 * 邮件
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface EmailSenderService extends BaseServices<EmailSenderEntity, EmailSenderVO, EmailSenderQuery> {

}