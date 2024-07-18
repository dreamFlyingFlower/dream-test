package com.wy.test.social.authn.support.socialsignon.service;

import java.util.List;

import com.wy.test.core.entity.SocialsAssociate;

public interface SocialsAssociateService {

	public boolean insert(SocialsAssociate socialsAssociate);

	public List<SocialsAssociate> query(SocialsAssociate socialsAssociate);

	public SocialsAssociate get(SocialsAssociate socialsAssociate);

	public boolean delete(SocialsAssociate socialsAssociate);

	public boolean update(SocialsAssociate socialsAssociate);

}
