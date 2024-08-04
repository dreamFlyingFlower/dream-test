package com.wy.test.social.authn.support.socialsignon.service;

import java.util.List;

import com.wy.test.core.entity.SocialAssociateEntity;

public interface SocialsAssociateService {

	public boolean insert(SocialAssociateEntity socialsAssociate);

	public List<SocialAssociateEntity> query(SocialAssociateEntity socialsAssociate);

	public SocialAssociateEntity get(SocialAssociateEntity socialsAssociate);

	public boolean delete(SocialAssociateEntity socialsAssociate);

	public boolean update(SocialAssociateEntity socialsAssociate);

}
