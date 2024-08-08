//package com.wy.test;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import org.apache.mybatis.jpa.util.JpaWebContext;
//import org.aspectj.lang.annotation.Before;
//import org.dromara.mybatis.jpa.spring.MybatisJpaContext;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
//import com.wy.test.entity.apps.Apps;
//import com.wy.test.persistence.service.AppsService;
//
//public class AppsServiceTest {
//
//	private static final Logger log = LoggerFactory.getLogger(AppsServiceTest.class);
//
//	public static ApplicationContext context;
//
//	public static AppsService service;
//
//	public AppsService getservice() {
//		service = (AppsService) MybatisJpaContext.getBean("appsService");
//		return service;
//	}
//
//	@Test
//	public void get() throws Exception {
//		log.info("get...");
//		Apps a = new Apps();
//		a.setPageNumber(2);
//		a.setPageSize(10);
//		getservice().queryPageResults(a);
//		// log.info("apps "+);
//
//	}
//
//	@Before
//	public void initSpringContext() {
//		if (context != null)
//			return;
//		log.info("init Spring Context...");
//		SimpleDateFormat sdf_ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String startTime = sdf_ymdhms.format(new Date());
//
//		try {
//			AppsServiceTest runner = new AppsServiceTest();
//			runner.init();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		log.info("-- --Init Start at " + startTime + " , End at  " + sdf_ymdhms.format(new Date()));
//	}
//
//	// Initialization ApplicationContext for Project
//	public void init() {
//		log.info("init ...");
//
//		log.info("Application dir " + System.getProperty("user.dir"));
//		context = new ClassPathXmlApplicationContext(new String[] { "spring/applicationContext.xml" });
//		MybatisJpaContext.init(context);
//		getservice();
//		System.out.println("init ...");
//	}
//}