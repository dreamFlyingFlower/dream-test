package com.wy.test.sync.reorgdept.reorg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.test.core.entity.SyncEntity;
import com.wy.test.sync.core.synchronizer.ISynchronizerService;
import com.wy.test.sync.reorgdept.workweixin.service.ReorgDeptService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReorgDeptSynchronizerService implements ISynchronizerService {

	SyncEntity synchronizer;

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
	public void setSynchronizer(SyncEntity synchronizer) {
		this.synchronizer = synchronizer;

	}

}
