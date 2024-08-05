package com.wy.test.protocol.saml.autoconfigure;

import java.io.IOException;
import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.opensaml.common.binding.security.IssueInstantRule;
import org.opensaml.common.binding.security.MessageReplayRule;
import org.opensaml.util.storage.MapBasedStorageService;
import org.opensaml.util.storage.ReplayCache;
import org.opensaml.util.storage.ReplayCacheEntry;
import org.opensaml.xml.ConfigurationException;
import org.opensaml.xml.parse.BasicParserPool;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.wy.test.core.entity.Saml20Metadata;
import com.wy.test.core.properties.DreamAuthSamlIdpProperties;
import com.wy.test.core.properties.DreamAuthSamlIssueProperties;
import com.wy.test.core.properties.DreamAuthSamlProperties;
import com.wy.test.protocol.saml.authz.saml.common.EndpointGenerator;
import com.wy.test.protocol.saml.authz.saml.service.IDService;
import com.wy.test.protocol.saml.authz.saml.service.TimeService;
import com.wy.test.protocol.saml.authz.saml20.binding.decoder.OpenHTTPPostDecoder;
import com.wy.test.protocol.saml.authz.saml20.binding.decoder.OpenHTTPPostSimpleSignDecoder;
import com.wy.test.protocol.saml.authz.saml20.binding.decoder.OpenHTTPRedirectDecoder;
import com.wy.test.protocol.saml.authz.saml20.binding.impl.ExtractPostBindingAdapter;
import com.wy.test.protocol.saml.authz.saml20.binding.impl.ExtractRedirectBindingAdapter;
import com.wy.test.protocol.saml.authz.saml20.binding.impl.PostBindingAdapter;
import com.wy.test.protocol.saml.authz.saml20.binding.impl.PostSimpleSignBindingAdapter;
import com.wy.test.protocol.saml.authz.saml20.provider.xml.AuthnResponseGenerator;
import com.wy.test.protocol.saml.authz.saml20.xml.SAML2ValidatorSuite;
import com.wy.test.protocol.saml.velocity.VelocityEngineFactoryBean;

import dream.flying.flower.framework.web.crypto.keystore.KeyStoreLoader;
import lombok.extern.slf4j.Slf4j;

// import org.dromara.dream.authz.saml20.binding.decoder.OpenHTTPPostDecoder;
// import
// org.dromara.dream.authz.saml20.binding.decoder.OpenHTTPPostSimpleSignDecoder;
// import
// org.dromara.dream.authz.saml20.binding.decoder.OpenHTTPRedirectDecoder;
// import
// org.dromara.dream.authz.saml20.binding.impl.ExtractPostBindingAdapter;
// import
// org.dromara.dream.authz.saml20.binding.impl.ExtractRedirectBindingAdapter;
// import org.dromara.dream.authz.saml20.binding.impl.PostBindingAdapter;
// import
// org.dromara.dream.authz.saml20.binding.impl.PostSimpleSignBindingAdapter;
// import org.dromara.dream.authz.saml20.provider.xml.AuthnResponseGenerator;
// import org.dromara.dream.authz.saml20.xml.SAML2ValidatorSuite;
// import org.dromara.dream.crypto.keystore.KeyStoreLoader;

@SuppressWarnings({ "deprecation" })
@AutoConfiguration
@ComponentScan(basePackages = { "com.wy.authz.saml20.provider.endpoint", "com.wy.authz.saml20.metadata.endpoint", })
@Slf4j
public class Saml20AutoConfiguration implements InitializingBean {

	/**
	 * samlBootstrapInitializer.
	 * 
	 * @return samlBootstrapInitializer
	 * @throws ConfigurationException
	 */
	@Bean(name = "samlBootstrapInitializer")
	String samlBootstrapInitializer() throws ConfigurationException {
		return "";
	}

	/**
	 * TimeService.
	 * 
	 * @return timeService
	 */
	@Bean(name = "timeService")
	TimeService TimeService() {
		TimeService timeService = new TimeService();
		return timeService;
	}

	/**
	 * IDService.
	 * 
	 * @return idService
	 */
	@Bean(name = "idService")
	IDService idService() {
		IDService idService = new IDService();
		return idService;
	}

	/**
	 * EndpointGenerator.
	 * 
	 * @return endpointGenerator
	 */
	@Bean(name = "endpointGenerator")
	EndpointGenerator endpointGenerator() {
		EndpointGenerator generator = new EndpointGenerator();
		return generator;
	}

	/**
	 * AuthnResponseGenerator.
	 * 
	 * @return authnResponseGenerator
	 */
	@Bean(name = "authnResponseGenerator")
	AuthnResponseGenerator authnResponseGenerator(TimeService timeService, IDService idService,
			DreamAuthSamlIdpProperties dreamAuthSamlIdpProperties) {
		log.debug("issuerEntityName " + dreamAuthSamlIdpProperties.getIssuer());
		AuthnResponseGenerator generator =
				new AuthnResponseGenerator(dreamAuthSamlIdpProperties.getIssuer(), timeService, idService);
		return generator;
	}

