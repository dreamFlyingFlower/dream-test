package com.wy.test.otp.algorithm;

import java.io.File;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.wy.test.otp.password.onetimepwd.algorithm.OtpKeyUriFormat;
import com.wy.test.util.QRCode;

public class KeyUriFormatTest {

	public static void main(String[] args) {
		try {
			OtpKeyUriFormat kuf =
					new OtpKeyUriFormat(OtpKeyUriFormat.Types.TOTP, "GIWVWOL7EI5WLVZPDMROEPSTFBEVO77Q", "connsec.com");
			kuf.setPeriod(60);
			String path = "D:\\totp.png";
			BitMatrix byteMatrix;
			byteMatrix = new MultiFormatWriter().encode(new String(kuf.format("shiming").getBytes("GBK"), "iso-8859-1"),
					BarcodeFormat.QR_CODE, 300, 300);
			File file = new File(path);

			QRCode.writeToPath(byteMatrix, "png", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
