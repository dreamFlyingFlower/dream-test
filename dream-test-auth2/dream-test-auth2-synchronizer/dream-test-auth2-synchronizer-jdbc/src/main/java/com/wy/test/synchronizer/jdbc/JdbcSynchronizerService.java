package com.wy.test.synchronizer.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.test.entity.Synchronizers;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JdbcSynchronizerService implements ISynchronizerService {

	Synchronizers synchronizer;

	@Autowired
	JdbcUsersService jdbcUsersService;

	@Autowired
	JdbcOrganizationService jdbcOrganizationService;

	public JdbcSynchronizerService() {
		super();
	}

	@Override
	public void sync() {
		log.info("Sync ...");
		jdbcOrganizationService.setSynchronizer(synchronizer);
		jdbcOrganizationService.sync();

		jdbcUsersService.setSynchronizer(synchronizer);
		jdbcUsersService.sync();
	}

	public void setJdbcUsersService(JdbcUsersService jdbcUsersService) {
		this.jdbcUsersService = jdbcUsersService;
	}

	public void setJdbcOrganizationService(JdbcOrganizationService jdbcOrganizationService) {
		this.jdbcOrganizationService = jdbcOrganizationService;
	}

	@Override
	public void setSynchronizer(Synchronizers synchronizer) {
		this.synchronizer = synchronizer;
	}
}