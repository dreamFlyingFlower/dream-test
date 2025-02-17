package com.wy.test.protocol.extend.adapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.web.servlet.ModelAndView;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.entity.ExtraAttrs;
import com.wy.test.core.vo.AppVO;
import com.wy.test.core.web.HttpRequestAdapter;
import com.wy.test.protocol.authorize.endpoint.adapter.AbstractAuthorizeAdapter;

import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.http.HttpsTrust;
import lombok.extern.slf4j.Slf4j;

/**
 * https://exmail.qq.com/qy_mng_logic/doc exmail sso
 */
@Slf4j
public class ExtendQqExmailAdapter extends AbstractAuthorizeAdapter {

	// https://exmail.qq.com/qy_mng_logic/doc#10003
	static String TOKEN_URI = "https://api.exmail.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";

	// https://exmail.qq.com/qy_mng_logic/doc#10036
	static String AUTHKEY_URI = "https://api.exmail.qq.com/cgi-bin/service/get_login_url?access_token=%s&userid=%s";

	final static Cache<String, String> tokenCache =
			Caffeine.newBuilder().expireAfterWrite(7200, TimeUnit.SECONDS).build();

	AccountEntity account;

	@Override
	public Object generateInfo() {
		return null;
	}

	@Override
	public ModelAndView authorize(ModelAndView modelAndView) {
		HttpsTrust.trustAllCerts();

		AppVO details = app;
		// extraAttrs from Applications
		ExtraAttrs extraAttrs = null;
		if (details.getIsExtendAttr() == 1) {
			extraAttrs = new ExtraAttrs(details.getExtendAttr());
		}

		log.debug("Extra Attrs {}", extraAttrs);

		String accessToken = getToken(details.getPrincipal(), details.getCredentials());

		ExMailLoginUrl exMailLoginUrl = getLoginUrl(accessToken, userInfo.getUsername());

		if (exMailLoginUrl.errcode == 0) {
			modelAndView.addObject("redirect_uri", exMailLoginUrl.getLogin_url());
		} else {
			log.error("Exception code {} , message {} , mapping message {} ,", exMailLoginUrl.getErrcode(),
					exMailLoginUrl.getErrmsg(), exMailMsgMapper.get(exMailLoginUrl.getErrcode()));
			// remove accessToken
			tokenCache.invalidate(details.getPrincipal());
			modelAndView.addObject("errorCode", exMailLoginUrl.getErrcode());
			modelAndView.addObject("errorMessage", exMailMsgMapper.get(exMailLoginUrl.getErrcode()));
		}

		return modelAndView;
	}

	public String getToken(String corpid, String corpsecret) {
		String accessToken = tokenCache.getIfPresent(corpid);
		if (accessToken == null) {
			log.debug("corpid {} , corpsecret {}", corpid, corpsecret);
			log.debug("get token url {}", String.format(TOKEN_URI, corpid, corpsecret));
			String responseBody = new HttpRequestAdapter().get(String.format(TOKEN_URI, corpid, corpsecret), null);
			log.debug("Response Body {}", responseBody);
			Token token = JsonHelpers.read(responseBody, Token.class);
			if (token.getErrcode() == 0) {
				log.debug("access_token {}", token);
				accessToken = token.getAccess_token();
				tokenCache.put(corpid, accessToken);
			} else {
				log.debug("Error Code {}", exMailMsgMapper.get(token.getErrcode()));
				;
			}
		}
		return accessToken;
	}

	public ExMailLoginUrl getLoginUrl(String accessToken, String userId) {
		if (accessToken != null) {
			log.debug("userId {}", userId);
			String authKeyBody = new HttpRequestAdapter().get(String.format(AUTHKEY_URI, accessToken, userId), null);

			ExMailLoginUrl exMailLoginUrl = JsonHelpers.read(authKeyBody, ExMailLoginUrl.class);
			log.debug("LoginUrl {} ", exMailLoginUrl);
			return exMailLoginUrl;
		}
		return new ExMailLoginUrl(-1, "access_token is null .");
	}

	class ExMailMsg {

		protected long expires_in;

		protected String errmsg;

		protected Integer errcode;

		public ExMailMsg() {
		}

