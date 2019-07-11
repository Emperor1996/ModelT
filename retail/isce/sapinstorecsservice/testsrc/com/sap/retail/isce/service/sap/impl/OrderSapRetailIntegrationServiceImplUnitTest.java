/*****************************************************************************
 Class:        OrderSapRetailIntegrationServiceImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.core.configuration.ConfigurationPropertyAccess;
import de.hybris.platform.sap.core.configuration.enums.HTTPAuthenticationType;
import de.hybris.platform.sap.core.configuration.global.SAPGlobalConfigurationService;
import de.hybris.platform.sap.core.configuration.http.HTTPDestination;
import de.hybris.platform.sap.core.configuration.http.HTTPDestinationService;
import de.hybris.platform.sap.core.configuration.http.impl.DefaultHTTPDestination;
import de.hybris.platform.sap.core.configuration.impl.DefaultConfigurationPropertyAccess;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.ep.entry.EntryMetadata;
import org.apache.olingo.odata2.api.ep.entry.MediaMetadata;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.uri.ExpandSelectTreeNode;
import org.junit.Before;
import org.junit.Test;

import com.sap.retail.isce.sap.exception.SAPISCEException;
import com.sap.retail.isce.service.sap.OrderHandOverOutput;
import com.sap.retail.isce.service.sap.OrderHandoverInput.Operation;
import com.sap.retail.isce.service.sap.odata.BackendMessage;
import com.sap.retail.isce.service.sap.odata.BackendMessage.Severity;
import com.sap.retail.isce.service.sap.odata.BackendMessageUtil;
import com.sap.retail.isce.service.sap.odata.HttpODataClient;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;
import com.sap.retail.isce.service.sap.odata.HttpODataResult.StatusCode;
import com.sap.retail.isce.service.sap.odata.HttpODataResultManipulator;
import com.sap.retail.isce.service.sap.odata.impl.BackendMessageImpl;
import com.sap.retail.isce.service.sap.odata.impl.DataContainerBatchPart;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataException;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataResultImpl;


/**
 * Unit Test class for OrderSapRetailIntegrationServiceImpl.
 */
@UnitTest
public class OrderSapRetailIntegrationServiceImplUnitTest
{
	private TestOrderSapRetailIntegrationServiceImpl classUnderTest;

	private static String HTTP_DESTINATION_NAME = "HTTP_DESTINATION_NAME";
	private static String HTTP_DESTINATION_USER_ID = "HTTP_DESTINATION_USER_ID";
	private static String HTTP_DESTINATION_TARGET_URL = "http://www.sap.com/test/target/url/";
	private static String ORDER_ID = "0000012345";
	private static String INVOICE_ID = "9876";
	private static String GOODSISSUES_ID = "12345";
	private static String PICKING_ID = "000111";
	private static String DELIVERIES_IDS = "800111 800112";
	private static String MESSAGE_CLASS = "messageClass";
	private static String MESSAGE_TEXT = "The bad message";
	private static String MEDIA_CONTENT = "A media content";

	private boolean throwHttpODataException = false;
	private boolean errorWasLogged = false;
	private boolean warningWasLogged = false;
	private boolean debugWasLogged = false;
	private boolean oDataEntry = false;

	@Before
	public void setUp()
	{
		classUnderTest = new TestOrderSapRetailIntegrationServiceImpl();

		classUnderTest.setHttpDestinationService(httpDestinationServiceMock);
		classUnderTest.setGlobalConfigurationService(globalConfigurationServiceMock);
		classUnderTest.setHttpODataClient(httpODataClientMock);
		classUnderTest.setMessageUtil(messageUtilMock);

		assertSame("getHttpDestinationService is not identical", classUnderTest.getHttpDestinationService(),
				httpDestinationServiceMock);
		assertSame("getGlobalConfigurationService is not identical", classUnderTest.getGlobalConfigurationService(),
				globalConfigurationServiceMock);
		assertSame("getHttpODataClient is not identical", classUnderTest.getHttpODataClient(), httpODataClientMock);
		assertSame("getMessageUtil is not identical", classUnderTest.getMessageUtil(), messageUtilMock);
	}

