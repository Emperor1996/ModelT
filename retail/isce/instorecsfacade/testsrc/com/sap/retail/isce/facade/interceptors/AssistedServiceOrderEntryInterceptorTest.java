/*****************************************************************************
 Class:        AssistedServiceOrderEntryInterceptorTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade.interceptors;

import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.retail.isce.facade.constants.InstorecsfacadeConstants;


@UnitTest
public class AssistedServiceOrderEntryInterceptorTest extends TestCase
{

	private AssistedServiceOrderEntryInterceptor classUnderTest;
	private AbstractOrderEntryModel orderEntryModel;

	@Mock
	private InterceptorContext ctxMock;

	@Mock
	private AssistedServiceFacade assistedServiceFacadeMock;

	@Override
	@Before
	public void setUp()
	{
		Logger.getLogger(AssistedServiceOrderEntryInterceptor.class.getName()).setLevel(Level.DEBUG);
		MockitoAnnotations.initMocks(this);
		classUnderTest = new AssistedServiceOrderEntryInterceptor();
		final Map<String, Object> sessionMap = new HashMap<>();
		sessionMap.put(InstorecsfacadeConstants.AGENTUID, "ASAGENT");
		classUnderTest.setAssistedServiceFacade(assistedServiceFacadeMock);
		when(assistedServiceFacadeMock.getAssistedServiceSessionAttributes()).thenReturn(sessionMap);

		orderEntryModel = new DummyOrderEntryModel();

		assertEquals("assistedServiceFacadeMock not identical", classUnderTest.getAssistedServiceFacade(),
				assistedServiceFacadeMock);
	}

	@Test
	public void testOnPrepareNewItemAsmOn() throws InterceptorException
	{
		when(Boolean.valueOf(assistedServiceFacadeMock.isAssistedServiceAgentLoggedIn())).thenReturn(Boolean.TRUE);
		when(Boolean.valueOf(ctxMock.isNew(orderEntryModel))).thenReturn(Boolean.TRUE);
		classUnderTest.onPrepare(orderEntryModel, ctxMock);
		assertEquals("ASAGENT", orderEntryModel.getEmployeeId());
		assertTrue("Item not flagged as ASM-item", orderEntryModel.getCreatedInAsmMode().booleanValue());
	}

	@Test
	public void testOnPrepareNewItemAsmOff() throws InterceptorException
	{
		when(Boolean.valueOf(assistedServiceFacadeMock.isAssistedServiceAgentLoggedIn())).thenReturn(Boolean.FALSE);
		when(Boolean.valueOf(ctxMock.isNew(orderEntryModel))).thenReturn(Boolean.TRUE);
		classUnderTest.onPrepare(orderEntryModel, ctxMock);
		assertNull(orderEntryModel.getEmployeeId());
		assertFalse("Item flagged as ASM-item", orderEntryModel.getCreatedInAsmMode().booleanValue());
	}

	@Test
	public void testOnPrepareOldItemAsmOn() throws InterceptorException
	{
		when(Boolean.valueOf(assistedServiceFacadeMock.isAssistedServiceAgentLoggedIn())).thenReturn(Boolean.TRUE);
		when(Boolean.valueOf(ctxMock.isNew(orderEntryModel))).thenReturn(Boolean.FALSE);
		classUnderTest.onPrepare(orderEntryModel, ctxMock);
		assertNull(orderEntryModel.getEmployeeId());
		assertNull("createdInAsmMode flag should not be set for existing items", orderEntryModel.getCreatedInAsmMode());
	}


	@Test
	public void testOnPrepareNewItemAsmOnButNotCreatedInAsmMode() throws InterceptorException
	{
		orderEntryModel.setCreatedInAsmMode(Boolean.FALSE);
		when(Boolean.valueOf(assistedServiceFacadeMock.isAssistedServiceAgentLoggedIn())).thenReturn(Boolean.TRUE);
		when(Boolean.valueOf(ctxMock.isNew(orderEntryModel))).thenReturn(Boolean.TRUE);
		classUnderTest.onPrepare(orderEntryModel, ctxMock);
		assertNull("employeeId should not be set for items, that were intially not created in ASM-Mode",
				orderEntryModel.getEmployeeId());
	}

	@Test
	public void testOnPrepareNewItemAsmOnCreatedInAsmMode() throws InterceptorException
	{
		orderEntryModel.setCreatedInAsmMode(Boolean.TRUE);
		orderEntryModel.setEmployeeId("anotherAsmAgent");
		when(Boolean.valueOf(assistedServiceFacadeMock.isAssistedServiceAgentLoggedIn())).thenReturn(Boolean.TRUE);
		when(Boolean.valueOf(ctxMock.isNew(orderEntryModel))).thenReturn(Boolean.TRUE);
		classUnderTest.onPrepare(orderEntryModel, ctxMock);
		assertEquals("original employee Assigment shoul be keept", "anotherAsmAgent", orderEntryModel.getEmployeeId());
	}

	@Test
	public void testOnPrepareNewItemAsmOffCreatedInAsmMode() throws InterceptorException
	{
		orderEntryModel.setCreatedInAsmMode(Boolean.TRUE);
		orderEntryModel.setEmployeeId("anotherAsmAgent");
		when(Boolean.valueOf(assistedServiceFacadeMock.isAssistedServiceAgentLoggedIn())).thenReturn(Boolean.FALSE);
		when(Boolean.valueOf(ctxMock.isNew(orderEntryModel))).thenReturn(Boolean.TRUE);
		classUnderTest.onPrepare(orderEntryModel, ctxMock);
		assertEquals("original employee Assigment shoul be keept", "anotherAsmAgent", orderEntryModel.getEmployeeId());
		assertTrue("Item not flagged as ASM-item", orderEntryModel.getCreatedInAsmMode().booleanValue());
	}





	/**
	 * <b>Performant implementation for unit testing</b><br>
	 * The default constructor of the OrderEntryModel will create an ItemModelContext, for which most of the Hybris core
	 * needs to be loaded, including parsing the localextesions.xml and loading of the corresponding extensions. This is
	 * very time consuming and can take up to ~30 seconds.<br>
	 * But for this test we just need a container to store the employeeId. Hence this implementation passes on a
	 * mockedContext to the super implementation to prevent loading of the hybris core and just overwrites the getter and
	 * setters for the employee to bypass the non-functional mocked context.
	 *
	 */
	private static class DummyOrderEntryModel extends AbstractOrderEntryModel
	{

		public DummyOrderEntryModel()
		{
			super(Mockito.mock(ItemModelContextImpl.class));
		}

		private String employeeId;
		private Boolean createdInAsmMode;


		@Override
		public Boolean getCreatedInAsmMode()
		{
			return createdInAsmMode;
		}


		@Override
		public void setCreatedInAsmMode(final Boolean createdInAsmMode)
		{
			this.createdInAsmMode = createdInAsmMode;
		}


		@Override
		public String getEmployeeId()
		{
			return employeeId;
		}


		@Override
		public void setEmployeeId(final String employeeID)
		{
			this.employeeId = employeeID;
		}

		@Override
		public Integer getEntryNumber()
		{
			return Integer.valueOf(10);
		}


		@Override
		public AbstractOrderModel getOrder()
		{
			return new DummyOrderModel();
		}

		private class DummyOrderModel extends AbstractOrderModel
		{
			public DummyOrderModel()
			{
				super(Mockito.mock(ItemModelContextImpl.class));
			}

			@Override
			public String getCode()
			{
				return "123456";
			}
		}

	}

}