		public long getExpires_in() {
			return expires_in;
		}

		public void setExpires_in(long expires_in) {
			this.expires_in = expires_in;
		}

		public String getErrmsg() {
			return errmsg;
		}

		public void setErrmsg(String errmsg) {
			this.errmsg = errmsg;
		}

		public Integer getErrcode() {
			return errcode;
		}

		public void setErrcode(Integer errcode) {
			this.errcode = errcode;
		}
	}

	class Token extends ExMailMsg implements Serializable {

		private static final long serialVersionUID = 275756585220635542L;

		/**
		 * access_token
		 */
		private String access_token;

		public String getAccess_token() {
			return access_token;
		}

		public void setAccess_token(String access_token) {
			this.access_token = access_token;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Token [access_token=");
			builder.append(access_token);
			builder.append("]");
			return builder.toString();
		}

	}

	class ExMailLoginUrl extends ExMailMsg implements Serializable {

		private static final long serialVersionUID = 3033047757268214198L;

		private String login_url;

		public String getLogin_url() {
			return login_url;
		}

		public void setLogin_url(String login_url) {
			this.login_url = login_url;
		}

		public ExMailLoginUrl() {
		}

		public ExMailLoginUrl(Integer errcode, String errmsg) {
			super.errcode = errcode;
			super.errmsg = errmsg;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("LoginUrl [login_url=");
			builder.append(login_url);
			builder.append("]");
			return builder.toString();
		}
	}

	public static HashMap<Integer, String> exMailMsgMapper = new HashMap<Integer, String>();

