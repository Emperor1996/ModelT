/*****************************************************************************
 Class:        InstorecsHandoverOrderComponentController
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.frontend.controllers.cms;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sap.retail.isce.facade.impl.DefaultInstorecsAssistedServiceFacade;
import com.sap.retail.isce.sap.exception.SAPISCEException;
import com.sap.retail.isce.service.sap.OrderHandOverOutput;
import com.sap.retail.isce.service.sap.OrderHandOverOutput.Status;


/**
 * Controller to process the handover action
 *
 */
@Controller
public class InstorecsHandoverOrderComponentController extends AbstractController
{

	private static final String TEXT_RETRIEVING_ORDER = "Retrieving order '";

	private static final String TEXT_FROM_SESSION = "' from session.";

	protected static Logger log = Logger.getLogger(InstorecsHandoverOrderComponentController.class);

	private static final String ACCOUNT_ORDER_DETAIL_PAGE = "redirect:/my-account/order/";
	private static final String ACCOUNT_ORDER_LIST_PAGE = "redirect:/my-account/orders/";
	private static final String ERROR_MESSAGE_CODE = "errorMessageCode";
	private static final String ERROR_IN_HANDOVER_MESSAGE_CODE = "instorecs.errorInHandOverProcess";
	private static final String ERROR_NO_INVOICE_BACK_MESSAGE_CODE = "instorecs.errorNoInvoiceReturned";
	private static final String ORDER_CODE_FOR_INVOICE = "orderCodeForInvoice";
	private static final String INVOICE_PDF = "invoicePdf";


	@Resource(name = "i18nService")
	private I18NService i18nService = null;
	@Resource(name = "storefrontMessageSource")
	private MessageSource messageSource = null;
	@Resource(name = "assistedServiceFacade")
	private DefaultInstorecsAssistedServiceFacade assistedServiceFacade;

	@RequestMapping(value = "/handover/act", method = RequestMethod.POST)
	public String handoverOrder(final HttpServletRequest request, final RedirectAttributes redirectAttributes)
	{
		final String orderCodeFromSession = (String) request.getSession().getAttribute("orderCodeForHandover");
		if (orderCodeFromSession == null || orderCodeFromSession.isEmpty())
		{
			log.error("Attribute orderCodeForHandover not found in the request session");
			return ACCOUNT_ORDER_LIST_PAGE;
		}

		OrderHandOverOutput output;
		try
		{
			output = assistedServiceFacade.handOverOrder(orderCodeFromSession);
			if (!Status.OK.equals(output.getStatus()))
			{
				redirectAttributes.addFlashAttribute(ERROR_MESSAGE_CODE, ERROR_IN_HANDOVER_MESSAGE_CODE);
			}
			else
			{
				final byte[] invoice = output.getInvoiceAsPDF();
				if (invoice != null && invoice.length > 0)
				{
					redirectAttributes.addFlashAttribute(ORDER_CODE_FOR_INVOICE, orderCodeFromSession);
					keepInvoiceInSession(request, invoice, orderCodeFromSession);
				}
				else
				{
					redirectAttributes.addFlashAttribute(ERROR_MESSAGE_CODE, ERROR_NO_INVOICE_BACK_MESSAGE_CODE);
				}
			}
		}
		catch (final SAPISCEException e)
		{
			log.error("Error in handover process", e);
			redirectAttributes.addFlashAttribute(ERROR_MESSAGE_CODE, ERROR_IN_HANDOVER_MESSAGE_CODE);
		}

		return ACCOUNT_ORDER_DETAIL_PAGE + orderCodeFromSession;
	}

