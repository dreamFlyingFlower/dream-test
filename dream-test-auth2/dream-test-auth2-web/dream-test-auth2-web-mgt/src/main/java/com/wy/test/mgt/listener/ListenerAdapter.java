package com.wy.test.mgt.listener;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListenerAdapter {

	JobExecutionContext context;

	public final static class JOBSTATUS {

		public static int STOP = 0;

		public static int RUNNING = 1;

		public static int ERROR = 2;

		public static int FINISHED = 3;
	}

	protected int jobStatus = JOBSTATUS.STOP;

	void init(JobExecutionContext context) {
		this.context = context;
	};

	@SuppressWarnings("unchecked")
	public <T> T getParameter(String name, Class<T> requiredType) {
		return (T) context.getMergedJobDataMap().get(name);
	};

	public static void addListener(Class<? extends Job> jobClass, Scheduler scheduler, JobDataMap jobDataMap,
			String cronSchedule, String identity) throws SchedulerException {
		log.debug("Cron {}  , Job schedule {}  ", cronSchedule, identity);

		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(identity, identity + "Group").build();

		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronSchedule);

		CronTrigger cronTrigger =
				TriggerBuilder.newTrigger().withIdentity("trigger" + identity, identity + "TriggerGroup")
						.usingJobData(jobDataMap).withSchedule(scheduleBuilder).build();

		scheduler.scheduleJob(jobDetail, cronTrigger);
	}
}