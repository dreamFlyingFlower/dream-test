package com.wy.test.synchronizer.core.synchronizer;

import java.util.HashMap;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wy.test.core.entity.Synchronizers;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.SynchronizersService;

public class SynchronizerJob implements Job {

	final static Logger _logger = LoggerFactory.getLogger(SynchronizerJob.class);

	SynchronizersService synchronizersService;

	public static class JOBSTATUS {

		public static int STOP = 0;

		public static int RUNNING = 1;

		public static int FINISHED = 2;
	}

	private static HashMap<String, Integer> jobStatus = new HashMap<String, Integer>();

	@Override
	public void execute(JobExecutionContext context) {
		Synchronizers synchronizer = readSynchronizer(context);
		if (jobStatus.get(synchronizer.getId()) == null) {
			// init
			jobStatus.put(synchronizer.getId(), JOBSTATUS.STOP);
		} else if (jobStatus.get(synchronizer.getId()) == JOBSTATUS.RUNNING) {
			_logger.info("SynchronizerJob is in running . ");
			return;
		}

		_logger.debug("SynchronizerJob is running ... ");
		jobStatus.put(synchronizer.getId(), JOBSTATUS.RUNNING);
		try {

			_logger.debug("synchronizer : " + synchronizer.getName() + "(" + synchronizer.getId() + "_"
					+ synchronizer.getSourceType() + ")");
			_logger.debug("synchronizer service : " + synchronizer.getService());
			_logger.debug("synchronizer Scheduler : " + synchronizer.getScheduler());
			ISynchronizerService service = (ISynchronizerService) WebContext.getBean(synchronizer.getService());
			service.setSynchronizer(synchronizer);
			service.sync();
			jobStatus.put(synchronizer.getId(), JOBSTATUS.FINISHED);
			_logger.debug("SynchronizerJob is success  ");
		} catch (Exception e) {
			_logger.error("Exception ", e);
			jobStatus.put(synchronizer.getId(), JOBSTATUS.STOP);
		}
		_logger.debug("SynchronizerJob is finished . ");
	}

	public Synchronizers readSynchronizer(JobExecutionContext context) {
		Synchronizers jobSynchronizer = (Synchronizers) context.getMergedJobDataMap().get("synchronizer");
		if (synchronizersService == null) {
			synchronizersService = (SynchronizersService) WebContext.getBean("synchronizersService");
		}
		// read synchronizer by id from database
		Synchronizers synchronizer = synchronizersService.get(jobSynchronizer.getId());
		_logger.trace("synchronizer " + synchronizer);
		return synchronizer;
	}

}
