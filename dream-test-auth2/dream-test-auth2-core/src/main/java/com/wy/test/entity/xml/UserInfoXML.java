package com.wy.test.entity.xml;

import javax.xml.bind.annotation.XmlRootElement;

import org.dromara.mybatis.jpa.entity.JpaEntity;

/**
 * xml can not include array , MultipartFile
 */
@XmlRootElement
public class UserInfoXML extends JpaEntity {

	private static final long serialVersionUID = 6942731467730249291L;

	public UserInfoXML() {
		super();
	}
}