	static {
		exMailMsgMapper.put(-1, "系统繁忙");
		exMailMsgMapper.put(0, "请求成功");
		exMailMsgMapper.put(40001, "获取access_token时CorpSecret错误，或者access_token无效");
		exMailMsgMapper.put(40003, "不合法的UserID");
		exMailMsgMapper.put(40013, "不合法的corpid");
		exMailMsgMapper.put(40014, "不合法的access_token");
		exMailMsgMapper.put(40057, "不合法的callbackurl或者callbackurl验证失败");
		exMailMsgMapper.put(40091, "无效secert");
		exMailMsgMapper.put(40092, "参数不合法");
		exMailMsgMapper.put(40093, "请求并发过大，请降低并发并重试");
		exMailMsgMapper.put(45009, "接口调用超过限制");
		exMailMsgMapper.put(45024, "帐号数量超过上限");
		exMailMsgMapper.put(50005, "企业已禁用");
		exMailMsgMapper.put(60001, "部门长度不符合限制");
		exMailMsgMapper.put(60002, "部门层级深度超过限制");
		exMailMsgMapper.put(60003, "部门不存在");
		exMailMsgMapper.put(60004, "父部门不存在");
		exMailMsgMapper.put(60005, "不允许删除有成员的部门");
		exMailMsgMapper.put(60006, "不允许删除有子部门的部门");
		exMailMsgMapper.put(60007, "不允许删除根部门");
		exMailMsgMapper.put(60008, "部门名称已存在");
		exMailMsgMapper.put(60009, "部门名称含有非法字符");
		exMailMsgMapper.put(60010, "部门存在循环关系");
		exMailMsgMapper.put(60102, "UserID已存在");
		exMailMsgMapper.put(60103, "手机号码不合法");
		exMailMsgMapper.put(60104, "不合法的position参数");
		exMailMsgMapper.put(60105, "部门ID数量超过上限");
		exMailMsgMapper.put(60106, "不合法的userlist参数");
		exMailMsgMapper.put(60111, "UserID不存在");
		exMailMsgMapper.put(60112, "成员姓名不合法");
		exMailMsgMapper.put(60114, "性别不合法");
		exMailMsgMapper.put(60115, "激活码格式错误");
		exMailMsgMapper.put(60116, "邮箱回收站内已存在相同的帐号");
		exMailMsgMapper.put(60118, "userid在企业微信重复");
		exMailMsgMapper.put(60119, "用户未绑定(删除、回收站状态)");
		exMailMsgMapper.put(60120, "密码和手机号不能同时为空");
		exMailMsgMapper.put(60123, "无效的部门id");
		exMailMsgMapper.put(60124, "无效的父部门id");
		exMailMsgMapper.put(60125, "非法部门名字，长度超过限制、重名等，重名包括与csv文件中同级部门重名或者与旧组织架构包含成员的同级部门重名");
		exMailMsgMapper.put(60126, "创建部门失败");
		exMailMsgMapper.put(60127, "缺少部门id");
		exMailMsgMapper.put(60128, "帐号已绑定手机或微信，需员工修改密码");
		exMailMsgMapper.put(60201, "不合法的标签id");
		exMailMsgMapper.put(60202, "缺少标签id");
		exMailMsgMapper.put(60203, "不合法的标签名");
		exMailMsgMapper.put(60204, "标签名已存在");
		exMailMsgMapper.put(60205, "所有参数都非法");
		exMailMsgMapper.put(60301, "不合法的type参数");
		exMailMsgMapper.put(60302, "不合法的option参数");
		exMailMsgMapper.put(600001, "Userid与别名冲突");
		exMailMsgMapper.put(600002, "Userid与Groupid冲突");
		exMailMsgMapper.put(600003, "无效密码或者是弱密码");
		exMailMsgMapper.put(600004, "别名无效");
		exMailMsgMapper.put(600005, "别名与userid或者Groupid冲突");
		exMailMsgMapper.put(600006, "别名数量达到上限");
		exMailMsgMapper.put(600007, "Groupid无效");
		exMailMsgMapper.put(600008, "邮件群组不存在");
		exMailMsgMapper.put(600009, "群组成员为空");
		exMailMsgMapper.put(600010, "Userlist无效，可能是个别成员无效");
		exMailMsgMapper.put(600011, "Grouplist无效，可能是个别成员无效");
		exMailMsgMapper.put(600012, "Partylist无效，可能是个别成员无效");
		exMailMsgMapper.put(600013, "群发权限类型无效");
		exMailMsgMapper.put(600014, "群发权限成员无效");
		exMailMsgMapper.put(600015, "邮件群组已存在");
		exMailMsgMapper.put(600016, "Userlist部分成员未找到");
		exMailMsgMapper.put(600017, "Partylist部分成员未找到");
		exMailMsgMapper.put(600018, "Grouplist部分成员未找到");
		exMailMsgMapper.put(600019, "邮件群组名称含有非法字符");
		exMailMsgMapper.put(600020, "邮件群组存在循环");
		exMailMsgMapper.put(600021, "邮件群组嵌套超过层数");
		exMailMsgMapper.put(600023, "群发权限成员缺失");
		exMailMsgMapper.put(600024, "Groupid与userid或者别名冲突");
		exMailMsgMapper.put(600025, "座机号码无效");
		exMailMsgMapper.put(600026, "编号无效");
		exMailMsgMapper.put(600027, "批量检查的成员数超过限额");
		exMailMsgMapper.put(600034, "不合法的fuzzy参数");
		exMailMsgMapper.put(600035, "不合法的业务邮箱账号");
		exMailMsgMapper.put(600036, "密码不允许修改");
		exMailMsgMapper.put(600037, "不合法的业务邮箱ID");
		exMailMsgMapper.put(600038, "不合法的业务邮箱名称");
		exMailMsgMapper.put(600039, "业务邮箱账号已存在");
		exMailMsgMapper.put(600040, "业务邮箱名称已存在");
		exMailMsgMapper.put(600041, "业务邮箱ID不存在");
		exMailMsgMapper.put(601001, "日志查询的时间无效");
		exMailMsgMapper.put(601002, "日志查询的时间超过限制");
		exMailMsgMapper.put(601003, "日志查询的域名无效");
		exMailMsgMapper.put(601004, "日志查询的域名不存在");
		exMailMsgMapper.put(601005, "不合法的mailtype参数");
		exMailMsgMapper.put(601006, "不合法的type参数");
		exMailMsgMapper.put(602005, "应用没有访问此API的权限");
		exMailMsgMapper.put(604001, "基础版企业无权限操作专业版功能");
		exMailMsgMapper.put(604002, "企业VIP人员达到上限");
		exMailMsgMapper.put(604003, "不合法的setvip参数");
	}
}
