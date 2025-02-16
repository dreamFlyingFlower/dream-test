package com.wy.test.protocol.cas.endpoint;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wy.test.authentication.core.entity.SignPrincipal;
import com.wy.test.core.web.HttpResponseConstants;
import com.wy.test.protocol.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.protocol.cas.endpoint.response.ProxyServiceResponseBuilder;
import com.wy.test.protocol.cas.endpoint.response.ServiceResponseBuilder;
import com.wy.test.protocol.cas.endpoint.ticket.CasConstants;
import com.wy.test.protocol.cas.endpoint.ticket.ProxyGrantingTicketIOUImpl;
import com.wy.test.protocol.cas.endpoint.ticket.ProxyGrantingTicketImpl;
import com.wy.test.protocol.cas.endpoint.ticket.ProxyTicketImpl;
import com.wy.test.protocol.cas.endpoint.ticket.Ticket;

import dream.flying.flower.enums.BooleanEnum;
import dream.flying.flower.lang.StrHelper;
import dream.flying.flower.reflect.ReflectHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * https://apereo.github.io/cas/6.2.x/protocol/CAS-Protocol-Specification.html
 */
@Tag(name = "2-3-CAS API文档模块")
@Controller
@Slf4j
public class Cas30AuthorizeEndpoint extends CasBaseAuthorizeEndpoint {