	@Test
	public void testGetHttpDestination()
	{

		final HTTPDestination httpDestination = classUnderTest.getHttpDestination();

		assertNotNull("httpDestination is null", httpDestination);
		assertEquals("getUserid is not identical", httpDestination.getUserid(), HTTP_DESTINATION_USER_ID);
		assertEquals("getHttpDestinationName is not identical", httpDestination.getHttpDestinationName(), HTTP_DESTINATION_NAME);
		assertEquals("getTargetURL is not identical", httpDestination.getTargetURL(), HTTP_DESTINATION_TARGET_URL);
		assertEquals("getAuthenticationType is not identical", httpDestination.getAuthenticationType(),
				HTTPAuthenticationType.BASIC_AUTHENTICATION.toString());

		classUnderTest.setDestination(httpDestination);
		assertSame("getDestination is not identical", classUnderTest.getDestination(), httpDestination);
	}

	@Test
	public void testHandoverOrder()
	{
		// HAPPY PATH
		errorWasLogged = false;
		warningWasLogged = false;
		debugWasLogged = false;

		final OrderHandOverInputImpl input = new OrderHandOverInputImpl();
		input.setOrderId(ORDER_ID);
		input.setGetInvoiceAsPDF(true);
		input.addOperation(Operation.DELIVERY);
		input.addOperation(Operation.PICK);
		OrderHandOverOutput handoverOrder;
		try
		{
			handoverOrder = classUnderTest.handoverOrder(input);
			assertNotNull("handoverOrder is null", handoverOrder);
			assertTrue("Error was not logged", errorWasLogged);
			assertFalse("Warning was logged", warningWasLogged);
			assertTrue("Debug info was not logged", debugWasLogged);
		}
		catch (final SAPISCEException ex)
		{
			assertFalse("SAPISCEException was thrown", true);
		}

		errorWasLogged = false;
		warningWasLogged = false;
		debugWasLogged = false;

		// EXCEPTION
		throwHttpODataException = true;
		try
		{
			classUnderTest.handoverOrder(input);
			assertTrue("Expected exception was not thrown", false);
		}
		catch (final SAPISCEException e)
		{
			assertTrue("Expected exception was thrown", true);
		}
		throwHttpODataException = false;
	}

	@Test
	public void testGetInvoiceForOrder()
	{
		// NULL
		errorWasLogged = false;
		warningWasLogged = false;
		debugWasLogged = false;
		oDataEntry = false;

		byte[] invoiceForOrder;
		try
		{
			invoiceForOrder = classUnderTest.getInvoiceForOrder(ORDER_ID);
			assertNull("invoiceForOrder is not null", invoiceForOrder);
			assertTrue("Error was not logged", errorWasLogged);
			assertFalse("Warning was logged", warningWasLogged);
			assertTrue("Debug info was not logged", debugWasLogged);
		}
		catch (final SAPISCEException ex)
		{
			assertFalse("SAPISCEException was thrown", true);
		}
		errorWasLogged = false;
		warningWasLogged = false;
		debugWasLogged = false;
		oDataEntry = true;

		// HAPPY PATH
		try
		{
			invoiceForOrder = classUnderTest.getInvoiceForOrder(ORDER_ID);
			assertEquals("bytes[] not identical", new String(invoiceForOrder), MEDIA_CONTENT);
		}
		catch (final SAPISCEException e1)
		{
			assertFalse("SAPISCEException was thrown", true);
		}
		oDataEntry = false;

		// EXCEPTION
		throwHttpODataException = true;
		try
		{
			classUnderTest.getInvoiceForOrder(ORDER_ID);
			assertTrue("Expected exception was not thrown", false);
		}
		catch (final SAPISCEException e)
		{
			assertTrue("Expected exception was thrown", true);
		}
		throwHttpODataException = false;

	}

