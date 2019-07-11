/*****************************************************************************
 Class:        InstorecsHandoverOrderComponentControllerTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.frontend.controllers.cms;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.sap.retail.isce.container.impl.mock.LoggerMock;
import com.sap.retail.isce.facade.impl.DefaultInstorecsAssistedServiceFacade;
import com.sap.retail.isce.sap.exception.SAPISCEException;
import com.sap.retail.isce.service.mock.I18NServiceMock;
import com.sap.retail.isce.service.mock.MessageSourceMock;
import com.sap.retail.isce.service.sap.OrderHandOverOutput.Status;
import com.sap.retail.isce.service.sap.impl.OrderHandOverOutputImpl;


/**
 * Test for InstorecsHandoverOrderComponentController.
 *
 */
@UnitTest
public class InstorecsHandoverOrderComponentControllerTest
{
	private static final String ORDER_DETAIL_PAGE = "redirect:/my-account/order/";
	private static final String ERROR_MESSAGE_CODE = "errorMessageCode";
	private static final String ERROR_IN_HANDOVER_MESSAGE_CODE = "instorecs.errorInHandOverProcess";
	private static final String ERROR_NO_INVOICE_RETURNED_MESSAGE_CODE = "instorecs.errorNoInvoiceReturned";
	private static final byte[] INVOICE_PDF_CONTENT = "Invoice Pdf Content".getBytes();
	private static final String SESSION_ATTRIBUTE_INVOICE_PDF = "invoicePdf";

	private final InstorecsHandoverOrderComponentController classUnderTest = new InstorecsHandoverOrderComponentController();
	private final LoggerMock loggerMock = new LoggerMock("");

	@Mock
	private DefaultInstorecsAssistedServiceFacade assistedServiceFacade;

	@Mock
	private HttpServletRequest request;

	@Mock
	private HttpServletResponse responseMock;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		classUnderTest.setAssistedServiceFacade(assistedServiceFacade);
		classUnderTest.setMessageSource(new MessageSourceMock());
		classUnderTest.setI18nService(new I18NServiceMock());
		classUnderTest.log = loggerMock;
		loggerMock.clearAll();

