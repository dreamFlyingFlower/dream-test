package com.wy.test.web.mgt.contorller.app;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.SPSSODescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.constant.ConstProtocols;
import com.wy.test.core.convert.AppSamlDetailConvert;
import com.wy.test.core.entity.AppSamlDetailEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.core.vo.AppSamlDetailVO;
import com.wy.test.persistence.service.AppSamlDetailService;
import com.wy.test.protocol.saml2.authz.saml20.metadata.MetadataDescriptorUtil;

import dream.flying.flower.framework.web.crypto.ReciprocalHelpers;
import dream.flying.flower.framework.web.crypto.cert.X509CertHelpers;
import dream.flying.flower.framework.web.crypto.keystore.KeyStoreHelpers;
import dream.flying.flower.framework.web.crypto.keystore.KeyStoreLoader;
import dream.flying.flower.generator.GeneratorStrategyContext;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/apps/saml20" })
@Slf4j
public class SamlDetailController extends BaseAppContorller {

	@Autowired
	private KeyStoreLoader keyStoreLoader;

	@Autowired
	AppSamlDetailService saml20DetailsService;

	@Autowired
	AppSamlDetailConvert appSamlDetailConvert;

	@Autowired
	DreamAuthServerProperties dreamServerProperties;

	@GetMapping(value = { "/init" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> init() {
		AppSamlDetailVO saml20Details = new AppSamlDetailVO();
		saml20Details.setSecret(ReciprocalHelpers.generateKey(""));
		saml20Details.setProtocol(ConstProtocols.SAML20);
		GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();
		saml20Details.setId(generatorStrategyContext.generate());
		return new Message<>(saml20Details).buildResponse();
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AppSamlDetailVO saml20Details = saml20DetailsService.getAppDetails(id, false);
		decoderSecret(saml20Details);
		saml20Details.transIconBase64();
		// modelAndView.addObject("authzURI",applicationConfig.getAuthzUri());
		return new Message<>(saml20Details).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> add(@RequestBody AppSamlDetailVO saml20Details, @CurrentUser UserEntity currentUser) {
		log.debug("-Add  :" + saml20Details);
		try {
			transform(saml20Details);
		} catch (Exception e) {
			e.printStackTrace();
		}
		saml20Details.setInstId(currentUser.getInstId());
		saml20DetailsService.save(appSamlDetailConvert.convert(saml20Details));
		if (null != appService.add(saml20Details)) {
			return new Message<AppSamlDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppSamlDetailEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody AppSamlDetailVO saml20Details, @CurrentUser UserEntity currentUser) {
		log.debug("-update  :" + saml20Details);
		try {
			transform(saml20Details);
		} catch (Exception e) {
			e.printStackTrace();
		}
		saml20Details.setInstId(currentUser.getInstId());
		saml20DetailsService.edit(saml20Details);
		if (appService.edit(saml20Details)) {
			return new Message<AppSamlDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppSamlDetailEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		log.debug("-delete  ids : {} ", ids);
		if (saml20DetailsService.removeByIds(Arrays.asList(ids.split(",")))
				&& appService.removeByIds(Arrays.asList(ids.split(",")))) {
			return new Message<AppSamlDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppSamlDetailEntity>(Message.FAIL).buildResponse();
		}
	}

	protected AppSamlDetailVO transform(AppSamlDetailVO samlDetails) throws Exception {
		super.transform(samlDetails);
		ByteArrayInputStream bArrayInputStream = null;
		if (StringUtils.isNotBlank(samlDetails.getMetaFileId())) {
			bArrayInputStream =
					new ByteArrayInputStream(fileUploadService.getById(samlDetails.getMetaFileId()).getUploaded());
			fileUploadService.removeById(samlDetails.getMetaFileId());
		}

		if (StringUtils.isNotBlank(samlDetails.getFileType())) {
			if (samlDetails.getFileType().equals("certificate")) {// certificate file
				try {
					if (bArrayInputStream != null) {
						samlDetails.setTrustCert(X509CertHelpers.loadCertFromInputStream(bArrayInputStream));
					}
				} catch (IOException e) {
					log.error("read certificate file error .", e);
				}
			} else if (samlDetails.getFileType().equals("metadata_file")) {// metadata file
				if (bArrayInputStream != null) {
					samlDetails = resolveMetaData(samlDetails, bArrayInputStream);
				}
			} else if (samlDetails.getFileType().equals("metadata_url")
					&& StringUtils.isNotBlank(samlDetails.getMetaUrl())) {// metadata url
				CloseableHttpClient httpClient = HttpClients.createDefault();
				HttpPost post = new HttpPost(samlDetails.getMetaUrl());
				CloseableHttpResponse response = httpClient.execute(post);
				samlDetails = resolveMetaData(samlDetails, response.getEntity().getContent());
				;
				response.close();
				httpClient.close();
			}
		}

		if (samlDetails.getTrustCert() != null) {
			samlDetails.setCertSubject(samlDetails.getTrustCert().getSubjectDN().getName());
			samlDetails.setCertExpiration(samlDetails.getTrustCert().getNotAfter().toString());

			samlDetails
					.setCertIssuer(X509CertHelpers.getCommonName(samlDetails.getTrustCert().getIssuerX500Principal()));

			KeyStore keyStore =
					KeyStoreHelpers.clone(keyStoreLoader.getKeyStore(), keyStoreLoader.getKeystorePassword());

			KeyStore trustKeyStore = null;
			if (!samlDetails.getEntityId().equals("")) {
				trustKeyStore = KeyStoreHelpers.importTrustCertificate(keyStore, samlDetails.getTrustCert(),
						samlDetails.getEntityId());
			} else {
				trustKeyStore = KeyStoreHelpers.importTrustCertificate(keyStore, samlDetails.getTrustCert());
			}

			byte[] keyStoreByte = KeyStoreHelpers.keyStore2Bytes(trustKeyStore, keyStoreLoader.getKeystorePassword());

			// store KeyStore content
			samlDetails.setKeystore(keyStoreByte);
		}
		return samlDetails;
	}

	public AppSamlDetailVO resolveMetaData(AppSamlDetailVO samlDetails, InputStream inputStream) throws Exception {
		X509Certificate trustCert = null;
		EntityDescriptor entityDescriptor;
		try {
			entityDescriptor = MetadataDescriptorUtil.getInstance().getEntityDescriptor(inputStream);
		} catch (IOException e) {
			log.error("metadata  file resolve error .", e);
			throw new Exception("metadata  file resolve error", e);
		}
		SPSSODescriptor sPSSODescriptor = entityDescriptor.getSPSSODescriptor(SAMLConstants.SAML20P_NS);
		String b64Encoder = sPSSODescriptor.getKeyDescriptors().get(0).getKeyInfo().getX509Datas().get(0)
				.getX509Certificates().get(0).getValue();

		trustCert = X509CertHelpers.loadCertFromB64Encoded(b64Encoder);

		samlDetails.setTrustCert(trustCert);
		samlDetails.setSpAcsUrl(sPSSODescriptor.getAssertionConsumerServices().get(0).getLocation());
		samlDetails.setEntityId(entityDescriptor.getEntityID());

		if (samlDetails.getIssuer() == null || samlDetails.getIssuer().equals("")) {
			samlDetails.setIssuer(entityDescriptor.getEntityID());
		}

		if (samlDetails.getAudience() == null || samlDetails.getAudience().equals("")) {
			samlDetails.setAudience(entityDescriptor.getEntityID());
		}

		log.info("SPSSODescriptor EntityID " + entityDescriptor.getEntityID());
		return samlDetails;
	}
}