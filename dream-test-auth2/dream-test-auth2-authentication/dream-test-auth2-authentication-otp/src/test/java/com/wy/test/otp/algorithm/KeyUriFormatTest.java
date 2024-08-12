package com.wy.test.otp.algorithm;

import java.io.File;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.wy.test.authentication.otp.password.onetimepwd.algorithm.OtpKeyUri;

import dream.flying.flower.framework.core.qrcode.QrCodeHelpers;

public class KeyUriFormatTest {

	public static void main(String[] args) {
		try {
			OtpKeyUri kuf =
					new OtpKeyUri(OtpKeyUri.Types.TOTP, "GIWVWOL7EI5WLVZPDMROEPSTFBEVO77Q", "connsec.com");
			kuf.setPeriod(60);
			String path = "D:\\totp.png";
			BitMatrix byteMatrix;
			byteMatrix = new MultiFormatWriter().encode(new String(kuf.format("shiming").getBytes("GBK"), "iso-8859-1"),
					BarcodeFormat.QR_CODE, 300, 300);
			File file = new File(path);
			QrCodeHelpers.encodeToFile(byteMatrix, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}