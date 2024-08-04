package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.EmailSenderEntity;
import com.wy.test.core.query.EmailSenderQuery;
import com.wy.test.core.vo.EmailSenderVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;


/**
 * 邮件
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface EmailSenderMapper extends BaseMappers<EmailSenderEntity, EmailSenderVO, EmailSenderQuery> {

}