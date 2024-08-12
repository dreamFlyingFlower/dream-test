package com.wy.test.core.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fhs.core.trans.vo.TransPojo;
import com.wy.test.core.constant.ConstServiceMessage;
import com.wy.test.core.exception.PasswordPolicyException;
import com.wy.test.core.web.WebContext;

import dream.flying.flower.framework.web.valid.ValidEdit;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 密码策略
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "密码策略")
public class PasswordPolicyVO implements Serializable, TransPojo {

	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	@NotNull(message = "id不能为空", groups = { ValidEdit.class })
	private String id;

	@Schema(description = "最小长度")
	private Integer minLength;

	@Schema(description = "最大长度")
	private Integer maxLength;

	@Schema(description = "小写")
	private Integer lowerCase;

	@Schema(description = "大写")
	private Integer upperCase;

	@Schema(description = "数字")
	private Integer digits;

	@Schema(description = "特殊单词")
	private Integer specialChar;

	@Schema(description = "登录是否锁定")
	private Integer attempts;

	@Schema(description = "锁定时长")
	private Integer duration;

	@Schema(description = "密码有效期")
	private Integer expiration;

	@Schema(description = "用户名")
	private Integer username;

	@Schema(description = "历史密码")
	private Integer history;

	@Schema(description = "字典")
	private Integer dictionary;

	@Schema(description = "字母")
	private Integer alphabetical;

	@Schema(description = "数字")
	private Integer numerical;

	@Schema(description = "标准键盘")
	private Integer qwerty;

	@Schema(description = "事件")
	private Integer occurances;

	@Schema(description = "随机密码长度")
	private Integer randomPasswordLength;

	@Schema(description = "机构ID")
	private String instId;

	@Schema(description = "策略消息")
	List<String> policyMessages;

	public void buildMessage() {
		if (policyMessages == null) {
			policyMessages = new ArrayList<>();
		}
		String msg;
		if (minLength != 0) {
			// msg = "新密码长度为"+minLength+"-"+maxLength+"位";
			msg = WebContext.getI18nValue("PasswordPolicy.TOO_SHORT", new Object[] { minLength });
			policyMessages.add(msg);
		}
		if (maxLength != 0) {
			// msg = "新密码长度为"+minLength+"-"+maxLength+"位";
			msg = WebContext.getI18nValue("PasswordPolicy.TOO_LONG", new Object[] { maxLength });
			policyMessages.add(msg);
		}

		if (lowerCase > 0) {
			// msg = "新密码至少需要包含"+lowerCase+"位【a-z】小写字母";
			msg = WebContext.getI18nValue("PasswordPolicy.INSUFFICIENT_LOWERCASE", new Object[] { lowerCase });
			policyMessages.add(msg);
		}

		if (upperCase > 0) {
			// msg = "新密码至少需要包含"+upperCase+"位【A-Z】大写字母";
			msg = WebContext.getI18nValue("PasswordPolicy.INSUFFICIENT_UPPERCASE", new Object[] { upperCase });
			policyMessages.add(msg);
		}

		if (digits > 0) {
			// msg = "新密码至少需要包含"+digits+"位【0-9】阿拉伯数字";
			msg = WebContext.getI18nValue("PasswordPolicy.INSUFFICIENT_DIGIT", new Object[] { digits });
			policyMessages.add(msg);
		}

		if (specialChar > 0) {
			// msg = "新密码至少需要包含"+specialChar+"位特殊字符";
			msg = WebContext.getI18nValue("PasswordPolicy.INSUFFICIENT_SPECIAL", new Object[] { specialChar });
			policyMessages.add(msg);
		}

		if (expiration > 0) {
			// msg = "新密码有效期为"+expiration+"天";
			msg = WebContext.getI18nValue("PasswordPolicy.INSUFFICIENT_EXPIRES_DAY", new Object[] { expiration });
			policyMessages.add(msg);
		}
	}

	public void check(String username, String newPassword, String oldPassword) throws PasswordPolicyException {
		if ((1 == this.getUsername()) && newPassword.toLowerCase().contains(username.toLowerCase())) {
			throw new PasswordPolicyException(ConstServiceMessage.PASSWORDPOLICY.XW00000001);
		}
		if (oldPassword != null && newPassword.equalsIgnoreCase(oldPassword)) {
			throw new PasswordPolicyException(ConstServiceMessage.PASSWORDPOLICY.XW00000002);
		}
		if (newPassword.length() < this.getMinLength()) {
			throw new PasswordPolicyException(ConstServiceMessage.PASSWORDPOLICY.XW00000003, this.getMinLength());
		}
		if (newPassword.length() > this.getMaxLength()) {
			throw new PasswordPolicyException(ConstServiceMessage.PASSWORDPOLICY.XW00000004, this.getMaxLength());
		}
		int numCount = 0, upperCount = 0, lowerCount = 0, spacil = 0;
		char[] chPwd = newPassword.toCharArray();
		for (int i = 0; i < chPwd.length; i++) {
			char ch = chPwd[i];
			if (Character.isDigit(ch)) {
				numCount++;
				continue;
			}
			if (Character.isLowerCase(ch)) {
				lowerCount++;
				continue;
			}
			if (Character.isUpperCase(ch)) {
				upperCount++;
				continue;
			}
			spacil++;
		}
		if (numCount < this.getDigits()) {
			throw new PasswordPolicyException(ConstServiceMessage.PASSWORDPOLICY.XW00000005, this.getDigits());
		}
		if (lowerCount < this.getLowerCase()) {
			throw new PasswordPolicyException(ConstServiceMessage.PASSWORDPOLICY.XW00000006, this.getLowerCase());
		}
		if (upperCount < this.getUpperCase()) {
			throw new PasswordPolicyException(ConstServiceMessage.PASSWORDPOLICY.XW00000007, this.getUpperCase());
		}
		if (spacil < this.getSpecialChar()) {
			throw new PasswordPolicyException(ConstServiceMessage.PASSWORDPOLICY.XW00000008, this.getSpecialChar());
		}
	}
}