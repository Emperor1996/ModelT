/*****************************************************************************
 Class:        CMSISCECChangeOrderActionControllerUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.frontend.controllers.cms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.sap.retail.isce.facade.OrderChangeFacade;


/**
 * Test for ChangeOrderActionController
 */
@UnitTest
public class CMSISCECChangeOrderActionControllerUnitTest
{
	private final LoggerTest loggerTest = new LoggerTest("");

	private final CMSISCECChangeOrderActionControllerTest classUnderTest = new CMSISCECChangeOrderActionControllerTest(loggerTest);

	@Mock
	protected OrderChangeFacade orderChangeFacadeMock;
	@Mock
	protected HttpServletRequest requestMock;
	@Mock
	protected HttpSession sessionMock;
	protected RedirectAttributes redirectAttributes;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		redirectAttributes = new RedirectAttributesModelMap();

		Mockito.doReturn(Boolean.TRUE).when(orderChangeFacadeMock).copyOrderToCartAndCancel("123456");
		Mockito.doReturn(sessionMock).when(requestMock).getSession();

		classUnderTest.setOrderChangeFacade(orderChangeFacadeMock);

		loggerTest.clearMessages();
	}

	@Test
	public void testChangeOrderNoOderInRequest()
	{
		final String navigate = classUnderTest.changeOrder(requestMock, redirectAttributes);

		Mockito.verify(requestMock, Mockito.times(1)).getSession();
		Mockito.verify(sessionMock, Mockito.times(1)).getAttribute("orderCodeForChange");

		String expected = "redirect:/my-account/orders";
		assertEquals("Navigate should be - " + expected, expected, navigate);

		expected = "Retrieving order '' from session.";
		assertTrue("Debug Message should have been created - " + expected, loggerTest.getMessages().contains(expected));
		expected = "Order number missing - Attribute orderCodeForChange not found in the request session";
		assertTrue("Error Messages should contain - " + expected, loggerTest.getMessages().contains(expected));
	}

	@Test
	public void testChangeOrder123456InRequest()
	{
		Mockito.doReturn("123456").when(sessionMock).getAttribute("orderCodeForChange");

		final String navigation = classUnderTest.changeOrder(requestMock, redirectAttributes);

		Mockito.verify(requestMock, Mockito.times(1)).getSession();
		Mockito.verify(sessionMock, Mockito.times(1)).getAttribute("orderCodeForChange");
		Mockito.verify(orderChangeFacadeMock, Mockito.times(1)).copyOrderToCartAndCancel("123456");

		final String expected = "Retrieving order '123456' from session.";
		assertTrue("Debug Message should have been created - " + expected, loggerTest.getMessages().contains(expected));
		assertEquals("Only Debug Message should have been created", 1, loggerTest.getMessages().size());
		assertEquals("Navigation should be - navigation", "redirect:/cart", navigation);
	}

	@Test
	public void testChangeOrder123456InRequestWithFailure()
	{
		Mockito.doReturn("123456").when(sessionMock).getAttribute("orderCodeForChange");
		Mockito.doReturn(Boolean.FALSE).when(orderChangeFacadeMock).copyOrderToCartAndCancel("123456");

		final String navigation = classUnderTest.changeOrder(requestMock, redirectAttributes);

		Mockito.verify(requestMock, Mockito.times(1)).getSession();
		Mockito.verify(sessionMock, Mockito.times(1)).getAttribute("orderCodeForChange");
		Mockito.verify(orderChangeFacadeMock, Mockito.times(1)).copyOrderToCartAndCancel("123456");

		String expected = "Retrieving order '123456' from session.";
		assertTrue("Debug Message should have been created - " + expected, loggerTest.getMessages().contains(expected));
		expected = "Copy order to basket failed";
		assertTrue("Error Messages should have been created - " + expected, loggerTest.getMessages().contains(expected));
		assertEquals("2 Messages should have been created", 2, loggerTest.getMessages().size());
		assertEquals("Navigation should be - navigation", "redirect:/my-account/order/123456", navigation);
		expected = "errorMessageCode";
		assertTrue("RedirectAttributes should contain Flash attribute - " + expected, redirectAttributes.getFlashAttributes()
				.containsKey(expected));
	}

	/**
	 * Mock for real class under Test
	 */
	class CMSISCECChangeOrderActionControllerTest extends CMSISCEChangeOrderActionController
	{
		public CMSISCECChangeOrderActionControllerTest(final LoggerTest logger)
		{
			super();
			log = logger;
		}
	}

	protected class LoggerTest extends Logger
	{
		private final List<String> messages = new ArrayList();

		protected LoggerTest(final String name)
		{
			super(name);
		}

		@Override
		public void debug(final Object message)
		{
			this.messages.add((String) message);
		}

		@Override
		public void error(final Object message)
		{
			this.messages.add((String) message);
		}

		//helper method
		public List<String> getMessages()
		{
			return messages;
		}

		public void clearMessages()
		{
			messages.clear();
		}
	}

	@Test
	public void testGetOrderChangeFacade()
	{
		assertSame("getOrderChangeFacade not identical", classUnderTest.getOrderChangeFacade(), orderChangeFacadeMock);
	}

}