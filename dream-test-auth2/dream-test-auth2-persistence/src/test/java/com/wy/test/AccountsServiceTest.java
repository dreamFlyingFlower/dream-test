//package com.wy.test;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import org.dromara.mybatis.jpa.spring.MybatisJpaContext;
//import org.junit.jupiter.api.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
//import com.wy.test.entity.Accounts;
//import com.wy.test.persistence.service.AccountsService;
//
//public class AccountsServiceTest {
//
//	public static ApplicationContext context;
//
//	public static AccountsService service;
//
//	public AccountsService getservice() {
//		service = (AccountsService) MybatisJpaContext.getBean("accountsService");
//		return service;
//	}
//
//	@Test
//	public void get() throws Exception {
//		log.info("get...");
//		Accounts accounts = service.get("26b1c864-ae81-4b1f-9355-74c4c699cb6b");
//
//		log.info("accounts " + accounts);
//
//	}
//
//	@Test
//	public void load() throws Exception {
//		log.info("get...");
//		Accounts queryAccounts =
//				new Accounts("7BF5315CA1004CDB8E614B0361C4D46B", "fe86db85-5475-4494-b5aa-dbd3b886ff64");
//
//		log.info("accounts " + queryAccounts);
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
//			AccountsServiceTest runner = new AccountsServiceTest();
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