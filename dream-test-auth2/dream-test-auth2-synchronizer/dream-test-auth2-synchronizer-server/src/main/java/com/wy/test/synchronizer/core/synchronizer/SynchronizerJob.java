package com.wy.test.synchronizer.core.synchronizer;

import java.util.HashMap;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.wy.test.core.entity.SyncEntity;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.SyncService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SynchronizerJob implements Job {

	SyncService synchronizersService;

	public static class JOBSTATUS {

		public static int STOP = 0;

		public static int RUNNING = 1;

		public static int FINISHED = 2;
	}

	private static HashMap<String, Integer> jobStatus = new HashMap<String, Integer>();

	@Override
	public void execute(JobExecutionContext context) {
		SyncEntity synchronizer = readSynchronizer(context);
		if (jobStatus.get(synchronizer.getId()) == null) {
			// init
			jobStatus.put(synchronizer.getId(), JOBSTATUS.STOP);
		} else if (jobStatus.get(synchronizer.getId()) == JOBSTATUS.RUNNING) {
			log.info("SynchronizerJob is in running . ");
			return;
		}

		log.debug("SynchronizerJob is running ... ");
		jobStatus.put(synchronizer.getId(), JOBSTATUS.RUNNING);
		try {

			log.debug("synchronizer : " + synchronizer.getName() + "(" + synchronizer.getId() + "_"
					+ synchronizer.getSourceType() + ")");
			log.debug("synchronizer service : " + synchronizer.getService());
			log.debug("synchronizer Scheduler : " + synchronizer.getScheduler());
			ISynchronizerService service = (ISynchronizerService) WebContext.getBean(synchronizer.getService());
			service.setSynchronizer(synchronizer);
			service.sync();
			jobStatus.put(synchronizer.getId(), JOBSTATUS.FINISHED);
			log.debug("SynchronizerJob is success  ");
		} catch (Exception e) {
			log.error("Exception ", e);
			jobStatus.put(synchronizer.getId(), JOBSTATUS.STOP);
		}
		log.debug("SynchronizerJob is finished . ");
	}

	public SyncEntity readSynchronizer(JobExecutionContext context) {
		SyncEntity jobSynchronizer = (SyncEntity) context.getMergedJobDataMap().get("synchronizer");
		if (synchronizersService == null) {
			synchronizersService = (SyncService) WebContext.getBean("syncService");
		}
		// read synchronizer by id from database
		SyncEntity synchronizer = synchronizersService.getById(jobSynchronizer.getId());
		log.trace("synchronizer " + synchronizer);
		return synchronizer;
	}
}