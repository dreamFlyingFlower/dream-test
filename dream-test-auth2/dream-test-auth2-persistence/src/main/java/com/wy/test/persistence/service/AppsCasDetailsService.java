/*
 * Copyright [2020] [MaxKey of copyright http://www.maxkey.top]
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.wy.test.persistence.service;

import java.util.concurrent.TimeUnit;

import org.dromara.mybatis.jpa.JpaService;
import org.springframework.stereotype.Repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.entity.apps.AppsCasDetails;
import com.wy.test.persistence.mapper.AppsCasDetailsMapper;

@Repository
public class AppsCasDetailsService extends JpaService<AppsCasDetails> {

	protected final static Cache<String, AppsCasDetails> detailsCache =
			Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(200000).build();

	public AppsCasDetailsService() {
		super(AppsCasDetailsMapper.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.db.service.BaseService#getMapper()
	 */
	@Override
	public AppsCasDetailsMapper getMapper() {
		return (AppsCasDetailsMapper) super.getMapper();
	}

	public AppsCasDetails getAppDetails(String id, boolean cached) {
		AppsCasDetails details = null;
		if (cached) {
			details = detailsCache.getIfPresent(id);
			if (details == null) {
				details = getMapper().getAppDetails(id);
				if (details != null) {
					detailsCache.put(id, details);
				}
			}
		} else {
			details = getMapper().getAppDetails(id);
		}
		return details;
	}
}