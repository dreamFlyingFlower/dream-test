package com.wy.test.sync.core.autoconfigure;

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
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.wy.test.core.entity.SyncEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.properties.DreamAuthJobProperties;
import com.wy.test.sync.core.synchronizer.SyncJob;

import lombok.extern.slf4j.Slf4j;

@EnableConfigurationProperties(DreamAuthJobProperties.class)
@AutoConfiguration
@Slf4j
public class SyncAutoConfiguration implements InitializingBean {

	public static final String SYNCHRONIZERS_SELECT_STATEMENT = "select * from auth_sync where status ='1'";

	@Bean(name = "schedulerSynchronizerJobs")
	String schedulerSynchronizerJobs(JdbcTemplate jdbcTemplate, SchedulerFactoryBean schedulerFactoryBean,
			DreamAuthJobProperties dreamAuthJobProperties) throws SchedulerException {

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		if (dreamAuthJobProperties.getCron().isEnabled()) {
			List<SyncEntity> synchronizerList = querySynchronizers(jdbcTemplate);
			for (SyncEntity synchronizer : synchronizerList) {
				if (synchronizer.getScheduler() != null && !synchronizer.getScheduler().equals("")
						&& CronExpression.isValidExpression(synchronizer.getScheduler())) {
					log.debug("synchronizer details : " + synchronizer);
					buildJob(scheduler, synchronizer);
				}
			}
		}
		return "schedulerSynchronizerJobs";
	}

	private void buildJob(Scheduler scheduler, SyncEntity synchronizer) throws SchedulerException {
		JobDetail jobDetail = JobBuilder.newJob(SyncJob.class)
				.withIdentity(synchronizer.getService() + "Job", "SynchronizerGroups")
				.build();

		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("synchronizer", synchronizer);
		log.debug("synchronizer : " + synchronizer.getName() + "(" + synchronizer.getId() + "_"
				+ synchronizer.getSourceType() + ")");
		log.debug("synchronizer service : " + synchronizer.getService());
		log.debug("synchronizer Scheduler : " + synchronizer.getScheduler());
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(synchronizer.getScheduler());
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity("trigger" + synchronizer.getService(), "SynchronizerGroups")
				.usingJobData(jobDataMap)
				.withSchedule(scheduleBuilder)
				.build();
		scheduler.scheduleJob(jobDetail, cronTrigger);
	}

	public List<SyncEntity> querySynchronizers(JdbcTemplate jdbcTemplate) {
		List<SyncEntity> synchronizerList =
				jdbcTemplate.query(SYNCHRONIZERS_SELECT_STATEMENT, new RowMapper<SyncEntity>() {

					@Override
					public SyncEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
						SyncEntity synchronizer = new SyncEntity();
						synchronizer.setId(rs.getString("id"));
						synchronizer.setName(rs.getString("name"));
						synchronizer.setScheduler(rs.getString("scheduler"));
						synchronizer.setSourceType(rs.getString("source_type"));
						synchronizer.setProviderUrl(rs.getString("provider_url"));
						synchronizer.setDriverClass(rs.getString("driver_class"));
						synchronizer.setPrincipal(rs.getString("principal"));
						synchronizer
								.setCredentials(PasswordReciprocal.getInstance().decoder(rs.getString("credentials")));
						synchronizer.setResumeTime(rs.getString("resume_time"));
						synchronizer.setSuspendTime(rs.getString("suspend_time"));
						synchronizer.setUserBasedn(rs.getString("user_basedn"));
						synchronizer.setUserFilters(rs.getString("user_filters"));
						synchronizer.setOrgBasedn(rs.getString("org_basedn"));
						synchronizer.setOrgFilters(rs.getString("org_filters"));
						synchronizer.setMsadDomain(rs.getString("msad_domain"));
						synchronizer.setSslSwitch(rs.getInt("ssl_switch"));
						synchronizer.setTrustStore(rs.getString("trust_store"));
						synchronizer.setStatus(rs.getInt("status"));
						synchronizer.setRemark(rs.getString("remark"));
						synchronizer.setSyncStartTime(rs.getInt("sync_start_time"));
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