	@Test
	public void testSetFailedOpOData()
	{
		final OrderHandOverOutputImpl output = new OrderHandOverOutputImpl();
		classUnderTest.setFailedOpOData(output, "PICK/GOODSISSUE");
		assertEquals("Operation.PICK is not identical", output.getFailedOp(), Operation.PICK);
	}























	private class TestOrderSapRetailIntegrationServiceImpl extends OrderSapRetailIntegrationServiceImpl
	{
		public TestOrderSapRetailIntegrationServiceImpl()
		{
			super();
			log = new LoggerTest("");
		}
	}

	private final BackendMessageUtil messageUtilMock = new BackendMessageUtil()
	{

		@Override
		public List<BackendMessage> mergeMessageLists(final List<BackendMessage> m1, final List<BackendMessage> m2)
		{
			return null;
		}

		@Override
		public boolean hasErrorMessage(final List<BackendMessage> messages)
		{
			return true;
		}
	};

	private final HttpODataClient httpODataClientMock = new HttpODataClient()
	{

		@Override
		public HttpODataResult readFeed(final String entitySetName, final String filter, final HTTPDestination httpDestination,
				final String serviceURI) throws HttpODataException
		{
			if (throwHttpODataException)
			{
				throw new HttpODataException("Message", 404);
			}
			final HttpODataResultManipulator httpODataResultManipulator = new HttpODataResultImpl();
			final BackendMessageImpl message = new BackendMessageImpl(Severity.ERROR, MESSAGE_CLASS, 123, MESSAGE_TEXT);
			httpODataResultManipulator.setMessages(Collections.singletonList(message));

			if (oDataEntry)
			{
				httpODataResultManipulator.setEntity(oDataEntryMock);
			}

			return (HttpODataResult) httpODataResultManipulator;
		}

		@Override
		public HttpODataResult putEntity(final String entitySetName, final Map<String, Object> entityData,
				final HTTPDestination httpDestination, final String serviceURI) throws HttpODataException
		{
			if (throwHttpODataException)
			{
				throw new HttpODataException("Message", 404);
			}

			final HttpODataResultManipulator httpODataResultManipulator = new HttpODataResultImpl();
			final Map<String, List<String>> httpHeaders = new HashMap<String, List<String>>();
			httpHeaders.put("deliveries", Collections.singletonList(DELIVERIES_IDS));
			httpHeaders.put("picking", Collections.singletonList(PICKING_ID));
			httpHeaders.put("goodsissues", Collections.singletonList(GOODSISSUES_ID));
			httpHeaders.put("invoices", Collections.singletonList(INVOICE_ID));
			httpHeaders.put("status", Collections.singletonList(OrderHandOverOutput.Status.ERROR.toString()));
			httpHeaders.put("failing_operation", Collections.singletonList("PICK"));
			httpHeaders.put(null, Collections.singletonList(DELIVERIES_IDS));
			httpODataResultManipulator.setHttpHeaders(httpHeaders);
			httpODataResultManipulator.setStatusCode(StatusCode.ERROR);
			httpODataResultManipulator.setHttpStatusCode(HttpStatusCodes.BAD_REQUEST);
			final BackendMessageImpl message = new BackendMessageImpl(Severity.ERROR, MESSAGE_CLASS, 123, MESSAGE_TEXT);
			httpODataResultManipulator.setMessages(Collections.singletonList(message));

			return (HttpODataResult) httpODataResultManipulator;
		}

		@Override
		public HttpODataResult getMediaFile(final String entitySetName, final Map<String, Object> entityKeys,
				final String contentType, final HTTPDestination httpDestination, final String serviceURI) throws HttpODataException
		{
			if (throwHttpODataException)
			{
				throw new HttpODataException("Message", 404);
			}
			final HttpODataResultManipulator httpODataResultManipulator = new HttpODataResultImpl();
			final BackendMessageImpl message = new BackendMessageImpl(Severity.ERROR, MESSAGE_CLASS, 123, MESSAGE_TEXT);
			httpODataResultManipulator.setMessages(Collections.singletonList(message));
			httpODataResultManipulator.setMediaContent(MEDIA_CONTENT.getBytes());

			return (HttpODataResult) httpODataResultManipulator;
		}

		@Override
		public Map<String, HttpODataResult> executeBatchCall(final List<DataContainerBatchPart> dcBatchParts,
				final HTTPDestination httpDestination, final String serviceURI) throws HttpODataException
		{
			return null;
		}
	};