	/**
	 * IssuerEntityName.
	 * 
	 * @return issuerEntityName
	 */
	@Bean(name = "issuerEntityName")
	String issuerEntityName(DreamAuthSamlIdpProperties dreamAuthSamlIdpProperties) {
		return dreamAuthSamlIdpProperties.getIssuer();
	}

	/**
	 * Saml20Metadata.
	 * 
	 * @return saml20Metadata
	 */
	@Bean(name = "saml20Metadata")
	Saml20Metadata saml20Metadata(DreamAuthSamlProperties dreamAuthSamlProperties) {
		Saml20Metadata metadata = new Saml20Metadata();
		metadata.setOrgName(dreamAuthSamlProperties.getMetadata().getOrgName());
		metadata.setOrgDisplayName(dreamAuthSamlProperties.getMetadata().getOrgDisplayName());
		metadata.setOrgURL(dreamAuthSamlProperties.getMetadata().getOrgUrl());
		metadata.setCompany(dreamAuthSamlProperties.getMetadata().getCompany());
		metadata.setContactType(dreamAuthSamlProperties.getMetadata().getContactType());
		metadata.setGivenName(dreamAuthSamlProperties.getMetadata().getGivenName());
		metadata.setSurName(dreamAuthSamlProperties.getMetadata().getSurName());
		metadata.setEmailAddress(dreamAuthSamlProperties.getMetadata().getEmailAddress());
		metadata.setPhoneNumber(dreamAuthSamlProperties.getMetadata().getPhoneNumber());
		return metadata;
	}

	/**
	 * SAML2ValidatorSuite.
	 * 
	 * @return samlValidaotrSuite
	 */
	@Bean(name = "samlValidaotrSuite")
	SAML2ValidatorSuite validatorSuite() {
		SAML2ValidatorSuite validatorSuite = new SAML2ValidatorSuite();
		return validatorSuite;
	}

	/**
	 * MapBasedStorageService.
	 * 
	 * @return mapBasedStorageService
	 */
	@SuppressWarnings("rawtypes")
	@Bean(name = "mapBasedStorageService")
	MapBasedStorageService mapBasedStorageService() {
		MapBasedStorageService mapBasedStorageService = new MapBasedStorageService();
		return mapBasedStorageService;
	}

