package com.wy.test.sync.reorgdept.reorg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.test.core.entity.SyncEntity;
import com.wy.test.sync.core.synchronizer.SyncProcessor;
import com.wy.test.sync.reorgdept.workweixin.service.ReorgDeptProcessor;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReorgDeptSyncProcessor implements SyncProcessor {

	SyncEntity synchronizer;

	@Autowired
	ReorgDeptProcessor reorgDeptService;

	public ReorgDeptSyncProcessor() {
		super();
	}

	@Override
	public void sync() throws Exception {
		log.info("Sync ...");
		reorgDeptService.setSynchronizer(synchronizer);
		reorgDeptService.sync();

	}

	public void setReorgDeptService(ReorgDeptProcessor reorgDeptService) {
		this.reorgDeptService = reorgDeptService;
	}

	@Override
	public void setSynchronizer(SyncEntity synchronizer) {
		this.synchronizer = synchronizer;

	}

}
