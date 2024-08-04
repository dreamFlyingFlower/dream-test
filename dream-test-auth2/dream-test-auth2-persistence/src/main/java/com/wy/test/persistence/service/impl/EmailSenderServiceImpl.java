package com.wy.test.persistence.service.impl;

import org.springframework.stereotype.Service;

import com.wy.test.core.convert.EmailSenderConvert;
import com.wy.test.core.entity.EmailSenderEntity;
import com.wy.test.core.query.EmailSenderQuery;
import com.wy.test.core.vo.EmailSenderVO;
import com.wy.test.persistence.mapper.EmailSenderMapper;
import com.wy.test.persistence.service.EmailSenderService;

import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;

/**
 * 邮件
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
public class EmailSenderServiceImpl extends
		AbstractServiceImpl<EmailSenderEntity, EmailSenderVO, EmailSenderQuery, EmailSenderConvert, EmailSenderMapper>
		implements EmailSenderService {

}