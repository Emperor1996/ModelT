/*****************************************************************************
 Class:        Customer360PageControllerUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.frontend.controllers.pages;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.servicelayer.session.SessionService;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;


/**
 * Test for Customer360PageControllerTest
 *
 */
@UnitTest
public class Customer360PageControllerUnitTest
{

	private final static String VIEW_FOR_PAGE = "customer360";
	private final static String PAGE_TITLE = "Customer 360";

	private final Customer360PageControllerTest classUnderTest = new Customer360PageControllerTest();
	private final Model mvcModel = new ExtendedModelMap();

	@Mock
	private CustomerFacade customerFacadeMock;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testShowCustomer360()
	{
		String customer360Page = "";
		try
		{
			customer360Page = classUnderTest.showCustomer360(mvcModel);
		}
		catch (final CMSItemNotFoundException e)
		{
			assertFalse("CMSItemNotFoundException occured", false);
		}
		assertTrue(mvcModel.containsAttribute(AbstractPageController.CMS_PAGE_TITLE));

		assertEquals(PAGE_TITLE, mvcModel.asMap().get(AbstractPageController.CMS_PAGE_TITLE));
		assertEquals("Customer360 page should be shown:", customer360Page, VIEW_FOR_PAGE);
	}

	private class LoggerTest extends Logger
	{
		protected LoggerTest(final String name)
		{
			super(name);
		}
	}

	/**
	 * Mock for real class under Test Customer360PageController
	 *
	 */
	class Customer360PageControllerTest extends Customer360PageController
	{
		public Customer360PageControllerTest()
		{
			super();
			log = new LoggerTest("");
		}

		@Override
		protected String getViewForPage(final Model model)
		{
			return VIEW_FOR_PAGE;
		}

		@Override
		protected void storeCmsPageInModel(final Model model, final AbstractPageModel cmsPage)
		{
			model.addAttribute(CMS_PAGE_MODEL, cmsPage);
			storeContentPageTitleInModel(model, PAGE_TITLE);
		}

		@Override
		protected ContentPageModel getContentPageForLabelOrId(final String labelOrId) throws CMSItemNotFoundException
		{
			return new ContentPageModel();
		}

		@Override
		protected SessionService getSessionService()
		{
			final SessionService mockSessionService = mock(SessionService.class);

			return mockSessionService;
		}

	}

}