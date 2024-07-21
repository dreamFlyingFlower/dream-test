package com.wy.test.synchronizer.reorgdept.reorg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.test.core.entity.Synchronizers;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;
import com.wy.test.synchronizer.reorgdept.workweixin.service.ReorgDeptService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReorgDeptSynchronizerService implements ISynchronizerService {

	Synchronizers synchronizer;

	@Autowired
	ReorgDeptService reorgDeptService;

	public ReorgDeptSynchronizerService() {
		super();
	}

	@Override
	public void sync() throws Exception {
		log.info("Sync ...");
		reorgDeptService.setSynchronizer(synchronizer);
		reorgDeptService.sync();

	}

	public void setReorgDeptService(ReorgDeptService reorgDeptService) {
		this.reorgDeptService = reorgDeptService;
	}

	@Override
	public void setSynchronizer(Synchronizers synchronizer) {
		this.synchronizer = synchronizer;

	}

}
