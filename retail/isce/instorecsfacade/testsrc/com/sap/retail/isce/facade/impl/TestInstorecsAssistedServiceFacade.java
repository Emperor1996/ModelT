/*****************************************************************************
 Class:        TestInstorecsAssistedServiceFacade
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceAgentNotLoggedInException;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceException;
import de.hybris.platform.assistedserviceservices.utils.AssistedServiceSession;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.storelocator.StoreLocatorFacade;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.stock.CommerceStockService;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.impl.DefaultCartService;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.Session;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.sap.retail.isce.facade.BaseInstorecsFacadeTest;
import com.sap.retail.isce.facade.constants.InstorecsfacadeConstants;
import com.sap.retail.isce.facade.exception.StoreContextNotDefinedException;
import com.sap.retail.isce.sap.exception.SAPISCEException;
import com.sap.retail.isce.service.sap.OrderHandOverOutput.Status;
import com.sap.retail.isce.service.sap.OrderHandoverInput;
import com.sap.retail.isce.service.sap.OrderHandoverInput.Operation;
import com.sap.retail.isce.service.sap.OrderSapRetailIntegrationService;
import com.sap.retail.isce.service.sap.impl.OrderHandOverInputImpl;
import com.sap.retail.isce.service.sap.impl.OrderHandOverOutputImpl;


@UnitTest
public class TestInstorecsAssistedServiceFacade extends BaseInstorecsFacadeTest
{
	@Spy
	private DefaultInstorecsAssistedServiceFacade instorecsAssistedServiceFacade;
	@Spy
	private DefaultInstorecsAssistedServiceService assistedServiceService;

	@Mock
	private SessionService sessionService;
	@Mock
	private Session session;
	@Mock
	private UserService userService;
	@Mock
	private StoreLocatorFacade storeLocator;
	@Mock
	private CommerceStockService commerceStockService;
	@Mock
	private OrderSapRetailIntegrationService orderIntegrationService;
	@Mock
	private CustomerAccountService custAccService;
	@Mock
	private BaseStoreService baseStoreService;
	@Mock
	private ModelService modelService;
	@Mock
	private Converter<UserModel, CustomerData> customerConverter;
	@Mock
	private AssistedServiceSession assistedServiceSession;
	@Mock
	private UserModel userModel;


	@Before
	public void setUp()
	{
		Logger.getLogger(DefaultInstorecsAssistedServiceFacade.class.getName()).setLevel(Level.DEBUG);

		MockitoAnnotations.initMocks(this);

		instorecsAssistedServiceFacade = Mockito.spy(new DefaultInstorecsAssistedServiceFacade());
		instorecsAssistedServiceFacade.setSessionService(sessionService);
		instorecsAssistedServiceFacade.setUserService(userService);
		instorecsAssistedServiceFacade.setStoreLocatorFacade(storeLocator);
		instorecsAssistedServiceFacade.setCommerceStockService(commerceStockService);
		instorecsAssistedServiceFacade.setOrderIntegrationSerivce(orderIntegrationService);
		assistedServiceService.setSessionService(sessionService);
		assistedServiceService.setCustomerAccountService(custAccService);
		instorecsAssistedServiceFacade.setAssistedServiceService(assistedServiceService);
		instorecsAssistedServiceFacade.setBaseStoreService(baseStoreService);
		instorecsAssistedServiceFacade.setModelService(modelService);
		instorecsAssistedServiceFacade.setCustomerConverter(customerConverter);
	}

	@Test(expected = AssistedServiceAgentNotLoggedInException.class)
	public void testGetStoreNameNotLoggedOn() throws Exception
	{
		Mockito.doReturn(Boolean.FALSE).when(instorecsAssistedServiceFacade).isAssistedServiceAgentLoggedIn();
		instorecsAssistedServiceFacade.getEmployeeStore();
	}

	@Test(expected = StoreContextNotDefinedException.class)
	public void testGetStoreNameStoreContextNotSet() throws Exception
	{
		Mockito.doReturn(Boolean.TRUE).when(instorecsAssistedServiceFacade).isAssistedServiceAgentLoggedIn();
		given(sessionService.getCurrentSession()).willReturn(session);
		given(userService.getUserForUID(AGENT_UID)).willReturn(getStoreEmployee());
		given(storeLocator.getPOSForName(STORE_NAME)).willReturn(null);
		given(session.getAttribute(InstorecsfacadeConstants.AGENTSTORE)).willReturn(null);
		given(session.getAttribute(InstorecsfacadeConstants.AS_AGENT_ID_SESSION_ATTRIBUTE)).willReturn(AGENT_UID);

		final BaseStoreModel baseStoreModel = new BaseStoreModel();
		baseStoreModel.setPointsOfService(Collections.singletonList(getStore()));
		given(baseStoreService.getCurrentBaseStore()).willReturn(baseStoreModel);

		given(instorecsAssistedServiceFacade.getAsmSession()).willReturn(assistedServiceSession);
		given(assistedServiceSession.getAgent()).willReturn(userModel);
		given(userModel.getUid()).willReturn(AGENT_UID);

		instorecsAssistedServiceFacade.getEmployeeStore();
	}

	@Test
	public void testGetAssistedServiceSessionAttributes() throws Exception
	{
		Mockito.doReturn(Boolean.TRUE).when(instorecsAssistedServiceFacade).isAssistedServiceAgentLoggedIn();

		given(instorecsAssistedServiceFacade.getAsmSession()).willReturn(assistedServiceSession);

		final Map<String, Object> attributes = new HashMap<>();
		given(assistedServiceSession.getAsmSessionParametersMap()).willReturn(attributes);

		given(sessionService.getCurrentSession()).willReturn(session);
		given(session.getAttribute(DefaultCartService.SESSION_CART_PARAMETER_NAME)).willReturn(null);

		final Map<String, Object> theAttributes = new HashMap<>();
		Mockito.doReturn(theAttributes).when(instorecsAssistedServiceFacade).getSuperAssistedServiceSessionAttributes();

		given(instorecsAssistedServiceFacade.getAsmSession()).willReturn(assistedServiceSession);
		given(assistedServiceSession.getAgent()).willReturn(userModel);
		given(userModel.getUid()).willReturn(AGENT_UID);

		given(instorecsAssistedServiceFacade.getAsmSession()).willReturn(assistedServiceSession);
		given(assistedServiceSession.getAgent()).willReturn(userModel);
		given(userModel.getName()).willReturn(AGENT_NAME);

		final Map<String, Object> assistedServiceSessionAttributes = instorecsAssistedServiceFacade
				.getAssistedServiceSessionAttributes();
		assertNotNull("assistedServiceSessionAttributes is null", assistedServiceSessionAttributes);

		assertEquals("Number of entries not = 2", assistedServiceSessionAttributes.size(), 2);
		assertEquals("agentName not equals to Sally", assistedServiceSessionAttributes.get("agentName"), "Sally");
		assertEquals("agentUID not equals to sally", assistedServiceSessionAttributes.get("agentUID"), "sally");
	}

	@Test
	public void testGetStoreName() throws Exception
	{
		Mockito.doReturn(Boolean.TRUE).when(instorecsAssistedServiceFacade).isAssistedServiceAgentLoggedIn();
		given(sessionService.getCurrentSession()).willReturn(session);
		given(session.getAttribute(InstorecsfacadeConstants.AGENTSTORE)).willReturn(getStoreDAO());
		final PointOfServiceData posData = instorecsAssistedServiceFacade.getEmployeeStore();

		assertEquals("Wrong store name found", STORE_NAME, posData.getName());
	}

	@Test(expected = AssistedServiceAgentNotLoggedInException.class)
	public void testSetStoreContextNotLoggedOn() throws Exception
	{
		Mockito.doReturn(Boolean.FALSE).when(instorecsAssistedServiceFacade).isAssistedServiceAgentLoggedIn();
		instorecsAssistedServiceFacade.determineStoreContext();
	}

	@Test(expected = StoreContextNotDefinedException.class)
	public void testSetStoreContextWithStoreAssignment() throws Exception
	{
		Mockito.doReturn(Boolean.TRUE).when(instorecsAssistedServiceFacade).isAssistedServiceAgentLoggedIn();
		given(session.getAttribute(InstorecsfacadeConstants.AS_AGENT_ID_SESSION_ATTRIBUTE)).willReturn(AGENT_UID);
		given(sessionService.getCurrentSession()).willReturn(session);
		given(userService.getUserForUID(AGENT_UID)).willReturn(getStoreEmployeeWithoutGroups());

		given(instorecsAssistedServiceFacade.getAsmSession()).willReturn(assistedServiceSession);
		given(assistedServiceSession.getAgent()).willReturn(userModel);
		given(userModel.getUid()).willReturn(AGENT_UID);


		instorecsAssistedServiceFacade.determineStoreContext();
	}


	@Test
	public void testSetStoreContext() throws Exception
	{
		Mockito.doReturn(Boolean.TRUE).when(instorecsAssistedServiceFacade).isAssistedServiceAgentLoggedIn();
		given(session.getAttribute(InstorecsfacadeConstants.AS_AGENT_ID_SESSION_ATTRIBUTE)).willReturn(AGENT_UID);
		given(sessionService.getCurrentSession()).willReturn(session);
		given(userService.getUserForUID(AGENT_UID)).willReturn(getStoreEmployee());
		given(storeLocator.getPOSForName(STORE_NAME)).willReturn(getStoreDAO());
		final BaseStoreModel baseStoreModel = new BaseStoreModel();
		baseStoreModel.setPointsOfService(Collections.singletonList(getStore()));
		given(baseStoreService.getCurrentBaseStore()).willReturn(baseStoreModel);

		given(instorecsAssistedServiceFacade.getAsmSession()).willReturn(assistedServiceSession);
		given(assistedServiceSession.getAgent()).willReturn(userModel);
		given(userModel.getUid()).willReturn(AGENT_UID);


		instorecsAssistedServiceFacade.determineStoreContext();
		verify(session, times(1)).setAttribute(eq(InstorecsfacadeConstants.AGENTSTORE), eq(getStoreDAO()));
	}

	@Test
	public void testGetProductQtyInCurrentStoreNotLoggedIn()
	{
		Mockito.doReturn(Boolean.FALSE).when(instorecsAssistedServiceFacade).isAssistedServiceAgentLoggedIn();

		final Long storeStockQty = instorecsAssistedServiceFacade.getProductQtyInCurrentStore(null);

		assertNull(storeStockQty);
	}

	@Test
	public void testGetProductQtyInCurrentStoreNoStoreAssigned()
	{
		Mockito.doReturn(Boolean.TRUE).when(instorecsAssistedServiceFacade).isAssistedServiceAgentLoggedIn();
		given(sessionService.getCurrentSession()).willReturn(session);
		given(session.getAttribute(InstorecsfacadeConstants.AGENTSTORE_MODEL)).willReturn(null);

		final Long storeStockQty = instorecsAssistedServiceFacade.getProductQtyInCurrentStore(null);

		assertNull(storeStockQty);
	}

	@Test
	public void testGetProductQtyInCurrentStore()
	{
		final ProductModel product = new ProductModel();
		Mockito.doReturn(Boolean.TRUE).when(instorecsAssistedServiceFacade).isAssistedServiceAgentLoggedIn();
		given(sessionService.getCurrentSession()).willReturn(session);
		given(session.getAttribute(InstorecsfacadeConstants.AGENTSTORE_MODEL)).willReturn(getStore());
		given(commerceStockService.getStockLevelForProductAndPointOfService(product, getStore())).willReturn(Long.valueOf(10));


		final Long storeStockQty = instorecsAssistedServiceFacade.getProductQtyInCurrentStore(product);

		assertEquals(Long.valueOf(10), storeStockQty);
	}

	@Test
	public void testPrepareHandover()
	{
		final OrderHandOverInputImpl input = instorecsAssistedServiceFacade.prepareHandover("123");
		assertEquals("123", input.getOrderId());
		assertTrue(input.isGetInvoiceAsPDF());
		assertEquals(Operation.GOODSISSUE, input.getOperations().get(0));
		assertEquals(Operation.INVOICE, input.getOperations().get(1));
	}

	@Test
	public void testPrepareHandoverTestMode()
	{
		instorecsAssistedServiceFacade.setAutoCreateDeliveryAndPick(true);
		final OrderHandOverInputImpl input = instorecsAssistedServiceFacade.prepareHandover("123");
		assertEquals("123", input.getOrderId());
		assertTrue(input.isGetInvoiceAsPDF());
		assertEquals(Operation.DELIVERY, input.getOperations().get(0));
		assertEquals(Operation.PICK, input.getOperations().get(1));
		assertEquals(Operation.GOODSISSUE, input.getOperations().get(2));
		assertEquals(Operation.INVOICE, input.getOperations().get(3));
	}

	@Test
	public void testHandoverOrderStatusCompleted()
	{
		//prepare
		final OrderHandOverOutputImpl output = new OrderHandOverOutputImpl();
		output.setStatus(Status.OK);
		try
		{
			given(orderIntegrationService.handoverOrder(Mockito.any(OrderHandoverInput.class))).willReturn(output);
		}
		catch (final SAPISCEException e1)
		{
			assertFalse("SAPISCEException was thrown", true);
		}
		final BaseStoreModel baseStoreModel = new BaseStoreModel();
		given(baseStoreService.getCurrentBaseStore()).willReturn(baseStoreModel);
		final OrderModel orderModel = new OrderModel();
		final Set<ConsignmentModel> consignments = new HashSet<>();
		consignments.add(new ConsignmentModel());
		consignments.add(new ConsignmentModel());
		orderModel.setConsignments(consignments);
		given(custAccService.getOrderForCode("123", baseStoreModel)).willReturn(orderModel);

		//test
		try
		{
			instorecsAssistedServiceFacade.handOverOrder("123");
			assertEquals(OrderStatus.COMPLETED, orderModel.getStatus());
			assertEquals(de.hybris.platform.core.enums.DeliveryStatus.SHIPPED, orderModel.getDeliveryStatus());
			//check all consigments
			for (final ConsignmentModel consigment : orderModel.getConsignments())
			{
				assertEquals(ConsignmentStatus.SHIPPED, consigment.getStatus());
			}
		}
		catch (final SAPISCEException e)
		{
			assertFalse("SAPISCEException was thrown", true);
		}
	}

	@Test
	public void testHandoverOrderStatusFailed()
	{
		//prepare
		final OrderHandOverOutputImpl output = new OrderHandOverOutputImpl();
		output.setStatus(Status.ERROR);
		try
		{
			given(orderIntegrationService.handoverOrder(Mockito.any(OrderHandoverInput.class))).willReturn(output);
		}
		catch (final SAPISCEException e1)
		{
			assertFalse("SAPISCEException was thrown", true);
		}
		final BaseStoreModel baseStoreModel = new BaseStoreModel();
		given(baseStoreService.getCurrentBaseStore()).willReturn(baseStoreModel);
		final OrderModel orderModel = new OrderModel();
		final Set<ConsignmentModel> consignments = new HashSet<>();
		consignments.add(new ConsignmentModel());
		consignments.add(new ConsignmentModel());
		orderModel.setConsignments(consignments);
		given(custAccService.getOrderForCode("123", baseStoreModel)).willReturn(orderModel);

		//test
		try
		{
			instorecsAssistedServiceFacade.handOverOrder("123");
			assertEquals(OrderStatus.PROCESSING_ERROR, orderModel.getStatus());
			Assert.assertNotEquals(de.hybris.platform.core.enums.DeliveryStatus.SHIPPED, orderModel.getDeliveryStatus());
			//check all consigments
			for (final ConsignmentModel consigment : orderModel.getConsignments())
			{
				Assert.assertNotEquals(ConsignmentStatus.SHIPPED, consigment.getStatus());
			}
		}
		catch (final SAPISCEException e)
		{
			assertFalse("SAPISCEException was thrown", true);
		}
	}

	@Test
	public void testSetterGetter()
	{
		assertSame("orderIntegrationService not identical", instorecsAssistedServiceFacade.getOrderIntegrationSerivce(),
				orderIntegrationService);
	}

	@Test
	public void testGetInvoiceDocumentForOrderCode()
	{
		final byte[] data = new byte[10];
		try
		{
			given(orderIntegrationService.getInvoiceForOrder("test")).willReturn(data);
		}
		catch (final SAPISCEException e1)
		{
			assertFalse("SAPISCEException was thrown", true);
		}

		byte[] invoiceDocument;
		try
		{
			invoiceDocument = instorecsAssistedServiceFacade.getInvoiceDocumentForOrderCode("test");
			assertEquals("invoiceDocument is not identical", invoiceDocument, data);
		}
		catch (final SAPISCEException e)
		{
			assertFalse("SAPISCEException was thrown", true);
		}
	}

	@Test
	public void testLoginAssistedServiceAgent()
	{
		final String username = null;
		final String password = null;
		try
		{
			Mockito.doNothing().when(instorecsAssistedServiceFacade).superLoginAssistedServiceAgent(username, password);
		}
		catch (final AssistedServiceException e1)
		{
			assertTrue("Test failed", false);
		}
		Mockito.doReturn(Boolean.TRUE).when(instorecsAssistedServiceFacade).isAssistedServiceAgentLoggedIn();
		given(session.getAttribute(InstorecsfacadeConstants.AS_AGENT_ID_SESSION_ATTRIBUTE)).willReturn(AGENT_UID);
		given(sessionService.getCurrentSession()).willReturn(session);
		given(userService.getUserForUID(AGENT_UID)).willReturn(getStoreEmployee());
		given(storeLocator.getPOSForName(STORE_NAME)).willReturn(getStoreDAO());
		final BaseStoreModel baseStoreModel = new BaseStoreModel();
		baseStoreModel.setPointsOfService(Collections.singletonList(getStore()));
		given(baseStoreService.getCurrentBaseStore()).willReturn(baseStoreModel);

		given(instorecsAssistedServiceFacade.getAsmSession()).willReturn(assistedServiceSession);
		given(assistedServiceSession.getAgent()).willReturn(userModel);
		given(userModel.getUid()).willReturn(AGENT_UID);

		try
		{
			instorecsAssistedServiceFacade.loginAssistedServiceAgent(username, password);
			assertTrue("Test succeeded", true);
		}
		catch (final AssistedServiceException e)
		{
			assertTrue("Test failed", false);
		}
	}

	@Test
	public void testLoginAssistedServiceAgentException()
	{
		final String username = null;
		final String password = null;
		try
		{
			Mockito.doNothing().when(instorecsAssistedServiceFacade).superLoginAssistedServiceAgent(username, password);
		}
		catch (final AssistedServiceException e1)
		{
			assertTrue("Test failed", false);
		}
		Mockito.doReturn(Boolean.TRUE).when(instorecsAssistedServiceFacade).isAssistedServiceAgentLoggedIn();
		given(session.getAttribute(InstorecsfacadeConstants.AS_AGENT_ID_SESSION_ATTRIBUTE)).willReturn(AGENT_UID);
		given(sessionService.getCurrentSession()).willReturn(session);
		given(userService.getUserForUID(AGENT_UID)).willReturn(null);
		given(storeLocator.getPOSForName(STORE_NAME)).willReturn(getStoreDAO());
		final BaseStoreModel baseStoreModel = new BaseStoreModel();
		baseStoreModel.setPointsOfService(null);
		given(baseStoreService.getCurrentBaseStore()).willReturn(baseStoreModel);

		given(instorecsAssistedServiceFacade.getAsmSession()).willReturn(assistedServiceSession);
		given(assistedServiceSession.getAgent()).willReturn(userModel);
		given(userModel.getUid()).willReturn(AGENT_UID);

		try
		{
			instorecsAssistedServiceFacade.loginAssistedServiceAgent(username, password);
			assertTrue("Test failed", false);
		}
		catch (final AssistedServiceException e)
		{
			assertTrue("Test succeeded", true);
		}
	}


	@Test
	public void testLogoutAssistedServiceAgent()
	{
		try
		{
			Mockito.doNothing().when(instorecsAssistedServiceFacade).superLogoutAssistedServiceAgent();
		}
		catch (final AssistedServiceException e1)
		{
			assertTrue("Test failed", false);
		}
		given(sessionService.getCurrentSession()).willReturn(session);
		try
		{
			instorecsAssistedServiceFacade.logoutAssistedServiceAgent();
			verify(session, times(1)).removeAttribute(eq(InstorecsfacadeConstants.AGENTSTORE));
			verify(session, times(1)).removeAttribute(eq(InstorecsfacadeConstants.AGENTSTORE_MODEL));
		}
		catch (final AssistedServiceException e)
		{
			assertTrue("Test failed", false);
		}
	}

}
