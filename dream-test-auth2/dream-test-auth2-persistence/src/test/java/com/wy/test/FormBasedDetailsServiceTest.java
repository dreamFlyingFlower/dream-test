//package com.wy.test;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
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
//import com.wy.test.entity.apps.AppsFormBasedDetails;
//import com.wy.test.persistence.service.AppsFormBasedDetailsService;
//
//public class FormBasedDetailsServiceTest {
//
//	private static final Logger log = LoggerFactory.getLogger(FormBasedDetailsServiceTest.class);
//
//	public static ApplicationContext context;
//
//	public static AppsFormBasedDetailsService service;
//
//	public AppsFormBasedDetailsService getservice() {
//		service = (AppsFormBasedDetailsService) MybatisJpaContext.getBean("appsFormBasedDetailsService");
//		return service;
//	}
//
//	@Test
//	public void insert() throws Exception {
//		log.info("insert...");
//
//		AppsFormBasedDetails formBasedDetails = new AppsFormBasedDetails();
//
//		service.insert(formBasedDetails);
//
//		Thread.sleep(1000);
//		service.remove(formBasedDetails.getId());
//
//	}
//
//	@Test
//	public void get() throws Exception {
//		log.info("get...");
//		AppsFormBasedDetails formBasedDetails = service.get("850379a1-7923-4f6b-90be-d363b2dfd2ca");
//
//		log.info("formBasedDetails " + formBasedDetails);
//
//	}
//
//	@Test
//	public void remove() throws Exception {
//
//		log.info("remove...");
//		AppsFormBasedDetails formBasedDetails = new AppsFormBasedDetails();
//		formBasedDetails.setId("921d3377-937a-4578-b1e2-92fb23b5e512");
//		service.remove(formBasedDetails.getId());
//
//	}
//
//	@Test
//	public void batchDelete() throws Exception {
//		log.info("batchDelete...");
//		List<String> idList = new ArrayList<String>();
//		idList.add("8584804d-b5ac-45d2-9f91-4dd8e7a090a7");
//		idList.add("ab7422e9-a91a-4840-9e59-9d911257c918");
//		idList.add("12b6ceb8-573b-4f01-ad85-cfb24cfa007c");
//		idList.add("dafd5ba4-d2e3-4656-bd42-178841e610fe");
//		service.deleteBatch(idList);
//	}
//
//	@Test
//	public void queryPageResults() throws Exception {
//
//		log.info("queryPageResults...");
//		AppsFormBasedDetails formBasedDetails = new AppsFormBasedDetails();
//		formBasedDetails.setPageNumber(2);
//		log.info("queryPageResults " + service.queryPageResults(formBasedDetails));
//	}
//
//	@Test
//	public void queryPageResultsByMapperId() throws Exception {
//
//		log.info("queryPageResults by mapperId...");
//		AppsFormBasedDetails formBasedDetails = new AppsFormBasedDetails();
//
//		formBasedDetails.setPageNumber(2);
//
//		log.info("queryPageResults by mapperId " + service.queryPageResults("queryPageResults1", formBasedDetails));
//
//	}
//
//	@Test
//	public void findAll() throws Exception {
//		log.info("findAll...");
//		log.info("findAll " + service.findAll());
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
//			FormBasedDetailsServiceTest runner = new FormBasedDetailsServiceTest();
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