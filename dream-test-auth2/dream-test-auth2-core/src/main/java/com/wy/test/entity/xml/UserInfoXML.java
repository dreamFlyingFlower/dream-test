package com.wy.test.entity.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.dromara.mybatis.jpa.entity.JpaEntity;

/**
 * xml can not include array , MultipartFile
 */
@XmlRootElement
public class UserInfoXML extends JpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public UserInfoXML() {
		super();
	}
}