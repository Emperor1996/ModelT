/*****************************************************************************
 Class:        CMSISCEChangeOrderActionController
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/

package com.sap.retail.isce.frontend.controllers.cms;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sap.retail.isce.facade.OrderChangeFacade;


/**
 * Controller to process the changeOrder action
 */
@Controller
public class CMSISCEChangeOrderActionController extends AbstractController
{
	protected static Logger log = Logger.getLogger(CMSISCEChangeOrderActionController.class.getName());

	private static final String TEXT_RETRIEVING_ORDER = "Retrieving order '$0' from session.";

	private static final String ACCOUNT_ORDER_DETAIL_PAGE = "redirect:/my-account/order/";
	private static final String ACCOUNT_ORDER_LIST_PAGE = "redirect:/my-account/orders";
	private static final String CART_PAGE = "redirect:/cart";
	private static final String ERROR_MESSAGE_CODE = "errorMessageCode";
	private static final String ERROR_IN_CHANGE_ODRER_MESSAGE_CODE = "instorecs.errorInChangeOrderProcess";

	private OrderChangeFacade orderChangeFacade;

	@RequestMapping(value = "/changeOrder/act", method = RequestMethod.POST)
	public String changeOrder(final HttpServletRequest request, final RedirectAttributes redirectAttributes)
	{

		final String orderCodeFromSession = (String) request.getSession().getAttribute("orderCodeForChange");
		log.debug(TEXT_RETRIEVING_ORDER.replace("$0", orderCodeFromSession == null ? "" : orderCodeFromSession));

		if (orderCodeFromSession == null || orderCodeFromSession.isEmpty())
		{
			log.error("Order number missing - Attribute orderCodeForChange not found in the request session");
			return ACCOUNT_ORDER_LIST_PAGE;
		}

		if (!orderChangeFacade.copyOrderToCartAndCancel(orderCodeFromSession))
		{
			log.error("Copy order to basket failed");
			redirectAttributes.addFlashAttribute(ERROR_MESSAGE_CODE, ERROR_IN_CHANGE_ODRER_MESSAGE_CODE);
			return ACCOUNT_ORDER_DETAIL_PAGE + orderCodeFromSession;
		}

		return CART_PAGE;
	}

	/**
	 * Get the orderChangeFacade.
	 *
	 * @return the orderChangeFacade
	 */
	public OrderChangeFacade getOrderChangeFacade()
	{
		return orderChangeFacade;
	}

	/**
	 * Set the orderChangeFacade.
	 *
	 * @param orderChangeFacade
	 *           the orderChangeFacade to set
	 */
	@Required
	public void setOrderChangeFacade(final OrderChangeFacade orderChangeFacade)
	{
		this.orderChangeFacade = orderChangeFacade;
	}
}
