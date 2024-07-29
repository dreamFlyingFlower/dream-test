package com.wy.test.synchronizer.core.autoconfigure;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.wy.test.core.crypto.password.PasswordReciprocal;
import com.wy.test.core.entity.Synchronizers;
import com.wy.test.synchronizer.core.synchronizer.SynchronizerJob;

import lombok.extern.slf4j.Slf4j;

@AutoConfiguration
@Slf4j
public class SynchronizerAutoConfiguration implements InitializingBean {

	public static final String SYNCHRONIZERS_SELECT_STATEMENT = "select * from mxk_synchronizers where status ='1'";

	@Bean(name = "schedulerSynchronizerJobs")
	String schedulerSynchronizerJobs(JdbcTemplate jdbcTemplate, SchedulerFactoryBean schedulerFactoryBean,
			@Value("${maxkey.job.cron.enable}") boolean jobCronEnable) throws SchedulerException {

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		if (jobCronEnable) {
			List<Synchronizers> synchronizerList = querySynchronizers(jdbcTemplate);
			for (Synchronizers synchronizer : synchronizerList) {
				if (synchronizer.getScheduler() != null && !synchronizer.getScheduler().equals("")
						&& CronExpression.isValidExpression(synchronizer.getScheduler())) {
					log.debug("synchronizer details : " + synchronizer);
					buildJob(scheduler, synchronizer);
				}
			}
		}
		return "schedulerSynchronizerJobs";
	}

	private void buildJob(Scheduler scheduler, Synchronizers synchronizer) throws SchedulerException {
		JobDetail jobDetail = JobBuilder.newJob(SynchronizerJob.class)
				.withIdentity(synchronizer.getService() + "Job", "SynchronizerGroups").build();

		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("synchronizer", synchronizer);
		log.debug("synchronizer : " + synchronizer.getName() + "(" + synchronizer.getId() + "_"
				+ synchronizer.getSourceType() + ")");
		log.debug("synchronizer service : " + synchronizer.getService());
		log.debug("synchronizer Scheduler : " + synchronizer.getScheduler());
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(synchronizer.getScheduler());
		CronTrigger cronTrigger =
				TriggerBuilder.newTrigger().withIdentity("trigger" + synchronizer.getService(), "SynchronizerGroups")
						.usingJobData(jobDataMap).withSchedule(scheduleBuilder).build();
		scheduler.scheduleJob(jobDetail, cronTrigger);
	}

	public List<Synchronizers> querySynchronizers(JdbcTemplate jdbcTemplate) {
		List<Synchronizers> synchronizerList =
				jdbcTemplate.query(SYNCHRONIZERS_SELECT_STATEMENT, new RowMapper<Synchronizers>() {

					@Override
					public Synchronizers mapRow(ResultSet rs, int rowNum) throws SQLException {
						Synchronizers synchronizer = new Synchronizers();
						synchronizer.setId(rs.getString("id"));
						synchronizer.setName(rs.getString("name"));
						synchronizer.setScheduler(rs.getString("scheduler"));
						synchronizer.setSourceType(rs.getString("sourcetype"));
						synchronizer.setProviderUrl(rs.getString("providerurl"));
						synchronizer.setDriverClass(rs.getString("driverclass"));
						synchronizer.setPrincipal(rs.getString("principal"));
						synchronizer
								.setCredentials(PasswordReciprocal.getInstance().decoder(rs.getString("credentials")));
						synchronizer.setResumeTime(rs.getString("resumetime"));
						synchronizer.setSuspendTime(rs.getString("suspendtime"));
						synchronizer.setUserFilters(rs.getString("userfilters"));
						synchronizer.setUserBasedn(rs.getString("userbasedn"));
						synchronizer.setOrgFilters(rs.getString("orgfilters"));
						synchronizer.setOrgBasedn(rs.getString("orgbasedn"));
						synchronizer.setMsadDomain(rs.getString("msaddomain"));
						synchronizer.setSslSwitch(rs.getString("sslswitch"));
						synchronizer.setTrustStore(rs.getString("truststore"));
						synchronizer.setStatus(rs.getString("status"));
						synchronizer.setDescription(rs.getString("description"));
						synchronizer.setSyncStartTime(rs.getInt("syncstarttime"));
						synchronizer.setService(rs.getString("service"));

						return synchronizer;
					}
				});

		return synchronizerList;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}
