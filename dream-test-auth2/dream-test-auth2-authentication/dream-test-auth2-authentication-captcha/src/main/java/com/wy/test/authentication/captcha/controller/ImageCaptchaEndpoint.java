package com.wy.test.authentication.captcha.controller;

import java.awt.image.BufferedImage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.Producer;
import com.wy.test.authentication.core.jwt.AuthTokenService;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.cache.MomentaryService;

import dream.flying.flower.helper.ImageHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 验证码生产者
 * 
 * @author 飞花梦影
 * @date 2024-08-08 11:31:55
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Tag(name = "1-12 验证码API")
@Slf4j
@RestController
@AllArgsConstructor
public class ImageCaptchaEndpoint {

	final Producer captchaProducer;

	final MomentaryService momentaryService;

	final AuthTokenService authTokenService;

	/**
	 * captcha image Producer.
	 * 
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 */
	@Operation(summary = "获取验证码", description = "查询列表", method = "GET")
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
			log.trace("kaptchaKey {} , Captcha Text is {}", kaptchaKey, kaptchaValue);

			momentaryService.put("", kaptchaKey, kaptchaValue);
			// create the image with the text
			BufferedImage bufferedImage = captchaProducer.createImage(kaptchaText);
			String b64Image = ImageHelper.encodeImage(bufferedImage);

			log.trace("b64Image {}", b64Image);

			return new ResultResponse<ImageCaptcha>(new ImageCaptcha(state, b64Image)).buildResponse();
		} catch (Exception e) {
			log.error("captcha Producer Error " + e.getMessage());
		}
		return new ResultResponse<Object>(ResultResponse.FAIL).buildResponse();
	}
}