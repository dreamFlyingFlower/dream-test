package dream.flying.flower.framework.web.helper;

import java.util.HashMap;
import java.util.Map;

import dream.flying.flower.framework.core.http.HttpClientHelpers;
import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.lang.StrHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 获取真实地址
 *
 * @author 飞花梦影
 * @date 2023-08-08 14:54:33
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
@Slf4j
public class AddressHelpers {

	/** 实时查询地址 */
	public static final String ADDRESS_URL = "https://whois.pconline.com.cn/ipJson.jsp";

	public static final String UNKNOWN = "未知";

	public static String getAddressByIP(String ip) {
		if (IpHelpers.internalIp(ip)) {
			return "内网IP";
		}

		try {
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("ip", ip);
			paramMap.put("json", true);
			String response = HttpClientHelpers.get(ADDRESS_URL, paramMap);
			if (StrHelper.isBlank(response)) {
				log.error("根据IP获取地址异常 {}", ip);
				return UNKNOWN;
			}

			Address address = JsonHelpers.read(response, Address.class);
			return String.format("%s %s", address.getProvince(), address.getCity());
		} catch (Exception e) {
			e.printStackTrace();
			log.error("根据IP获取地址异常 {}", ip);
		}

		return UNKNOWN;
	}

	@Data
	static class Address {

		/**
		 * 省
		 */
		private String province;

		/**
		 * 市
		 */
		private String city;
	}
}