	@Operation(summary = "CAS 3.0 ticket验证接口", description = "通过ticket获取当前登录用户信息", method = "POST")
	@GetMapping(value = CasConstants.ENDPOINT.ENDPOINT_SERVICE_VALIDATE_V3)
	public void serviceValidate(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = CasConstants.PARAMETER.TICKET) String ticket,
			@RequestParam(value = CasConstants.PARAMETER.SERVICE) String service,
			@RequestParam(value = CasConstants.PARAMETER.PROXY_CALLBACK_URL, required = false) String pgtUrl,
			@RequestParam(value = CasConstants.PARAMETER.RENEW, required = false) String renew,
			@RequestParam(value = CasConstants.PARAMETER.FORMAT, required = false,
					defaultValue = HttpResponseConstants.FORMAT_TYPE.XML) String format) {
		log.debug("serviceValidate " + " ticket " + ticket + " , service " + service + " , pgtUrl " + pgtUrl
				+ " , renew " + renew + " , format " + format);

		Ticket storedTicket = null;
		if (ticket.startsWith(CasConstants.PREFIX.SERVICE_TICKET_PREFIX)) {
			try {
				storedTicket = ticketServices.consumeTicket(ticket);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();

		if (storedTicket != null) {
			SignPrincipal authentication = ((SignPrincipal) storedTicket.getAuthentication().getPrincipal());
			if (StrHelper.isNotBlank(pgtUrl)) {
				ProxyGrantingTicketIOUImpl proxyGrantingTicketIOUImpl = new ProxyGrantingTicketIOUImpl();
				String proxyGrantingTicketIOU = casProxyGrantingTicketServices.createTicket(proxyGrantingTicketIOUImpl);

				ProxyGrantingTicketImpl proxyGrantingTicketImpl =
						new ProxyGrantingTicketImpl(storedTicket.getAuthentication(), storedTicket.getCasDetails());
				String proxyGrantingTicket = casProxyGrantingTicketServices.createTicket(proxyGrantingTicketImpl);

				serviceResponseBuilder.success().setTicket(proxyGrantingTicketIOU);
				serviceResponseBuilder.success().setProxy(pgtUrl);

				httpRequestAdapter.post(pgtUrl + "?pgtId=" + proxyGrantingTicket + "&pgtIou=" + proxyGrantingTicketIOU,
						null);
			}

			if (BooleanEnum.isTrue(storedTicket.getCasDetails().getIsAdapter())) {
				try {
					Object samlAdapter = ReflectHelper.newInstance(storedTicket.getCasDetails().getAdapterClass());
					BeanUtils.setProperty(samlAdapter, "serviceResponseBuilder", serviceResponseBuilder);
					AbstractAuthorizeAdapter adapter = (AbstractAuthorizeAdapter) samlAdapter;
					adapter.setPrincipal(authentication);
					adapter.setApp(storedTicket.getCasDetails());
					adapter.generateInfo();
				} catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException
						| NoSuchMethodException | SecurityException | InstantiationException
						| IllegalArgumentException e) {
					log.error("setProperty error . ", e);
				}
			}
		} else {
			serviceResponseBuilder.failure().setCode(CasConstants.ERROR_CODE.INVALID_TICKET)
					.setDescription("Ticket " + ticket + " not recognized");
		}

		httpResponseAdapter.write(response, serviceResponseBuilder.serviceResponseBuilder(), format);
	}

	@Operation(summary = "CAS 3.0 ProxyTicket代理验证接口", description = "通过ProxyGrantingTicket获取ProxyTicket",
			method = "POST")
	@GetMapping(CasConstants.ENDPOINT.ENDPOINT_PROXY_V3)
	public void proxy(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = CasConstants.PARAMETER.PROXY_GRANTING_TICKET) String pgt,
			@RequestParam(value = CasConstants.PARAMETER.TARGET_SERVICE) String targetService,
			@RequestParam(value = CasConstants.PARAMETER.FORMAT, required = false,
					defaultValue = HttpResponseConstants.FORMAT_TYPE.XML) String format) {
		log.debug("proxy " + " pgt " + pgt + " , targetService " + targetService + " , format " + format);
		ProxyServiceResponseBuilder proxyServiceResponseBuilder = new ProxyServiceResponseBuilder();
		ProxyGrantingTicketImpl proxyGrantingTicketImpl =
				(ProxyGrantingTicketImpl) casProxyGrantingTicketServices.get(pgt);
		if (proxyGrantingTicketImpl != null) {
			ProxyTicketImpl ProxyTicketImpl = new ProxyTicketImpl(proxyGrantingTicketImpl.getAuthentication(),
					proxyGrantingTicketImpl.getCasDetails());
			String proxyTicket = ticketServices.createTicket(ProxyTicketImpl);
			proxyServiceResponseBuilder.success().setTicket(proxyTicket).setFormat(format);
		} else {
			proxyServiceResponseBuilder.success().setTicket("").setFormat(format);
		}

		httpResponseAdapter.write(response, proxyServiceResponseBuilder.serviceResponseBuilder(), format);
	}

	@Operation(summary = "CAS 3.0 ticket代理验证接口", description = "通过ProxyTicket获取当前登录用户信息", method = "POST")
	@GetMapping(CasConstants.ENDPOINT.ENDPOINT_PROXY_VALIDATE_V3)
	public void proxy(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = CasConstants.PARAMETER.TICKET) String ticket,
			@RequestParam(value = CasConstants.PARAMETER.SERVICE) String service,
			@RequestParam(value = CasConstants.PARAMETER.PROXY_CALLBACK_URL, required = false) String pgtUrl,
			@RequestParam(value = CasConstants.PARAMETER.RENEW, required = false) String renew,
			@RequestParam(value = CasConstants.PARAMETER.FORMAT, required = false,
					defaultValue = HttpResponseConstants.FORMAT_TYPE.XML) String format) {
		log.debug("proxyValidate " + " ticket " + ticket + " , service " + service + " , pgtUrl " + pgtUrl + " , renew "
				+ renew + " , format " + format);

		Ticket storedTicket = null;
		if (ticket.startsWith(CasConstants.PREFIX.PROXY_TICKET_PREFIX)) {
			try {
				storedTicket = ticketServices.consumeTicket(ticket);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		ServiceResponseBuilder serviceResponseBuilder = new ServiceResponseBuilder();

		if (storedTicket != null) {
			SignPrincipal authentication = ((SignPrincipal) storedTicket.getAuthentication().getPrincipal());
			if (BooleanEnum.isTrue(storedTicket.getCasDetails().getIsAdapter())) {
				try {
					Object samlAdapter = ReflectHelper.newInstance(storedTicket.getCasDetails().getAdapterClass());
					BeanUtils.setProperty(samlAdapter, "serviceResponseBuilder", serviceResponseBuilder);
					AbstractAuthorizeAdapter adapter = (AbstractAuthorizeAdapter) samlAdapter;
					adapter.setPrincipal(authentication);
					adapter.setApp(storedTicket.getCasDetails());
					adapter.generateInfo();
				} catch (IllegalAccessException | InvocationTargetException | ClassNotFoundException
						| NoSuchMethodException | SecurityException | InstantiationException
						| IllegalArgumentException e) {
					log.error("setProperty error . ", e);
				}
			}
		} else {
			serviceResponseBuilder.failure().setCode(CasConstants.ERROR_CODE.INVALID_TICKET)
					.setDescription("Ticket " + ticket + " not recognized");
		}
		httpResponseAdapter.write(response, serviceResponseBuilder.serviceResponseBuilder(), format);
	}
}
