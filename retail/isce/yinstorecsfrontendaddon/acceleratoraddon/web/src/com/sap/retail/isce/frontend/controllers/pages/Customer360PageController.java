/*****************************************************************************
 Class:        Customer360PageController
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.frontend.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractSearchPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sap.retail.isce.service.DataContainerService;


/**
 * Controller to process Customer 360° data.
 */
@Controller
public class Customer360PageController extends AbstractSearchPageController
{
	protected static Logger log = Logger.getLogger(Customer360PageController.class.getName());

	@RequestMapping(value = "/my-account/customer360", method = RequestMethod.POST)
	public String showCustomer360(final Model mvcModel) throws CMSItemNotFoundException
	{
		// remove session attribute holding data containers
		this.getSessionService().removeAttribute(DataContainerService.ISCE_360_DATA_CONTAINER_SESSION_ATTRIBUTE);

		// customer360Page is the id of the page in WCMS
		storeCmsPageInModel(mvcModel, getContentPageForLabelOrId("customer360Page"));
		return getViewForPage(mvcModel);
	}
}