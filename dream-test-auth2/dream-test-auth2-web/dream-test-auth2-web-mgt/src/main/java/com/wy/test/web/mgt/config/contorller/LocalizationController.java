package com.wy.test.web.mgt.config.contorller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authentication.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.LocalizationEntity;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserAdjunctEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.persistence.repository.LocalizationRepository;

import dream.flying.flower.generator.GeneratorStrategyContext;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/localization" })
@Slf4j
public class LocalizationController {

	@Autowired
	private LocalizationRepository localizationRepository;

	/**
	 * 读取
	 * 
	 * @return
	 */
	@GetMapping(value = { "/forward/{property}" })
	public ModelAndView forward(@PathVariable("property") String property, @CurrentUser UserEntity currentUser) {
		LocalizationEntity localization = localizationRepository.get(property, currentUser.getInstId());
		if (localization == null)
			localization = new LocalizationEntity();
		localization.setProperty(property);
		localization.setInstId(currentUser.getInstId());
		return new ModelAndView("localization/updateLocalization", "model", localization);
	}

	/**
	 * 更新
	 * 
	 * @param sysConfig
	 * @return
	 */
	@PostMapping(value = { "/update" })
	@ResponseBody
	public ResponseEntity<?> update(@ModelAttribute("localization") LocalizationEntity localization,
			@CurrentUser UserEntity currentUser, BindingResult result) {
		log.debug("update  localization : " + localization);
		localization.setInstId(currentUser.getInstId());
		if (StringUtils.isBlank(localization.getId())) {
			GeneratorStrategyContext generatorStrategyContext = new GeneratorStrategyContext();
			localization.setId(generatorStrategyContext.generate());
			if (localizationRepository.insert(localization)) {
				return new Message<UserAdjunctEntity>(Message.SUCCESS).buildResponse();
			} else {
				return new Message<UserAdjunctEntity>(Message.FAIL).buildResponse();
			}
		} else {
			if (localizationRepository.update(localization)) {
				return new Message<UserAdjunctEntity>(Message.SUCCESS).buildResponse();
			} else {
				return new Message<UserAdjunctEntity>(Message.FAIL).buildResponse();
			}
		}
	}
}