		final HttpSession session = new MockHttpSession();
		given(request.getSession()).willReturn(session);
	}

	@Test
	public void testHandoverOrderError() throws Exception
	{
		final OrderHandOverOutputImpl output = new OrderHandOverOutputImpl();
		output.setStatus(Status.ERROR);

		final RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

		final String orderCode = "OrderCode";
		reset(assistedServiceFacade);
		given(assistedServiceFacade.handOverOrder(orderCode)).willReturn(output);

		final OrderHistoryData orderHistoryData = new OrderHistoryData();
		orderHistoryData.setCode(orderCode);

		request.getSession().setAttribute("orderCodeForHandover", orderCode);
		final String page = classUnderTest.handoverOrder(request, redirectAttributes);

		assertEquals("No or wrong error code is set in the redirect attributes map", ERROR_IN_HANDOVER_MESSAGE_CODE,
				redirectAttributes.getFlashAttributes().get(ERROR_MESSAGE_CODE));

		assertEquals("Navigation to Order Detail page should be triggered", ORDER_DETAIL_PAGE + orderCode, page);
	}

	@Test
	public void testHandoverOrderException() throws Exception
	{
		final RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

		final String orderCode = "OrderCode";
		reset(assistedServiceFacade);
		given(assistedServiceFacade.handOverOrder(orderCode)).willThrow(new SAPISCEException());
		request.getSession().setAttribute("orderCodeForHandover", orderCode);

		classUnderTest.handoverOrder(request, redirectAttributes);

		assertEquals("No or wrong error code is set in the redirect attributes map", ERROR_IN_HANDOVER_MESSAGE_CODE,
				redirectAttributes.getFlashAttributes().get(ERROR_MESSAGE_CODE));
	}

	@Test
	public void testHandoverOrderRedirect() throws Exception
	{
		final OrderHandOverOutputImpl output = new OrderHandOverOutputImpl();
		output.setStatus(Status.ERROR);

		final RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

		final String orderCode = "OrderCode";
		reset(assistedServiceFacade);
		given(assistedServiceFacade.handOverOrder(orderCode)).willReturn(output);

		final OrderHistoryData orderHistoryData = new OrderHistoryData();
		orderHistoryData.setCode(orderCode);

		request.getSession().setAttribute("orderCodeForHandover", null);
		final String page = classUnderTest.handoverOrder(request, redirectAttributes);

		assertEquals("Navigation to Account Order List Page should be triggered", "redirect:/my-account/orders/", page);
	}

	@Test
	public void testHandoverOrderInvoicePdfError() throws Exception
	{

		final String orderCode = "OrderCode";
		final String invoiceCode = "invoiceCode";
		final byte[] emptyInvoice = new byte[0];

		final OrderHandOverOutputImpl output = new OrderHandOverOutputImpl();
		output.setStatus(Status.OK);
		output.setInvoiceId(invoiceCode);
		output.setInvoiceAsPDF(emptyInvoice);

		final RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

		reset(assistedServiceFacade);
		given(assistedServiceFacade.handOverOrder(orderCode)).willReturn(output);

		request.getSession().setAttribute("orderCodeForHandover", orderCode);
		final String page = classUnderTest.handoverOrder(request, redirectAttributes);

		assertEquals("No or wrong error code is set in the redirect attributes map", ERROR_NO_INVOICE_RETURNED_MESSAGE_CODE,
				redirectAttributes.getFlashAttributes().get(ERROR_MESSAGE_CODE));

		assertEquals("Navigation to Order Detail page should be triggered", ORDER_DETAIL_PAGE + orderCode, page);
	}

	@Test
	public void testHandoverOrderOK() throws Exception
	{
		final String orderCode = "OrderCode";
		final String invoiceCode = "invoiceCode";

		final OrderHandOverOutputImpl output = new OrderHandOverOutputImpl();
		output.setStatus(Status.OK);
		output.setInvoiceId(invoiceCode);
		output.setInvoiceAsPDF(INVOICE_PDF_CONTENT);

		final RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

		reset(assistedServiceFacade);
		given(assistedServiceFacade.handOverOrder(orderCode)).willReturn(output);

		request.getSession().setAttribute("orderCodeForHandover", orderCode);
		final String page = classUnderTest.handoverOrder(request, redirectAttributes);

		assertNull("Error code has not be set in the redirect attributes map",
				redirectAttributes.getFlashAttributes().get(ERROR_MESSAGE_CODE));

		assertEquals("Navigation to Order Detail page should be triggered", ORDER_DETAIL_PAGE + orderCode, page);

		final Map invoiceMapRetrievedFromSession = ((Map) request.getSession().getAttribute(SESSION_ATTRIBUTE_INVOICE_PDF));
		final byte[] invoiceContentRetrievedFromSession = (byte[]) (invoiceMapRetrievedFromSession.get(orderCode));

		assertArrayEquals("Wrong invoice content retrieved", INVOICE_PDF_CONTENT, invoiceContentRetrievedFromSession);
	}

	@Test
	public void testShowInvoiceOK() throws Exception
	{
		final String orderCode = "OrderCode";

		final MockHttpServletResponse response = new MockHttpServletResponse();

		reset(assistedServiceFacade);
		given(assistedServiceFacade.getInvoiceDocumentForOrderCode(orderCode)).willReturn(INVOICE_PDF_CONTENT);

		request.getSession().setAttribute("orderCodeForInvoicePDF", orderCode);
		classUnderTest.displayInvoicePdf(request, response);

		assertEquals("Wrong Content Type", "application/pdf", response.getContentType());
		assertEquals("Wrong Content Length", INVOICE_PDF_CONTENT.length, response.getContentLength());
		assertArrayEquals("Wrong invoice content retrieved", INVOICE_PDF_CONTENT, response.getContentAsByteArray());
	}

	@Test
	public void testShowInvoiceMissingOrder() throws Exception
	{
		String orderCode = null;

		final MockHttpServletResponse response = new MockHttpServletResponse();

		request.getSession().setAttribute("orderCodeForInvoicePDF", orderCode);
		classUnderTest.displayInvoicePdf(request, response);

		assertEquals("Wrong Content Type", "text/HTML", response.getContentType());

		orderCode = "";
		request.getSession().setAttribute("orderCodeForInvoicePDF", orderCode);
		classUnderTest.displayInvoicePdf(request, response);

		assertEquals("Wrong Content Type", "text/HTML", response.getContentType());
	}

	@Test
	public void testShowInvoiceNull() throws Exception
	{
		final String orderCode = "OrderCode";

		final MockHttpServletResponse response = new MockHttpServletResponse();

		reset(assistedServiceFacade);
		given(assistedServiceFacade.getInvoiceDocumentForOrderCode(orderCode)).willReturn(null);

		request.getSession().setAttribute("orderCodeForInvoicePDF", orderCode);
		classUnderTest.displayInvoicePdf(request, response);

		assertNotEquals("Wrong Content Type", "application/pdf", response.getContentType());
		assertNotEquals("Wrong Content Length", INVOICE_PDF_CONTENT.length, response.getContentLength());
	}

	@Test
	public void testRetrieveInvoiceFromBackend() throws Exception
	{
		final String orderCode = "OrderCode";

		reset(assistedServiceFacade);
		given(assistedServiceFacade.getInvoiceDocumentForOrderCode(orderCode)).willReturn(null);

		byte[] invoice = classUnderTest.retrieveInvoiceFromBackend(orderCode);

		assertEquals("should not provide an invoice", null, invoice);

		reset(assistedServiceFacade);
		given(assistedServiceFacade.getInvoiceDocumentForOrderCode(orderCode)).willThrow(new SAPISCEException());

		invoice = classUnderTest.retrieveInvoiceFromBackend(orderCode);

		assertEquals("should provide an empty invoice", new byte[0].length, invoice.length);
	}

	@Test
	public void testRespondWithErrorPage() throws Exception
	{
		final String orderCode = "OrderCode";

		final MockHttpServletResponse response = new MockHttpServletResponse();
		classUnderTest.respondWithErrorPage(response, orderCode);

		assertEquals("Wrong Content Type", "text/HTML", response.getContentType());
		assertTrue("Wrong Content", response.getContentAsString().contains("</body>"));
	}

	@Test
	public void testRespondWithErrorPageException() throws Exception
	{
		final String orderCode = "OrderCode";
		Mockito.doThrow(new IOException()).when(responseMock).getOutputStream();

		classUnderTest.respondWithErrorPage(responseMock, orderCode);
		assertEquals("no error has been logged", 2, loggerMock.getError().size());
	}

	@Test
	public void testShowInvoiceException() throws Exception
	{
		final String orderCode = "OrderCode";
		reset(assistedServiceFacade);
		given(assistedServiceFacade.getInvoiceDocumentForOrderCode(orderCode)).willReturn(INVOICE_PDF_CONTENT);
		request.getSession().setAttribute("orderCodeForInvoicePDF", orderCode);

		Mockito.doThrow(new IOException()).when(responseMock).getOutputStream();
		classUnderTest.displayInvoicePdf(request, responseMock);

		assertEquals("no error has been logged", 1, loggerMock.getError().size());
	}
}