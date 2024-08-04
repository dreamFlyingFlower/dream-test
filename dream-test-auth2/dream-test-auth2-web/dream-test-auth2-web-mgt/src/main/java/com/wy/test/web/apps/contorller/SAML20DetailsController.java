package com.wy.test.web.apps.contorller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml2.metadata.SPSSODescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.wy.test.authz.saml20.metadata.MetadataDescriptorUtil;
import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.constants.ConstProtocols;
import com.wy.test.core.entity.AppSamlDetailEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.properties.DreamAuthServerProperties;
import com.wy.test.persistence.service.AppsSaml20DetailsService;

import dream.flying.flower.framework.web.crypto.ReciprocalHelpers;
import dream.flying.flower.framework.web.crypto.cert.X509CertHelpers;
import dream.flying.flower.framework.web.crypto.keystore.KeyStoreHelpers;
import dream.flying.flower.framework.web.crypto.keystore.KeyStoreLoader;

@Controller
@RequestMapping(value = { "/apps/saml20" })
public class SAML20DetailsController extends BaseAppContorller {

	final static Logger _logger = LoggerFactory.getLogger(SAML20DetailsController.class);

	@Autowired
	private KeyStoreLoader keyStoreLoader;

	@Autowired
	AppsSaml20DetailsService saml20DetailsService;

	@Autowired
	DreamAuthServerProperties dreamServerProperties;

	@GetMapping(value = { "/init" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> init() {
		AppSamlDetailEntity saml20Details = new AppSamlDetailEntity();
		saml20Details.setSecret(ReciprocalHelpers.generateKey(""));
		saml20Details.setProtocol(ConstProtocols.SAML20);
		saml20Details.setId(saml20Details.generateId());
		return new Message<AppSamlDetailEntity>(saml20Details).buildResponse();
	}

	@GetMapping(value = { "/get/{id}" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		AppSamlDetailEntity saml20Details = saml20DetailsService.getAppDetails(id, false);
		decoderSecret(saml20Details);
		saml20Details.transIconBase64();
		// modelAndView.addObject("authzURI",applicationConfig.getAuthzUri());
		return new Message<AppSamlDetailEntity>(saml20Details).buildResponse();
	}

	@ResponseBody
	@PostMapping(value = { "/add" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> add(@RequestBody AppSamlDetailEntity saml20Details, @CurrentUser UserEntity currentUser) {
		_logger.debug("-Add  :" + saml20Details);

		try {
			transform(saml20Details);
		} catch (Exception e) {
			e.printStackTrace();
		}
		saml20Details.setInstId(currentUser.getInstId());
		saml20DetailsService.insert(saml20Details);
		if (appsService.insertApp(saml20Details)) {
			return new Message<AppSamlDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppSamlDetailEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/update" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@RequestBody AppSamlDetailEntity saml20Details, @CurrentUser UserEntity currentUser) {
		_logger.debug("-update  :" + saml20Details);
		try {
			transform(saml20Details);
		} catch (Exception e) {
			e.printStackTrace();
		}
		saml20Details.setInstId(currentUser.getInstId());
		saml20DetailsService.update(saml20Details);
		if (appsService.updateApp(saml20Details)) {
			return new Message<AppSamlDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppSamlDetailEntity>(Message.FAIL).buildResponse();
		}
	}

	@ResponseBody
	@PostMapping(value = { "/delete" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> delete(@RequestParam("ids") String ids, @CurrentUser UserEntity currentUser) {
		_logger.debug("-delete  ids : {} ", ids);
		if (saml20DetailsService.deleteBatch(ids) && appsService.deleteBatch(ids)) {
			return new Message<AppSamlDetailEntity>(Message.SUCCESS).buildResponse();
		} else {
			return new Message<AppSamlDetailEntity>(Message.FAIL).buildResponse();
		}
	}

	protected AppSamlDetailEntity transform(AppSamlDetailEntity samlDetails) throws Exception {
		super.transform(samlDetails);
		ByteArrayInputStream bArrayInputStream = null;
		if (StringUtils.isNotBlank(samlDetails.getMetaFileId())) {
			bArrayInputStream =
					new ByteArrayInputStream(fileUploadService.get(samlDetails.getMetaFileId()).getUploaded());
			fileUploadService.remove(samlDetails.getMetaFileId());
		}

		if (StringUtils.isNotBlank(samlDetails.getFileType())) {
			if (samlDetails.getFileType().equals("certificate")) {// certificate file
				try {
					if (bArrayInputStream != null) {
						samlDetails.setTrustCert(X509CertHelpers.loadCertFromInputStream(bArrayInputStream));
					}
				} catch (IOException e) {
					_logger.error("read certificate file error .", e);
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
			samlDetails.setKeyStore(keyStoreByte);
		}
		return samlDetails;
	}

	public AppSamlDetailEntity resolveMetaData(AppSamlDetailEntity samlDetails, InputStream inputStream) throws Exception {
		X509Certificate trustCert = null;
		EntityDescriptor entityDescriptor;
		try {
			entityDescriptor = MetadataDescriptorUtil.getInstance().getEntityDescriptor(inputStream);
		} catch (IOException e) {
			_logger.error("metadata  file resolve error .", e);
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

		_logger.info("SPSSODescriptor EntityID " + entityDescriptor.getEntityID());
		return samlDetails;
	}

}
