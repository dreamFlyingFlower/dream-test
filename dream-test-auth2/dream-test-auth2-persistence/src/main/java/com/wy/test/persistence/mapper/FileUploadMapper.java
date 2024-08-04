package com.wy.test.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.wy.test.core.entity.FileUploadEntity;
import com.wy.test.core.query.FileUploadQuery;
import com.wy.test.core.vo.FileUploadVO;

import dream.flying.flower.framework.mybatis.plus.mapper.BaseMappers;

/**
 * 文件上传
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Mapper
public interface FileUploadMapper extends BaseMappers<FileUploadEntity, FileUploadVO, FileUploadQuery> {

}