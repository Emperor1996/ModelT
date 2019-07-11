/*****************************************************************************
 Class:        CMSISCECComponentBaseControllerUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.frontend.controllers.cms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.sap.core.common.message.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import com.sap.retail.isce.container.DataContainer;
import com.sap.retail.isce.container.impl.MarketingAttributesDataContainer;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.service.sap.ISCEConfigurationService;
import com.sap.retail.isce.service.util.DataContainerServiceUtil;


/**
 * Test for CMSISCECComponentBaseController
 */
@UnitTest
public class CMSISCECComponentBaseControllerUnitTest extends CMSISCEComponentControllerUnitTestBase
{
	@Mock
	protected ISCEConfigurationService isceConfigurationServiceMock;

	private final CMSISCECComponentBaseControllerTest classUnderTest = new CMSISCECComponentBaseControllerTest(loggerTest);

	private final MarketingAttributesDataContainer marketingAttributesDataContainer = new MarketingAttributesDataContainer(
			isceConfigurationServiceMock);

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		classUnderTest.setCustomer360Facade(customer360FacadeMock);
		classUnderTest.setCustomerFacade(customerFacadeMock);
		classUnderTest.setSessionService(sessionServiceMock);
		springUtilMock.setMarketingDataContainer(marketingAttributesDataContainer);
		classUnderTest.setSpringUtil(springUtilMock);

