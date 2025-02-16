
package com.wy.test.web.core.controller;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.authentication.core.annotation.CurrentUser;
import com.wy.test.authentication.otp.onetimepwd.algorithm.OtpKeyUri;
import com.wy.test.authentication.otp.onetimepwd.algorithm.OtpSecret;
import com.wy.test.authentication.otp.onetimepwd.impl.TimeBasedOtpAuthn;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.persistence.service.UserService;

import dream.flying.flower.framework.core.qrcode.QrCodeHelpers;
import dream.flying.flower.framework.safe.helper.Base32Helpers;
import dream.flying.flower.helper.ImageHelper;
import lombok.SneakyThrows;

@Controller
@RequestMapping(value = { "/config" })
public class OneTimePasswordController {

	@Autowired
	private UserService userInfoService;

	@Autowired
	OtpKeyUri otpKeyUriFormat;

	@Autowired
	private TimeBasedOtpAuthn timeBasedOtpAuthn;

	@GetMapping(value = { "/timebased" })
	@ResponseBody
	@SneakyThrows
	public ResponseEntity<?> timebased(@RequestParam String generate, @CurrentUser UserEntity currentUser) {
		HashMap<String, Object> timebased = new HashMap<String, Object>();

		generate(generate, currentUser);

		String sharedSecret = PasswordReciprocal.getInstance().decoder(currentUser.getSharedSecret());

		otpKeyUriFormat.setSecret(sharedSecret);
		String otpauth = otpKeyUriFormat.format(currentUser.getUsername());
		byte[] byteSharedSecret = Base32Helpers.decode(sharedSecret);
		String hexSharedSecret = Hex.encodeHexString(byteSharedSecret);
		BufferedImage bufferedImage = QrCodeHelpers.createImage(otpauth, 300, 300, "gif");
		String rqCode = ImageHelper.encodeImage(bufferedImage);

		timebased.put("displayName", currentUser.getDisplayName());
		timebased.put("username", currentUser.getUsername());
		timebased.put("digits", otpKeyUriFormat.getDigits());
		timebased.put("period", otpKeyUriFormat.getPeriod());
		timebased.put("sharedSecret", sharedSecret);
		timebased.put("hexSharedSecret", hexSharedSecret);
		timebased.put("rqCode", rqCode);
		return new ResultResponse<HashMap<String, Object>>(timebased).buildResponse();
	}

	public void generate(String generate, @CurrentUser UserEntity currentUser) {
		if ((StringUtils.isNotBlank(generate) && generate.equalsIgnoreCase("YES"))
				|| StringUtils.isBlank(currentUser.getSharedSecret())) {

			byte[] byteSharedSecret = OtpSecret.generate(otpKeyUriFormat.getCrypto());
			String sharedSecret = Base32Helpers.encode(byteSharedSecret);
			sharedSecret = PasswordReciprocal.getInstance().encode(sharedSecret);
			currentUser.setSharedSecret(sharedSecret);
			userInfoService.updateSharedSecret(currentUser);
		}
	}

	@SuppressWarnings("unused")
	@GetMapping("/verify")
	public ResponseEntity<?> verify(@RequestParam("otp") String otp, @CurrentUser UserEntity currentUser) {
		// 从当前用户信息中获取共享密钥
		String sharedSecret = PasswordReciprocal.getInstance().decoder(currentUser.getSharedSecret());
		// 计算当前时间对应的动态密码
		boolean validate = timeBasedOtpAuthn.validate(currentUser, otp);
		if (validate) {
			return new ResultResponse<>(0, "One-Time Password verification succeeded").buildResponse();
		} else {
			return new ResultResponse<>(2, "One-Time Password verification failed").buildResponse();
		}
	}
}