	@RequestMapping(value = "/**/displayinvoicepdf/act", method = RequestMethod.GET)
	public void displayInvoicePdf(final HttpServletRequest request, final HttpServletResponse response)
	{
		final String orderCodeFromSession = (String) request.getSession().getAttribute("orderCodeForInvoicePDF");
		log.debug(TEXT_RETRIEVING_ORDER + orderCodeFromSession + TEXT_FROM_SESSION);

		if (orderCodeFromSession == null || orderCodeFromSession.isEmpty())
		{
			log.error("Attribute orderCodeForInvoicePDF not found in the request session");
			respondWithErrorPage(response, orderCodeFromSession);
			return;
		}

		//check if the invoice has already been read and is stored in the session
		byte[] invoice = retrieveInvoiceFromSession(request, orderCodeFromSession);

		if (invoice == null || invoice.length == 0)
		{
			invoice = retrieveInvoiceFromBackend(orderCodeFromSession);
		}

		if (invoice == null || invoice.length == 0)
		{
			respondWithErrorPage(response, orderCodeFromSession);
			return;
		}

		try
		{
			response.setContentType("application/pdf");
			response.setContentLength(invoice.length);
			response.getOutputStream().write(invoice);
			keepInvoiceInSession(request, invoice, orderCodeFromSession);
		}
		catch (final IOException e)
		{
			log.error("Failed to show invoice for order " + orderCodeFromSession, e);
		}
	}

	/**
	 * Creates the HTML code for an error page and fills this page in the response.
	 *
	 * @param response
	 * @param orderCodeFromSession
	 */
	protected void respondWithErrorPage(final HttpServletResponse response, final String orderCodeFromSession)
	{
		log.error("Got no invoice for order with id: " + orderCodeFromSession);
		response.setContentType("text/HTML");

		final Object[] attributes = new Object[1];
		attributes[0] = orderCodeFromSession;
		final StringBuilder htmlText = new StringBuilder();
		htmlText.append("<html><head></head><body>")
				.append(messageSource.getMessage("instorecs.errorInDisplayInvoice", attributes, this.i18nService.getCurrentLocale()))
				.append("</body>");
		final byte[] htmlContent = htmlText.toString().getBytes();
		response.setContentLength(htmlContent.length);
		try
		{
			response.getOutputStream().write(htmlContent);
		}
		catch (final IOException e)
		{
			log.error("Failed to show invoice for order " + orderCodeFromSession, e);
		}
		return;
	}

	/**
	 * @param assistedServiceFacade
	 *           the assistedServiceFacade to set
	 */
	protected void setAssistedServiceFacade(final DefaultInstorecsAssistedServiceFacade assistedServiceFacade)
	{
		this.assistedServiceFacade = assistedServiceFacade;
	}

	/**
	 * @param messageSource
	 *           The messageSource containing the resource bundle.
	 */
	protected void setMessageSource(final MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	/**
	 * @param i18nService
	 *           the internationalization service necessary to retrieve the current locale
	 */
	protected void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

	/**
	 * Retrieve the invoice from the backend
	 *
	 * @param orderCodeFromSession
	 * @return
	 */
	protected byte[] retrieveInvoiceFromBackend(final String orderCodeFromSession)
	{
		try
		{
			return assistedServiceFacade.getInvoiceDocumentForOrderCode(orderCodeFromSession);
		}
		catch (final SAPISCEException e)
		{
			log.error("Error when trying to get invoice for order code " + orderCodeFromSession, e);
			return new byte[0];
		}
	}

	private void keepInvoiceInSession(final HttpServletRequest request, final byte[] invoice, final String orderCode)
	{
		Object invoiceMap = request.getSession().getAttribute(INVOICE_PDF);
		if (invoiceMap == null)
		{
			invoiceMap = new HashMap();
		}
		((Map) invoiceMap).put(orderCode, invoice);
		request.getSession().setAttribute(INVOICE_PDF, invoiceMap);
		log.debug("Keeping Invoice for order '" + orderCode + "' in session.");
	}

	private byte[] retrieveInvoiceFromSession(final HttpServletRequest request, final String orderCode)
	{
		byte[] invoice = new byte[0];
		final Object invoiceMap = request.getSession().getAttribute(INVOICE_PDF);
		if (invoiceMap != null)
		{
			invoice = (byte[]) ((Map) invoiceMap).get(orderCode);
			((Map) invoiceMap).remove(orderCode);
			log.debug("Retrieving and Removing Invoice for order '" + orderCode + TEXT_FROM_SESSION);
		}
		return invoice;
	}
}