		customerData = new CustomerData();
		customerData.setUid("johndoe");
		final String[] dataContainerBeanAliases =
		{ MARKETING_ATTRIBUTES_DATA_CONTAINER };
		classUnderTest.setDataContainerBeanAliases(dataContainerBeanAliases);
	}

	@Test
	public void testShowCustomer360Exception()
	{
		classUnderTest.setDataContainerBeanAliases(new String[]
		{ "OneWhichDoesNotExist" });

		Mockito.doReturn(customerData).when(customerFacadeMock).getCurrentCustomer();
		try
		{
			classUnderTest.fillModel(requestMock, mvcModel, null);
			assertTrue("An Exception should have occured", false);
		}
		catch (final DataContainerRuntimeException dataContainerRuntimeException)
		{
			assertTrue("An Exception occured, as expected in Unit Test", true);
		}
	}

	@Test
	public void testFillModel()
	{
		classUnderTest.setDataContainerBeanAliases(new String[]
		{ MARKETING_ATTRIBUTES_DATA_CONTAINER });
		try
		{
			classUnderTest.fillModel(requestMock, mvcModel, null);
			assertTrue("Data Container found", true);
		}
		catch (final DataContainerRuntimeException dataContainerRuntimeException)
		{
			assertTrue("No Exception should have occured", false);
		}
		assertTrue(mvcModel.containsAttribute(MARKETING_ATTRIBUTES_DATA_CONTAINER));

		final DataContainerServiceUtil dataContainerUtil = new DataContainerServiceUtil();
		dataContainerUtil.setDataContainers(Arrays.asList(marketingAttributesDataContainer));
		Mockito.doReturn(dataContainerUtil).when(sessionServiceMock).getAttribute("ISCECustomer360DataContainers");

		try
		{
			classUnderTest.fillModel(requestMock, mvcModel, null);
			assertTrue("fillModel - Data Container found", true);
			assertEquals("fillModel - Data Container found", marketingAttributesDataContainer, customerFacadeDataContainers.get(0));
		}
		catch (final DataContainerRuntimeException dataContainerRuntimeException)
		{
			assertTrue("No Exception should have occured", false);
		}
		assertTrue(mvcModel.containsAttribute(MARKETING_ATTRIBUTES_DATA_CONTAINER));
	}

	@Test
	public void testDoAfterRead()
	{
		final List<DataContainer> dataContainers = new ArrayList<>();
		classUnderTest.doAfterRead(dataContainers);
		assertTrue("doAfterRead successfully called", true);
	}

	@Test
	public void testAddMessagesToGlobalMessages()
	{
		final List<DataContainer> dataContainers = new ArrayList<>();
		classUnderTest.addMessagesToGlobalMessages(mvcModel, dataContainers);
		assertNull("Attribute text shoud be null", classUnderTest.attributeText);

		final DataContainerTest dc1 = new DataContainerTest();
		dc1.setLocalizedContainerName("dc1_localized");
		dc1.setErrorState(Boolean.TRUE);
		final DataContainerTest dc2 = new DataContainerTest();
		dc2.setLocalizedContainerName("dc2_localized");
		dc2.setErrorState(Boolean.FALSE);
		dataContainers.add(dc1);
		dataContainers.add(dc2);
		classUnderTest.addMessagesToGlobalMessages(mvcModel, dataContainers);
		assertEquals("Attribute text shoud be dc1_localized", "dc1_localized", classUnderTest.attributeText);

		dc2.setErrorState(Boolean.TRUE);
		classUnderTest.addMessagesToGlobalMessages(mvcModel, dataContainers);
		assertEquals("Attribute text shoud be dc1_localized; dc2_localized", "dc1_localized; dc2_localized",
				classUnderTest.attributeText);
	}

	@Test
	public void testGetMessageHolder()
	{
		assertEquals("Message.SUCCESS should return INFO_MESSAGES_HOLDER", GlobalMessages.INFO_MESSAGES_HOLDER,
				classUnderTest.getMessageHolder(new Message(Message.SUCCESS)));
		assertEquals("Message.INFO should return INFO_MESSAGES_HOLDER", GlobalMessages.INFO_MESSAGES_HOLDER,
				classUnderTest.getMessageHolder(new Message(Message.INFO)));

		assertEquals("Message.WARNING should return ERROR_MESSAGES_HOLDER", GlobalMessages.ERROR_MESSAGES_HOLDER,
				classUnderTest.getMessageHolder(new Message(Message.WARNING)));
		assertEquals("Message.ERROR should return ERROR_MESSAGES_HOLDER", GlobalMessages.ERROR_MESSAGES_HOLDER,
				classUnderTest.getMessageHolder(new Message(Message.ERROR)));

		assertEquals("Message.DEBUG should return null", null, classUnderTest.getMessageHolder(new Message(Message.DEBUG)));
		assertEquals("Message.INITIAL should return null", null, classUnderTest.getMessageHolder(new Message(Message.INITIAL)));
	}

	@Test
	public void testGetAddonUiExtensionName()
	{
		assertEquals("AddonUiExtensionName should return INFO_MESSAGES_HOLDER", "yinstorecsfrontendaddon",
				classUnderTest.getAddonUiExtensionName(null));
	}

	@Test
	public void testAddMessagesFromDataContainerToGlobalMessages()
	{
		//no messages
		classUnderTest.addMessagesFromDataContainerToGlobalMessages(mvcModel, null);
		assertTrue("addMessagesFromDataContainerToGlobalMessages went well", true);

		// one message
		final DataContainerTest dc1 = new DataContainerTest();
		dc1.addMessage(new Message(Message.ERROR, null, "property"));

		classUnderTest.addMessagesFromDataContainerToGlobalMessages(mvcModel, dc1);
		assertTrue("addMessagesFromDataContainerToGlobalMessages went well", true);

		// Initial message
		dc1.clearMessages();
		dc1.addMessage(new Message(Message.INITIAL));

		try
		{
			classUnderTest.addMessagesFromDataContainerToGlobalMessages(mvcModel, dc1);
			assertTrue("addMessagesFromDataContainerToGlobalMessages must deliver an exception", false);
		}
		catch (final DataContainerRuntimeException e)
		{
			assertTrue("addMessagesFromDataContainerToGlobalMessages delivered an exception", true);
		}

		// Debug message
		dc1.clearMessages();
		final Message message = new Message(Message.DEBUG, null, "property");
		message.setDescription("Debug description");
		message.setResourceArgs(new String[]
		{ "resource1" });
		dc1.addMessage(message);

		classUnderTest.addMessagesFromDataContainerToGlobalMessages(mvcModel, dc1);
		assertEquals("addMessagesFromDataContainerToGlobalMessages message description must be 'Debug description'",
				"Debug description", loggerTest.getMessage().getDescription());
	}

	/**
	 * Mock for real class under Test
	 */
	class CMSISCECComponentBaseControllerTest extends CMSISCECComponentBaseController
	{
		private String attributeText = null;

		public CMSISCECComponentBaseControllerTest(final LoggerTest logger)
		{
			super();
			log = logger;
		}

		@Override
		protected void addMessageToGlobalMessages(final Model mvcModel, final String messageHolder, final String messageKey,
				final Object[] attributes)
		{

			if (attributes != null)
			{
				attributeText = attributes[0].toString();
			}
		}
	}

}