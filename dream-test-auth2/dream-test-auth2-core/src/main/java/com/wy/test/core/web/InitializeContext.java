package com.wy.test.core.web;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.lang3.ArchUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.arch.Processor;
import org.joda.time.DateTime;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.wy.test.core.constant.ConstDatabase;
import com.wy.test.core.util.PathHelpers;

import lombok.extern.slf4j.Slf4j;

/**
 * InitApplicationContext .
 */
@Slf4j
public class InitializeContext extends HttpServlet {

	private static final long serialVersionUID = -797399138268601444L;

	ApplicationContext applicationContext;

	@Override
	public String getServletInfo() {
		return super.getServletInfo();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		WebContext.applicationContext = applicationContext;

		// List Environment Variables
		listEnvVars();

		listProperties();

		// List DatabaseMetaData Variables
		listDataBaseVariables();

		// Show License
		showLicense();
	}

	/**
	 * InitApplicationContext.
	 */
	public InitializeContext() {
		this.applicationContext = WebApplicationContextUtils.getWebApplicationContext(this.getServletContext());
	}

	public InitializeContext(ConfigurableApplicationContext applicationContext) {
		if (applicationContext.containsBean("localeResolver")
				&& applicationContext.containsBean("cookieLocaleResolver")) {
			BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
			beanFactory.removeBeanDefinition("localeResolver");
			beanFactory.registerBeanDefinition("localeResolver", beanFactory.getBeanDefinition("cookieLocaleResolver"));
			log.debug("cookieLocaleResolver replaced localeResolver.");
		}
		this.applicationContext = applicationContext;
	}

	/**
	 * listDataBaseVariables.
	 */
	public void listDataBaseVariables() {
		if (applicationContext.containsBean("dataSource")) {
			try {
				log.debug("-----------------------------------------------------------");
				log.debug("List DatabaseMetaData Variables ");
				Connection connection =
						((javax.sql.DataSource) applicationContext.getBean("dataSource")).getConnection();

				DatabaseMetaData databaseMetaData = connection.getMetaData();
				ConstDatabase.databaseProduct = databaseMetaData.getDatabaseProductName();

				log.debug("DatabaseProductName   :   {}", databaseMetaData.getDatabaseProductName());
				log.debug("DatabaseProductVersion:   {}", databaseMetaData.getDatabaseProductVersion());
				log.trace("DatabaseMajorVersion  :   {}", databaseMetaData.getDatabaseMajorVersion());
				log.trace("DatabaseMinorVersion  :   {}", databaseMetaData.getDatabaseMinorVersion());
				log.trace("supportsTransactions  :   {}", databaseMetaData.supportsTransactions());
				log.trace("DefaultTransaction    :   {}", databaseMetaData.getDefaultTransactionIsolation());
				log.trace("MaxConnections        :   {}", databaseMetaData.getMaxConnections());
				log.trace("");
				log.trace("JDBCMajorVersion      :   {}", databaseMetaData.getJDBCMajorVersion());
				log.trace("JDBCMinorVersion      :   {}", databaseMetaData.getJDBCMinorVersion());
				log.trace("DriverName            :   {}", databaseMetaData.getDriverName());
				log.trace("DriverVersion         :   {}", databaseMetaData.getDriverVersion());
				log.debug("");
				log.debug("DBMS  URL             :   {}", databaseMetaData.getURL());
				log.debug("UserName              :   {}", databaseMetaData.getUserName());
				log.debug("-----------------------------------------------------------");
			} catch (SQLException e) {
				e.printStackTrace();
				log.error("DatabaseMetaData Variables Error .", e);
			}
		}
	}

	/**
	 * propertySourcesPlaceholderConfigurer.
	 */
	public void listProperties() {
		if (applicationContext.containsBean("propertySourcesPlaceholderConfigurer")) {
			log.trace("-----------------------------------------------------------");
			log.trace("List Properties Variables ");
			PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer =
					((PropertySourcesPlaceholderConfigurer) applicationContext
							.getBean("propertySourcesPlaceholderConfigurer"));

			WebContext.properties = (StandardEnvironment) propertySourcesPlaceholderConfigurer
					.getAppliedPropertySources()
					.get(PropertySourcesPlaceholderConfigurer.ENVIRONMENT_PROPERTIES_PROPERTY_SOURCE_NAME).getSource();

			Iterator<PropertySource<?>> it = WebContext.properties.getPropertySources().iterator();
			while (it.hasNext()) {
				log.debug("propertySource {}", it.next());
			}

			log.trace("-----------------------------------------------------------");
		}
	}

	/**
	 * listEnvVars.
	 */
	public void listEnvVars() {
		log.debug("-----------------------------------------------------------");
		log.debug("List Environment Variables ");
		Map<String, String> map = System.getenv();
		SortedSet<String> keyValueSet = new TreeSet<String>();
		for (Iterator<String> itr = map.keySet().iterator(); itr.hasNext();) {
			String key = itr.next();
			keyValueSet.add(key);
		}
		// out
		for (Iterator<String> it = keyValueSet.iterator(); it.hasNext();) {
			String key = (String) it.next();
			log.trace(key + "   =   {}", map.get(key));
		}
		log.debug("APP_HOME" + "   =   {}", PathHelpers.getInstance().getAppPath());

		Processor processor = ArchUtils.getProcessor();
		if (Objects.isNull(processor)) {
			processor = new Processor(Processor.Arch.UNKNOWN, Processor.Type.UNKNOWN);
		}
		log.debug("OS      : {}({} {}), version {}", SystemUtils.OS_NAME, SystemUtils.OS_ARCH, processor.getType(),
				SystemUtils.OS_VERSION

		);
		log.debug("COMPUTER: {}, USERNAME : {}", map.get("COMPUTERNAME"), map.get("USERNAME"));
		log.debug("JAVA    :");
		log.debug("{} java version {}, class {}", SystemUtils.JAVA_VENDOR, SystemUtils.JAVA_VERSION,
				SystemUtils.JAVA_CLASS_VERSION);
		log.debug("{} (build {}, {})", SystemUtils.JAVA_VM_NAME, SystemUtils.JAVA_VM_VERSION, SystemUtils.JAVA_VM_INFO);

		log.debug("-----------------------------------------------------------");

	}

	/**
	 * showLicense.
	 */
	public void showLicense() {
		log.info("-----------------------------------------------------------");
		log.info("+                       Community  Edition ");
		log.info("+                      Single   Sign  On ( SSO ) ");
		log.info("+                           Version {}",
				WebContext.properties.getProperty("dream.auth.app.version"));
		log.info("+");
		log.info("+                 {}Copyright 2018 - {} https://www.top/", (char) 0xA9, new DateTime().getYear());
		log.info("+                 Licensed under the Apache License, Version 2.0 ");
		log.info("-----------------------------------------------------------");
	}
}