package com.wy.test.captcha.web.contorller;

import java.awt.image.BufferedImage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.code.kaptcha.Producer;
import com.wy.test.common.entity.Message;
import com.wy.test.core.authn.jwt.AuthTokenService;
import com.wy.test.core.persistence.cache.MomentaryService;

import dream.flying.flower.helper.ImageHelper;

/**
 * ImageCaptchaEndpoint Producer captcha.
 * 
 */
@Controller
public class ImageCaptchaEndpoint {

	private static final Logger _logger = LoggerFactory.getLogger(ImageCaptchaEndpoint.class);

	@Autowired
	private Producer captchaProducer;

	@Autowired
	protected MomentaryService momentaryService;

	@Autowired
	AuthTokenService authTokenService;

	/**
	 * captcha image Producer.
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 */
	@GetMapping(value = { "/captcha" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> captchaHandleRequest(
			@RequestParam(value = "captcha", required = false, defaultValue = "text") String captchaType,
			@RequestParam(value = "state", required = false, defaultValue = "state") String state) {
		try {
			String kaptchaText = captchaProducer.createText();
			String kaptchaValue = kaptchaText;
			if (captchaType.equalsIgnoreCase("Arithmetic")) {
				Integer minuend = Integer.valueOf(kaptchaText.substring(0, 1));
				Integer subtrahend = Integer.valueOf(kaptchaText.substring(1, 2));
				if (minuend - subtrahend > 0) {
					kaptchaValue = (minuend - subtrahend) + "";
					kaptchaText = minuend + "-" + subtrahend + "=?";
				} else {
					kaptchaValue = (minuend + subtrahend) + "";
					kaptchaText = minuend + "+" + subtrahend + "=?";
				}
			}
			String kaptchaKey = "";
			if (StringUtils.isNotBlank(state) && !state.equalsIgnoreCase("state")
					&& authTokenService.validateJwtToken(state)) {
				// just validate state Token
			} else {
				state = authTokenService.genRandomJwt();
			}
			kaptchaKey = authTokenService.resolveJWTID(state);
			_logger.trace("kaptchaKey {} , Captcha Text is {}", kaptchaKey, kaptchaValue);

			momentaryService.put("", kaptchaKey, kaptchaValue);
			// create the image with the text
			BufferedImage bufferedImage = captchaProducer.createImage(kaptchaText);
			String b64Image = ImageHelper.encodeImage(bufferedImage);

			_logger.trace("b64Image {}", b64Image);

			return new Message<ImageCaptcha>(new ImageCaptcha(state, b64Image)).buildResponse();
		} catch (Exception e) {
			_logger.error("captcha Producer Error " + e.getMessage());
		}
		return new Message<Object>(Message.FAIL).buildResponse();
	}

	public void setCaptchaProducer(Producer captchaProducer) {
		this.captchaProducer = captchaProducer;
	}
}