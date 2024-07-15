package com.wy.test.sms.password.sms.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.wy.test.entity.UserInfo;
import com.wy.test.sms.password.sms.SmsOtpAuthn;

/**
 * 阿里云短信验证.
 * @author shimingxy
 *
 */
public class SmsOtpAuthnAliyun extends SmsOtpAuthn {
    private static final  Logger logger = LoggerFactory.getLogger(SmsOtpAuthnAliyun.class);
    
    public SmsOtpAuthnAliyun() {
        otpType = OtpTypes.SMS;
    }

    public SmsOtpAuthnAliyun(String accessKeyId, String accessSecret, String templateCode, String signName) {
    	otpType = OtpTypes.SMS;
		this.accessKeyId = accessKeyId;
		this.accessSecret = accessSecret;
		this.templateCode = templateCode;
		this.signName = signName;
	}
    
    //请替换你在管理后台应用下申请的accessKeyId
    private   String accessKeyId = "94395d754eb55693043f5d6a2b772ef3";
    //请替换你在管理后台应用下申请的accessSecret
    private  String accessSecret = "05d5485357bc";
    // 短信模板ID
    private  String templateCode = "SMS_187590021";
    
    private String signName = "MaxKey";
    


	@Override
	public boolean produce(UserInfo userInfo) {
        // 手机号
        String mobile = userInfo.getMobile();
        if (mobile != null && !mobile.equals("")) {
            try {
                DefaultProfile profile = DefaultProfile.getProfile(
                                    "cn-hangzhou", accessKeyId, accessSecret);
                IAcsClient client = new DefaultAcsClient(profile);

                String token = this.genToken(userInfo);
                CommonRequest request = new CommonRequest();
                request.setSysMethod(MethodType.POST);
                request.setSysDomain("dysmsapi.aliyuncs.com");
                request.setSysVersion("2017-05-25");
                request.setSysAction("SendSms");
                request.putQueryParameter("RegionId", "cn-hangzhou");
                request.putQueryParameter("PhoneNumbers", mobile);
                request.putQueryParameter("SignName", signName);
                request.putQueryParameter("TemplateCode", templateCode);
                request.putQueryParameter("TemplateParam", "{\"code\":\"" + token + "\"}");
                CommonResponse response = client.getCommonResponse(request);
                logger.debug("responseString " + response.getData());
                //成功返回
                if (response.getData().indexOf("OK") > -1) {
                    this.optTokenStore.store(
                            userInfo, 
                            token, 
                            userInfo.getMobile(), 
                            OtpTypes.SMS);
                    return true;
                }
            } catch  (Exception e) {
                logger.error(" produce code error ", e);
            } 
        }
        return false;
    }

    @Override
    public boolean validate(UserInfo userInfo, String token) {
        return this.optTokenStore.validate(userInfo, token, OtpTypes.SMS, interval);
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }
    
}
