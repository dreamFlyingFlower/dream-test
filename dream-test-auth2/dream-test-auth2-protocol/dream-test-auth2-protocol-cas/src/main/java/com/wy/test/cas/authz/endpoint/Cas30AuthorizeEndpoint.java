package com.wy.test.cas.authz.endpoint;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wy.test.authorize.endpoint.adapter.AbstractAuthorizeAdapter;
import com.wy.test.cas.authz.endpoint.response.ProxyServiceResponseBuilder;
import com.wy.test.cas.authz.endpoint.response.ServiceResponseBuilder;
import com.wy.test.cas.authz.endpoint.ticket.CasConstants;
import com.wy.test.cas.authz.endpoint.ticket.ProxyGrantingTicketIOUImpl;
import com.wy.test.cas.authz.endpoint.ticket.ProxyGrantingTicketImpl;
import com.wy.test.cas.authz.endpoint.ticket.ProxyTicketImpl;
import com.wy.test.cas.authz.endpoint.ticket.Ticket;
import com.wy.test.constants.ConstsBoolean;
import com.wy.test.core.authn.SignPrincipal;
import com.wy.test.util.Instance;
import com.wy.test.util.StringUtils;
import com.wy.test.web.HttpResponseConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author Crystal.Sea https://apereo.github.io/cas/6.2.x/protocol/CAS-Protocol-Specification.html
 */
@Tag(name = "2-3-CAS API文档模块")
@Controller
public class Cas30AuthorizeEndpoint extends CasBaseAuthorizeEndpoint {

	final static Logger _logger = LoggerFactory.getLogger(Cas30AuthorizeEndpoint.class);

	@Operation(summary = "CAS 3.0 ticket验证接口", description = "通过ticket获取当前登录用户信息", method = "POST")
	@RequestMapping(value = CasConstants.ENDPOINT.ENDPOINT_SERVICE_VALIDATE_V3)
	public void serviceValidate(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = CasConstants.PARAMETER.TICKET) String ticket,
			@RequestParam(value = CasConstants.PARAMETER.SERVICE) String service,
			@RequestParam(value = CasConstants.PARAMETER.PROXY_CALLBACK_URL, required = false) String pgtUrl,
			@RequestParam(value = CasConstants.PARAMETER.RENEW, required = false) String renew,
			@RequestParam(value = CasConstants.PARAMETER.FORMAT, required = false,
					defaultValue = HttpResponseConstants.FORMAT_TYPE.XML) String format) {
		_logger.debug("serviceValidate " + " ticket " + ticket + " , service " + service + " , pgtUrl " + pgtUrl
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
			if (StringUtils.isNotBlank(pgtUrl)) {
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

			if (ConstsBoolean.isTrue(storedTicket.getCasDetails().getIsAdapter())) {
				Object samlAdapter = Instance.newInstance(storedTicket.getCasDetails().getAdapter());
				try {
					BeanUtils.setProperty(samlAdapter, "serviceResponseBuilder", serviceResponseBuilder);
				} catch (IllegalAccessException | InvocationTargetException e) {
					_logger.error("setProperty error . ", e);
				}

				AbstractAuthorizeAdapter adapter = (AbstractAuthorizeAdapter) samlAdapter;
				adapter.setPrincipal(authentication);
				adapter.setApp(storedTicket.getCasDetails());
				adapter.generateInfo();
			}
		} else {
			serviceResponseBuilder.failure().setCode(CasConstants.ERROR_CODE.INVALID_TICKET)
					.setDescription("Ticket " + ticket + " not recognized");
		}

		httpResponseAdapter.write(response, serviceResponseBuilder.serviceResponseBuilder(), format);
	}

	@Operation(summary = "CAS 3.0 ProxyTicket代理验证接口", description = "通过ProxyGrantingTicket获取ProxyTicket",
			method = "POST")
	@RequestMapping(CasConstants.ENDPOINT.ENDPOINT_PROXY_V3)
	public void proxy(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = CasConstants.PARAMETER.PROXY_GRANTING_TICKET) String pgt,
			@RequestParam(value = CasConstants.PARAMETER.TARGET_SERVICE) String targetService,
			@RequestParam(value = CasConstants.PARAMETER.FORMAT, required = false,
					defaultValue = HttpResponseConstants.FORMAT_TYPE.XML) String format) {
		_logger.debug("proxy " + " pgt " + pgt + " , targetService " + targetService + " , format " + format);
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
	@RequestMapping(CasConstants.ENDPOINT.ENDPOINT_PROXY_VALIDATE_V3)
	public void proxy(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = CasConstants.PARAMETER.TICKET) String ticket,
			@RequestParam(value = CasConstants.PARAMETER.SERVICE) String service,
			@RequestParam(value = CasConstants.PARAMETER.PROXY_CALLBACK_URL, required = false) String pgtUrl,
			@RequestParam(value = CasConstants.PARAMETER.RENEW, required = false) String renew,
			@RequestParam(value = CasConstants.PARAMETER.FORMAT, required = false,
					defaultValue = HttpResponseConstants.FORMAT_TYPE.XML) String format) {
		_logger.debug("proxyValidate " + " ticket " + ticket + " , service " + service + " , pgtUrl " + pgtUrl
				+ " , renew " + renew + " , format " + format);

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
			if (ConstsBoolean.isTrue(storedTicket.getCasDetails().getIsAdapter())) {
				Object samlAdapter = Instance.newInstance(storedTicket.getCasDetails().getAdapter());
				try {
					BeanUtils.setProperty(samlAdapter, "serviceResponseBuilder", serviceResponseBuilder);
				} catch (IllegalAccessException | InvocationTargetException e) {
					_logger.error("setProperty error . ", e);
				}

				AbstractAuthorizeAdapter adapter = (AbstractAuthorizeAdapter) samlAdapter;
				adapter.setPrincipal(authentication);
				adapter.setApp(storedTicket.getCasDetails());
				adapter.generateInfo();
			}
		} else {
			serviceResponseBuilder.failure().setCode(CasConstants.ERROR_CODE.INVALID_TICKET)
					.setDescription("Ticket " + ticket + " not recognized");
		}
		httpResponseAdapter.write(response, serviceResponseBuilder.serviceResponseBuilder(), format);
	}
}
