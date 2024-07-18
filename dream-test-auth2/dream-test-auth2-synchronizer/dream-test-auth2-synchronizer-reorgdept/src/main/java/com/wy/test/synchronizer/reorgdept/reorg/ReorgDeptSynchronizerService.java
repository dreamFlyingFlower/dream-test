package com.wy.test.synchronizer.reorgdept.reorg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wy.test.core.entity.Synchronizers;
import com.wy.test.synchronizer.core.synchronizer.ISynchronizerService;
import com.wy.test.synchronizer.reorgdept.workweixin.service.ReorgDeptService;

@Service
public class ReorgDeptSynchronizerService implements ISynchronizerService {

	final static Logger _logger = LoggerFactory.getLogger(ReorgDeptSynchronizerService.class);

	Synchronizers synchronizer;

	@Autowired
	ReorgDeptService reorgDeptService;

	public ReorgDeptSynchronizerService() {
		super();
	}

	@Override
	public void sync() throws Exception {
		_logger.info("Sync ...");
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
