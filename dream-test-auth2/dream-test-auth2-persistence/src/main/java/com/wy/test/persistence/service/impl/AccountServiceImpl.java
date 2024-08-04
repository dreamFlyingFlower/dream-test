package com.wy.test.persistence.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wy.test.core.constants.ConstStatus;
import com.wy.test.core.convert.AccountConvert;
import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.entity.AccountStrategyEntity;
import com.wy.test.core.entity.OrgCastEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.query.AccountQuery;
import com.wy.test.core.vo.AccountVO;
import com.wy.test.core.vo.UserVO;
import com.wy.test.persistence.mapper.AccountMapper;
import com.wy.test.persistence.provision.ProvisionAction;
import com.wy.test.persistence.provision.ProvisionService;
import com.wy.test.persistence.provision.ProvisionTopic;
import com.wy.test.persistence.service.AccountService;
import com.wy.test.persistence.service.AccountStrategyService;
import com.wy.test.persistence.service.OrgCastService;
import com.wy.test.persistence.service.UserService;

import dream.flying.flower.collection.CollectionHelper;
import dream.flying.flower.framework.mybatis.plus.service.impl.AbstractServiceImpl;
import dream.flying.flower.lang.StrHelper;
import lombok.AllArgsConstructor;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 用户账号表
 *
 * @author 飞花梦影
 * @date 2024-08-01
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Service
@AllArgsConstructor
public class AccountServiceImpl
		extends AbstractServiceImpl<AccountEntity, AccountVO, AccountQuery, AccountConvert, AccountMapper>
		implements AccountService {

	private final ProvisionService provisionService;

	private final UserService userInfoService;

	private final AccountStrategyService accountStrategyService;

	private final OrgCastService orgCastService;

	@Override
	public boolean insert(AccountEntity account) {
		if (super.save(account)) {
			if (provisionService.getDreamServerProperties().isProvision()) {
				UserVO loadUserInfo = userInfoService.findUserRelated(account.getUserId());
				AccountVO accountVO = baseConvert.convertt(account);
				accountVO.setUserInfo(loadUserInfo);
				accountVO.setOrgCast(orgCastService
						.list(new LambdaQueryWrapper<OrgCastEntity>().eq(OrgCastEntity::getProvider, account.getAppId())
								.eq(OrgCastEntity::getOrgId, loadUserInfo.getDepartmentId())));
				provisionService.send(ProvisionTopic.ACCOUNT_TOPIC, account, ProvisionAction.CREATE_ACTION);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean update(AccountEntity account) {
		if (super.updateById(account)) {
			if (provisionService.getDreamServerProperties().isProvision()) {
				UserVO loadUserInfo = userInfoService.findUserRelated(account.getUserId());
				AccountVO accountVO = baseConvert.convertt(account);
				accountVO.setUserInfo(loadUserInfo);
				accountVO.setOrgCast(orgCastService
						.list(new LambdaQueryWrapper<OrgCastEntity>().eq(OrgCastEntity::getProvider, account.getAppId())
								.eq(OrgCastEntity::getOrgId, loadUserInfo.getDepartment())));
				provisionService.send(ProvisionTopic.ACCOUNT_TOPIC, account, ProvisionAction.UPDATE_ACTION);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean updateStatus(AccountEntity accounts) {
		return this.baseMapper.updateStatus(accounts) > 0;
	}

	@Override
	public boolean remove(String id) {
		AccountEntity account = this.getById(id);
		AccountVO accountVO = baseConvert.convertt(account);
		if (super.removeById(id)) {
			UserVO loadUserInfo = null;
			if (provisionService.getDreamServerProperties().isProvision()) {
				loadUserInfo = userInfoService.findUserRelated(account.getUserId());
				accountVO.setUserInfo(loadUserInfo);
				provisionService.send(ProvisionTopic.ACCOUNT_TOPIC, account, ProvisionAction.DELETE_ACTION);
			}
			return true;
		}
		return false;
	}

	@Override
	public void refreshByStrategy(AccountStrategyEntity strategy) {
		if (StrHelper.isNotBlank(strategy.getOrgIds())) {
			strategy.setOrgIds("'" + strategy.getOrgIds().replace(",", "','") + "'");
		}
		List<UserEntity> userEntities = queryUserNotInStrategy(strategy);
		if (CollectionHelper.isEmpty(userEntities)) {
			return;
		}

		for (UserEntity user : userEntities) {
			AccountEntity account = new AccountEntity();
			account.setAppId(strategy.getAppId());
			account.setAppName(strategy.getAppName());

			account.setUserId(user.getId());
			account.setUsername(user.getUsername());
			account.setDisplayName(user.getDisplayName());
			account.setRelatedUsername(generateAccount(user, strategy));
			account.setRelatedPassword(PasswordReciprocal.getInstance().encode(userInfoService.randomPassword()));

			account.setInstId(strategy.getInstId());
			account.setCreateType("automatic");
			account.setStatus(ConstStatus.ACTIVE);
			account.setStrategyId(strategy.getId());

			insert(account);
		}
		deleteByStrategy(strategy);
	}

	@Override
	public void refreshAllByStrategy() {
		List<AccountStrategyEntity> accountStrategyEntities = accountStrategyService.list(
				new LambdaQueryWrapper<AccountStrategyEntity>().eq(AccountStrategyEntity::getCreateType, "automatic"));

		for (AccountStrategyEntity strategy : accountStrategyEntities) {
			refreshByStrategy(strategy);
		}
	}

	@Override
	public List<UserEntity> queryUserNotInStrategy(AccountStrategyEntity strategy) {
		return baseMapper.queryUserNotInStrategy(strategy);
	}

	@Override
	public long deleteByStrategy(AccountStrategyEntity strategy) {
		return baseMapper.deleteByStrategy(strategy);
	}

	@Override
	public List<AccountEntity> queryByAppIdAndDate(AccountEntity account) {
		return baseMapper.queryByAppIdAndDate(account);
	}

	@Override
	public List<AccountEntity> queryByAppIdAndAccount(String appId, String relatedUsername) {
		return baseMapper.queryByAppIdAndAccount(appId, relatedUsername);
	}

	@Override
	public String generateAccount(UserEntity userInfo, AccountStrategyEntity accountsStrategy) {
		String shortAccount = generateAccount(userInfo, accountsStrategy, true);
		String account = generateAccount(userInfo, accountsStrategy, false);
		String accountResult = shortAccount;
		List<AccountEntity> AccountsList = baseMapper.queryByAppIdAndAccount(accountsStrategy.getAppId(),
				shortAccount + accountsStrategy.getSuffixes());
		if (!AccountsList.isEmpty()) {
			if (accountsStrategy.getMapping().equalsIgnoreCase("email")) {
				accountResult = account;
				AccountsList = baseMapper.queryByAppIdAndAccount(accountsStrategy.getAppId(),
						account + accountsStrategy.getSuffixes());
			}
			if (!AccountsList.isEmpty()) {
				for (int i = 1; i < 100; i++) {
					accountResult = account + i;
					AccountsList = baseMapper.queryByAppIdAndAccount(accountsStrategy.getAppId(),
							accountResult + accountsStrategy.getSuffixes());
					if (AccountsList.isEmpty())
						break;
				}
			}
		}
		if (StrHelper.isNotBlank(accountsStrategy.getSuffixes())) {
			accountResult = accountResult + accountsStrategy.getSuffixes();
		}
		return accountResult;
	}

	private String generateAccount(UserEntity userInfo, AccountStrategyEntity strategy, boolean isShort) {
		String account = "";
		if (strategy.getMapping().equalsIgnoreCase("username")) {
			account = userInfo.getUsername();
		} else if (strategy.getMapping().equalsIgnoreCase("mobile")) {
			account = userInfo.getMobile();
		} else if (strategy.getMapping().equalsIgnoreCase("email")) {
			try {
				if (isShort) {
					account = getPinYinShortName(userInfo.getDisplayName());
				} else {
					account = getPinYinName(userInfo.getDisplayName());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (strategy.getMapping().equalsIgnoreCase("employeeNumber")) {
			account = userInfo.getEmployeeNumber();
		} else if (strategy.getMapping().equalsIgnoreCase("windowsAccount")) {
			account = userInfo.getWindowsAccount();
		} else if (strategy.getMapping().equalsIgnoreCase("idCardNo")) {
			account = userInfo.getIdCardNo();
		} else {
			account = userInfo.getUsername();
		}

		return account;
	}

	public static String getPinYinName(String name) throws BadHanyuPinyinOutputFormatCombination {
		HanyuPinyinOutputFormat pinyinFormat = new HanyuPinyinOutputFormat();
		pinyinFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		pinyinFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		pinyinFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		return PinyinHelper.toHanYuPinyinString(name, pinyinFormat, "", false);
	}

	public static String getPinYinShortName(String name) throws BadHanyuPinyinOutputFormatCombination {
		char[] strs = name.toCharArray();
		String pinyinName = "";
		for (int i = 0; i < strs.length; i++) {
			if (i == 0) {
				pinyinName += getPinYinName(strs[i] + "");
			} else {
				pinyinName += getPinYinName(strs[i] + "").charAt(0);
			}
		}
		return pinyinName;
	}
}