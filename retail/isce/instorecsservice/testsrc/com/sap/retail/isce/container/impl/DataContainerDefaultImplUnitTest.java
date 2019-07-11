/*****************************************************************************
 Class:        DataContainerDefaultImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.core.common.message.Message;
import de.hybris.platform.sap.core.common.message.MessageList;
import de.hybris.platform.servicelayer.i18n.I18NService;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.MessageSource;

import com.sap.retail.isce.UnitTestBase;
import com.sap.retail.isce.container.DataContainerContext;
import com.sap.retail.isce.container.DataContainerPropertyLevel;
import com.sap.retail.isce.exception.DataContainerPropertyLevelException;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.model.CMSISCECustomer360ComponentModel;
import com.sap.retail.isce.service.mock.I18NServiceMock;
import com.sap.retail.isce.service.mock.MessageSourceMock;


/**
 * Unit Test class for DataContainerDefaultImpl.
 *
 */
@UnitTest
public class DataContainerDefaultImplUnitTest extends UnitTestBase
{
	private final boolean ThrowUnsupportedEncodingException = false;
	private final MessageSource messageSourceTest = new MessageSourceMock();

	private class DataContainerDefaultImplTest extends DataContainerDefaultImpl
	{
		@Override
		public void setDataInErrorState()
		{
			// nothing to do
		}

		@Override
		public void encodeHTML()
		{
			super.encodeHTML("");
		}

		@Override
		protected String encodeHTMLInner(final String input) throws UnsupportedEncodingException
		{
			if (ThrowUnsupportedEncodingException)
			{
				throw new UnsupportedEncodingException();
			}
			else
			{
				return super.encodeHTMLInner(input);
			}
		}

		@Override
		public String getLocalizedContainerName()
		{
			return null;
		}

		@Override
		public String getContainerContextParamName()
		{
			return "DataContainerDefaultImplTest";
		}

	}

	private DataContainerDefaultImpl classUnderTest = null;
	private final Boolean errorState = Boolean.TRUE;

	@Override
	@Before
	public void setUp()
	{
		super.setUp();
		// As the class to be tested is abstract, we test a derived class
		classUnderTest = new DataContainerDefaultImplTest();
	}

	@Test
	public void testErrorState()
	{
		classUnderTest.setErrorState(errorState);
		assertEquals("Error State should be true", Boolean.TRUE, classUnderTest.getErrorState());
	}

	@Test
	public void testSetDataInErrorState()
	{
		classUnderTest.setDataInErrorState();
		assertTrue("Method Execution went well", true);
	}

	@Test
	public void testGetContainerName()
	{
		try
		{
			classUnderTest.getContainerName();
			assertTrue("DataContainerRuntimeException should have been thrown", false);
		}
		catch (final DataContainerRuntimeException ex)
		{
			assertEquals("DataContainerRuntimeException for missing getContainerName was thrown", ex.getClass().getName(),
					"com.sap.retail.isce.exception.DataContainerRuntimeException");
		}

		try
		{
			classUnderTest.containerName = "";
			classUnderTest.getContainerName();
			assertTrue("DataContainerRuntimeException should have been thrown", false);
		}
		catch (final DataContainerRuntimeException ex)
		{
			assertEquals("DataContainerRuntimeException for missing getContainerName was thrown", ex.getClass().getName(),
					"com.sap.retail.isce.exception.DataContainerRuntimeException");
		}

		classUnderTest.containerName = "containerName1";
		assertEquals("Container Name shall be containerName1", "containerName1", classUnderTest.getContainerName());
	}

	@Test
	public void testGetLocalizedContainerName()
	{
		Assert.assertNull("Container Name shall be null", classUnderTest.getLocalizedContainerName());
	}

	@Test
	public void testSetDataContainerContext()
	{
		final DataContainerContext dataContainerContext = new DataContainerContextDefaultImpl();
		classUnderTest.setDataContainerContext(dataContainerContext);
		assertEquals("DataContainerContext shall be exact", dataContainerContext, classUnderTest.dataContainerContext);
	}

	//	@Test
	//	public void testEncodeHTML()
	//	{
	//		final String input = "NiceText to be HTML encoded with some strange characters ! §$%&/()=+*#'<>|/*-+;:_,.-µ^°1234567890´`~+*";
	//		final String encodedInput = "NiceText&#x20;to&#x20;be&#x20;HTML&#x20;encoded&#x20;with&#x20;some&#x20;strange&#x20;characters&#x20;&#x21;&#x20;&#xa7;&#x24;&#x25;&amp;&#x2f;&#x28;&#x29;&#x3d;&#x2b;&#x2a;&#x23;&#x27;&lt;&gt;&#x7c;&#x2f;&#x2a;-&#x2b;&#x3b;&#x3a;_,.-&#xb5;&#x5e;&#xb0;1234567890&#xb4;&#x60;&#x7e;&#x2b;&#x2a;";
	//		assertEquals("The encoded input should be " + encodedInput, encodedInput, classUnderTest.encodeHTML(input));
	//
	//		ThrowUnsupportedEncodingException = true;
	//		try
	//		{
	//			classUnderTest.encodeHTML("");
	//			assertFalse("Exception should have been thrown", true);
	//		}
	//		catch (final DataContainerRuntimeException e)
	//		{
	//			assertTrue("Expected exception was thrown", true);
	//		}
	//		finally
	//		{
	//			ThrowUnsupportedEncodingException = false;
	//		}
	//		// XSSEncoder is a final class, therefore we cannot mock it to test the catch block
	//	}