	private final ODataEntry oDataEntryMock = new ODataEntry()
	{

		@Override
		public Map<String, Object> getProperties()
		{
			final Map<String, Object> theMap = new HashMap<>();
			theMap.put("DocumentId", INVOICE_ID);
			return theMap;
		}

		@Override
		public EntryMetadata getMetadata()
		{
			return null;
		}

		@Override
		public MediaMetadata getMediaMetadata()
		{
			return null;
		}

		@Override
		public ExpandSelectTreeNode getExpandSelectTree()
		{
			return null;
		}

		@Override
		public boolean containsInlineEntry()
		{
			return false;
		}
	};


	private class LoggerTest extends Logger
	{
		protected LoggerTest(final String name)
		{
			super(name);
		}

		@Override
		public boolean isInfoEnabled()
		{
			return true;
		}


		@Override
		public boolean isDebugEnabled()
		{
			return true;
		}

		@Override
		public void error(final Object message)
		{
			errorWasLogged = true;
		}

		@Override
		public void warn(final Object message)
		{
			warningWasLogged = true;
		}

		@Override
		public void error(final Object message, final Throwable t)
		{
			//
		}

		@Override
		public void debug(final Object message)
		{
			debugWasLogged = true;
		}

		@Override
		public void info(final Object message)
		{
			//
		}
	}

	private final SAPGlobalConfigurationService globalConfigurationServiceMock = new SAPGlobalConfigurationService()
	{

		@Override
		public Collection<ConfigurationPropertyAccess> getPropertyAccessCollection(final String propertyAccessCollectionName)
		{
			return null;
		}

		@Override
		public ConfigurationPropertyAccess getPropertyAccess(final String propertyAccessName)
		{
			if (propertyAccessName.equals("sapcommon_erpHttpDestination"))
			{
				final DefaultConfigurationPropertyAccess httpDestinationPropertyMap = new DefaultConfigurationPropertyAccess();
				final Map<String, Object> properties = new HashMap<>();
				properties.put("httpDestinationName", HTTP_DESTINATION_NAME);
				httpDestinationPropertyMap.setProperties(properties);
				return httpDestinationPropertyMap;
			}
			return null;
		}

		@Override
		public <T> T getProperty(final String propertyName)
		{
			return null;
		}

		@Override
		public Map<String, ConfigurationPropertyAccess> getAllPropertyAccesses()
		{
			return null;
		}

		@Override
		public Map<String, Collection<ConfigurationPropertyAccess>> getAllPropertyAccessCollections()
		{
			return null;
		}

		@Override
		public Map<String, Object> getAllProperties()
		{
			return null;
		}

		@Override
		public boolean sapGlobalConfigurationExists()
		{
			return false;
		}
	};

	private final HTTPDestinationService httpDestinationServiceMock = new HTTPDestinationService()
	{
		@Override
		public HTTPDestination getHTTPDestination(final String destinationName)
		{
			if (destinationName.equals(HTTP_DESTINATION_NAME))
			{
				final DefaultHTTPDestination httpDestination = new DefaultHTTPDestination();
				httpDestination.setUserid(HTTP_DESTINATION_USER_ID);
				httpDestination.setHttpDestinationName(HTTP_DESTINATION_NAME);
				httpDestination.setTargetURL(HTTP_DESTINATION_TARGET_URL);
				httpDestination.setAuthenticationType(HTTPAuthenticationType.BASIC_AUTHENTICATION.toString());
				return httpDestination;
			}
			return null;
		}
	};



}
