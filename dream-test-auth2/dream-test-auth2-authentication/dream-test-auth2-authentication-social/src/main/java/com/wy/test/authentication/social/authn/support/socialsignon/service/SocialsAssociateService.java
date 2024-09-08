package com.wy.test.authentication.social.authn.support.socialsignon.service;

import java.util.List;

import com.wy.test.core.entity.SocialAssociateEntity;

public interface SocialsAssociateService {

	boolean insert(SocialAssociateEntity socialsAssociate);

	List<SocialAssociateEntity> query(SocialAssociateEntity socialsAssociate);

	SocialAssociateEntity get(SocialAssociateEntity socialsAssociate);

	boolean delete(SocialAssociateEntity socialsAssociate);

	boolean update(SocialAssociateEntity socialsAssociate);
}