	/**
	 * VelocityEngineFactoryBean.
	 * 
	 * @return velocityEngine
	 * @throws IOException
	 * @throws VelocityException
	 */
	@Bean(name = "velocityEngine")
	VelocityEngine velocityEngine() throws VelocityException, IOException {
		VelocityEngineFactoryBean factory = new VelocityEngineFactoryBean();
		factory.setPreferFileSystemAccess(false);
		Properties velocityProperties = new Properties();
		velocityProperties.put("resource.loader", "classpath");
		velocityProperties.put("classpath.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		factory.setVelocityProperties(velocityProperties);
		return factory.createVelocityEngine();
	}

	/**
	 * ReplayCache.
	 * 
	 * @return replayCache
	 */
	@Bean(name = "replayCache")
	ReplayCache replayCache(MapBasedStorageService<String, ReplayCacheEntry> mapBasedStorageService,
			DreamAuthSamlProperties dreamAuthSamlProperties) {
		ReplayCache replayCache =
				new ReplayCache(mapBasedStorageService, dreamAuthSamlProperties.getReplay().getCacheLifeInMillis());
		return replayCache;
	}

	/**
	 * MessageReplayRule.
	 * 
	 * @return messageReplayRule
	 */
	@Bean(name = "messageReplayRule")
	MessageReplayRule messageReplayRule(ReplayCache replayCache) {
		MessageReplayRule messageReplayRule = new MessageReplayRule(replayCache);
		return messageReplayRule;
	}

	/**
	 * BasicParserPool.
	 * 
	 * @return samlParserPool
	 */
	@Bean(name = "samlParserPool")
	BasicParserPool samlParserPool(DreamAuthSamlProperties dreamAuthSamlProperties) {
		BasicParserPool samlParserPool = new BasicParserPool();
		samlParserPool.setMaxPoolSize(dreamAuthSamlProperties.getMaxParserPoolSize());
		return samlParserPool;
	}

	/**
	 * IssueInstantRule.
	 * 
	 * @return issueInstantRule
	 */
	@Bean(name = "issueInstantRule")
	IssueInstantRule issueInstantRule(DreamAuthSamlIssueProperties dreamAuthSamlIssueProperties) {
		IssueInstantRule decoder =
				new IssueInstantRule(dreamAuthSamlIssueProperties.getInstant().getCheckClockSkewInSeconds(),
						dreamAuthSamlIssueProperties.getInstant().getCheckValidityTimeInSeconds());
		decoder.setRequiredRule(true);
		return decoder;
	}

	/**
	 * OpenHTTPPostSimpleSignDecoder.
	 * 
	 * @return openHTTPPostSimpleSignDecoder
	 */
	@Bean(name = "openHTTPPostSimpleSignDecoder")
	OpenHTTPPostSimpleSignDecoder openHTTPPostSimpleSignDecoder(BasicParserPool samlParserPool,
			DreamAuthSamlIdpProperties dreamAuthSamlIdpProperties) {
		OpenHTTPPostSimpleSignDecoder decoder = new OpenHTTPPostSimpleSignDecoder(samlParserPool);
		decoder.setReceiverEndpoint(dreamAuthSamlIdpProperties.getReceiver().getEndpoint());
		return decoder;
	}

	/**
	 * OpenHTTPPostDecoder.
	 * 
	 * @return openHTTPPostDecoder
	 */
	@Bean(name = "openHTTPPostDecoder")
	OpenHTTPPostDecoder openHTTPPostDecoder(BasicParserPool samlParserPool,
			DreamAuthSamlIdpProperties dreamAuthSamlIdpProperties) {
		OpenHTTPPostDecoder decoder = new OpenHTTPPostDecoder(samlParserPool);
		decoder.setReceiverEndpoint(dreamAuthSamlIdpProperties.getReceiver().getEndpoint());
		return decoder;
	}

	/**
	 * OpenHTTPRedirectDecoder.
	 * 
	 * @return openHTTPRedirectDecoder
	 */
	@Bean(name = "openHTTPRedirectDecoder")
	OpenHTTPRedirectDecoder openHTTPRedirectDecoder(BasicParserPool samlParserPool,
			DreamAuthSamlIdpProperties dreamAuthSamlIdpProperties) {
		OpenHTTPRedirectDecoder decoder = new OpenHTTPRedirectDecoder(samlParserPool);
		decoder.setReceiverEndpoint(dreamAuthSamlIdpProperties.getReceiver().getEndpoint());
		return decoder;
	}

	/**
	 * ExtractPostBindingAdapter.
	 * 
	 * @return extractPostBindingAdapter
	 */
	@Bean(name = "extractPostBindingAdapter")
	ExtractPostBindingAdapter extractPostBindingAdapter(OpenHTTPPostDecoder openHTTPPostDecoder,
			KeyStoreLoader keyStoreLoader, IssueInstantRule issueInstantRule, MessageReplayRule messageReplayRule) {
		ExtractPostBindingAdapter adapter = new ExtractPostBindingAdapter(openHTTPPostDecoder);
		adapter.setIssueInstantRule(issueInstantRule);
		adapter.setKeyStoreLoader(keyStoreLoader);
		adapter.setMessageReplayRule(messageReplayRule);
		return adapter;
	}

	/**
	 * ExtractRedirectBindingAdapter.
	 * 
	 * @return extractRedirectBindingAdapter
	 */
	@Bean(name = "extractRedirectBindingAdapter")
	ExtractRedirectBindingAdapter extractRedirectBindingAdapter(OpenHTTPRedirectDecoder openHTTPRedirectDecoder,
			KeyStoreLoader keyStoreLoader, IssueInstantRule issueInstantRule, MessageReplayRule messageReplayRule) {
		ExtractRedirectBindingAdapter adapter = new ExtractRedirectBindingAdapter(openHTTPRedirectDecoder);
		adapter.setIssueInstantRule(issueInstantRule);
		adapter.setKeyStoreLoader(keyStoreLoader);
		adapter.setMessageReplayRule(messageReplayRule);
		return adapter;
	}

	/**
	 * PostSimpleSignBindingAdapter.
	 * 
	 * @return postSimpleSignBindingAdapter
	 */
	@Bean(name = "postSimpleSignBindingAdapter")
	PostSimpleSignBindingAdapter postSimpleSignBindingAdapter(VelocityEngine velocityEngine,
			DreamAuthSamlIdpProperties dreamAuthSamlIdpProperties) {
		PostSimpleSignBindingAdapter adapter = new PostSimpleSignBindingAdapter();
		adapter.setVelocityEngine(velocityEngine);
		adapter.setIssuerEntityName(dreamAuthSamlIdpProperties.getIssuer());
		return adapter;
	}

	/**
	 * PostBindingAdapter.
	 * 
	 * @return postBindingAdapter
	 */
	@Bean(name = "postBindingAdapter")
	PostBindingAdapter postBindingAdapter(VelocityEngine velocityEngine,
			DreamAuthSamlIdpProperties dreamAuthSamlIdpProperties) {
		PostBindingAdapter adapter = new PostBindingAdapter();
		adapter.setVelocityEngine(velocityEngine);
		adapter.setIssuerEntityName(dreamAuthSamlIdpProperties.getIssuer());
		return adapter;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}