	@Test
	public void testAddMessage()
	{
		final Message message = new Message(1);
		message.setResourceKey("test.key");
		classUnderTest.addMessage(message);
		Assert.assertNotNull(classUnderTest.getMessageList());
	}

	@Test
	public void testInitializeMessageList()
	{
		Assert.assertNull(classUnderTest.getMessageList());
		classUnderTest.initializeMessageList();
		Assert.assertNotNull(classUnderTest.getMessageList());
		classUnderTest.initializeMessageList();
		Assert.assertNotNull(classUnderTest.getMessageList());
	}

	@Test
	public void testGetMessageList()
	{
		final Message message = new Message(1);
		message.setResourceKey("test.key");
		classUnderTest.addMessage(message);

		final MessageList retrievedMessageList = classUnderTest.getMessageList();
		final Message retrievedMessage = retrievedMessageList.get(0);

		Assert.assertEquals("The inserted message was not the one returned!", message.getResourceKey(),
				retrievedMessage.getResourceKey());
	}

	@Test
	public void testClearMessageList()
	{
		classUnderTest.clearMessages();
		MessageList retrievedMessageList = classUnderTest.getMessageList();
		Assert.assertEquals("The message list should be null!", null, retrievedMessageList);

		final Message message = new Message(1);
		message.setResourceKey("test.key");
		classUnderTest.addMessage(message);
		retrievedMessageList = classUnderTest.getMessageList();
		classUnderTest.clearMessages();

		Assert.assertEquals("The message list should be empty!", 0, retrievedMessageList.size());
	}

	@Test
	public void testSetI18nService()
	{
		Assert.assertNull(classUnderTest.i18nService);
		classUnderTest.setI18nService(new I18NServiceMock());
		Assert.assertNotNull(classUnderTest.i18nService);
	}

	@Test
	public void testSetMessageSource()
	{
		Assert.assertNull(classUnderTest.messageSource);
		classUnderTest.setMessageSource(this.messageSourceTest);
		Assert.assertNotNull(classUnderTest.messageSource);
	}

	@Test
	public void testSetCmsComponentModel()
	{
		final CMSISCECustomer360ComponentModel cmsComponentModel = new CMSISCECustomer360ComponentModel();
		classUnderTest.cmsComponentModel = null;
		Assert.assertNull(classUnderTest.cmsComponentModel);
		classUnderTest.setCMSComponentModel(cmsComponentModel);
		Assert.assertNotNull(classUnderTest.cmsComponentModel);
	}

	@Test
	public void testDetermineDataForCMSComponent()
	{
		classUnderTest.determineDataForCMSComponent();
		Assert.assertTrue("call of determineDataForCMSComponent successful", true);
	}

	@Test
	public void testUpdateLevelsDescription()
	{
		final String message_key = "instorecs.customer360.tilepopup.levelDescription";
		final I18NService i18NService = new I18NServiceMock();

		classUnderTest.setI18nService(i18NService);
		classUnderTest.setMessageSource(this.messageSourceTest);

		DataContainerPropertyLevel level;
		final List<DataContainerPropertyLevel> levelList = new ArrayList<>();
		level = new DataContainerPropertyLevelDefaultImpl("1", "1", "1000", "", "");
		levelList.add(level);
		level = new DataContainerPropertyLevelDefaultImpl("2", "1001", "2000", "", "");
		levelList.add(level);

		classUnderTest.updateLevelsDescription(levelList);
		assertEquals("The encoded input should be:  + message_key", message_key, levelList.get(0).getDescription());
		assertEquals("The encoded input should be:  + message_key", message_key, levelList.get(1).getDescription());
	}

	@Test
	public void testLogExceptionAndAddMessage()
	{
		final DataContainerPropertyLevelException e = new DataContainerPropertyLevelException();
		final String propertyNameResourceKey = "propertyNameResourceKey";

		classUnderTest.setMessageSource(this.messageSourceTest);
		classUnderTest.setI18nService(new I18NServiceMock());

		classUnderTest.logExceptionAndAddMessage(e, propertyNameResourceKey);
		assertEquals("There should be one message in the container.", 1, classUnderTest.getMessageList().size());
		assertEquals("Message should containe the correct resource key.", "instorecs.customer360.dataContainerLevelError",
				classUnderTest.getMessageList().get(0).getResourceKey());
		assertEquals("Message should containe the correct resource attribute.", "propertyNameResourceKey",
				classUnderTest.getMessageList().get(0).getResourceArgs()[0]);
	}
}
