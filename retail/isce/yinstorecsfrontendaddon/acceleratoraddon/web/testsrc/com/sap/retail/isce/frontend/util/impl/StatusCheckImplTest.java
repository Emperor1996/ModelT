/*****************************************************************************
 Class:        StatusCheckImplTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.frontend.util.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.core.enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sap.retail.isce.UnitTestBase;
import com.sap.retail.isce.facade.exception.StoreContextNotDefinedException;
import com.sap.retail.isce.facade.impl.DefaultInstorecsAssistedServiceFacade;


public class StatusCheckImplTest extends UnitTestBase
{
	private StatusCheckImpl statusCheck;

	@Mock
	private DefaultInstorecsAssistedServiceFacade instorecsAssistedServiceFacade;

	@Override
	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		statusCheck = new StatusCheckImpl();
		statusCheck.setInstorecsContextFacade(instorecsAssistedServiceFacade);

		assertSame(instorecsAssistedServiceFacade, statusCheck.getInstorecsContextFacade());

		suppressLogging();
	}

	@Test
	public void testisHandoverButtonShownNonIntegratedScenarioNonIntegratedScenarioOrderIsCancelled()
	{
		final OrderData orderData = createCancelledOrder();

		final boolean result = statusCheck.isHandoverButtonShownNonIntegratedScenario(orderData);

		assertFalse(result);
	}

	@Test
	public void testisHandoverButtonShownNonIntegratedScenarioIntegratedScenarioOrderIsCancelled()
	{
		final OrderData orderData = createCancelledOrder();

		final boolean result = statusCheck.isHandoverButtonShownIntegratedScenario(orderData);

		assertFalse(result);
	}

	@Test
	public void testisHandoverButtonShownNonIntegratedScenarioNonIntegratedScenarioNoAgentPOS() throws Exception
	{
		final OrderData orderData = createOrderForNonIntegratedScenario();
		given(instorecsAssistedServiceFacade.getEmployeeStore()).willReturn(null);

		boolean result = statusCheck.isHandoverButtonShownNonIntegratedScenario(orderData);
		assertFalse(result);

		given(instorecsAssistedServiceFacade.getEmployeeStore()).willThrow(new StoreContextNotDefinedException("No Store"));
		result = statusCheck.isHandoverButtonShownNonIntegratedScenario(orderData);
		assertFalse(result);
	}

	@Test
	public void testisHandoverButtonShownNonIntegratedScenarioIntegratedScenarioNoAgentPOS() throws Exception
	{
		final OrderData orderData = createOrdeForIntegratedScenario();
		given(instorecsAssistedServiceFacade.getEmployeeStore()).willReturn(null);

		boolean result = statusCheck.isHandoverButtonShownIntegratedScenario(orderData);
		assertFalse(result);

		given(instorecsAssistedServiceFacade.getEmployeeStore()).willThrow(new StoreContextNotDefinedException("No Store"));
		result = statusCheck.isHandoverButtonShownIntegratedScenario(orderData);
		assertFalse(result);
	}

	@Test
	public void testisHandoverButtonShownNonIntegratedScenarioNoConsignment() throws Exception
	{
		final OrderData orderData = createOrderForNonIntegratedScenario();
		given(instorecsAssistedServiceFacade.getEmployeeStore()).willReturn(getStore("Walldorf"));

		boolean result = statusCheck.isHandoverButtonShownNonIntegratedScenario(orderData);
		assertFalse(result);

		orderData.setConsignments(new ArrayList<ConsignmentData>());
		result = statusCheck.isHandoverButtonShownNonIntegratedScenario(orderData);
		assertFalse(result);
	}

	@Test
	public void testisHandoverButtonShownIntegratedScenarioNoConsignment() throws Exception
	{
		final OrderData orderData = createOrdeForIntegratedScenario();
		given(instorecsAssistedServiceFacade.getEmployeeStore()).willReturn(getStore("Walldorf"));

		boolean result = statusCheck.isHandoverButtonShownNonIntegratedScenario(orderData);
		assertFalse(result);

		orderData.setConsignments(new ArrayList<ConsignmentData>());
		result = statusCheck.isHandoverButtonShownIntegratedScenario(orderData);
		assertFalse(result);
	}

	@Test
	public void testisHandoverButtonShownNonIntegratedScenarioNoConsignmentNoCheck() throws Exception
	{
		final OrderData orderData = createOrderForNonIntegratedScenario();
		given(instorecsAssistedServiceFacade.getEmployeeStore()).willReturn(getStore("Walldorf"));

		statusCheck.setNoConsignmentCheck(true);
		boolean result = statusCheck.isHandoverButtonShownNonIntegratedScenario(orderData);
		assertTrue(result);

		orderData.setConsignments(new ArrayList<ConsignmentData>());
		result = statusCheck.isHandoverButtonShownNonIntegratedScenario(orderData);
		assertTrue(result);
	}

	@Test
	public void testisHandoverButtonShownIntegratedScenarioNoConsignmentNoCheck() throws Exception
	{
		final OrderData orderData = createOrdeForIntegratedScenario();
		given(instorecsAssistedServiceFacade.getEmployeeStore()).willReturn(getStore("Walldorf"));

		statusCheck.setNoConsignmentCheck(true);
		boolean result = statusCheck.isHandoverButtonShownIntegratedScenario(orderData);
		assertTrue(result);

		orderData.setConsignments(new ArrayList<ConsignmentData>());
		result = statusCheck.isHandoverButtonShownIntegratedScenario(orderData);
		assertTrue(result);
	}

	@Test
	public void testisHandoverButtonShownNonIntegratedScenarioConsignmentNotReady() throws Exception
	{
		final OrderData orderData = createOrderForNonIntegratedScenario();
		given(instorecsAssistedServiceFacade.getEmployeeStore()).willReturn(getStore("Walldorf"));

		orderData.setConsignments(getListOfConsignment("Walldorf", ConsignmentStatus.WAITING));

		final boolean result = statusCheck.isHandoverButtonShownNonIntegratedScenario(orderData);
		assertFalse(result);
	}

	@Test
	public void testisHandoverButtonShownIntegratedScenarioConsignmentNotCorrect() throws Exception
	{
		final OrderData orderData = createOrdeForIntegratedScenario();
		orderData.setConsignments(getListOfConsignment("Walldorf", ConsignmentStatus.READY_FOR_PICKUP));
		given(instorecsAssistedServiceFacade.getEmployeeStore()).willReturn(getStore("Walldorf"));

		final boolean result = statusCheck.isHandoverButtonShownIntegratedScenario(orderData);
		assertFalse(result);
	}

	@Test
	public void testisHandoverButtonShownNonIntegratedScenarioWrongPOS() throws Exception
	{
		final OrderData orderData = createOrderForNonIntegratedScenario();
		orderData.setConsignments(getListOfConsignment("Walldorf", ConsignmentStatus.READY_FOR_PICKUP));
		given(instorecsAssistedServiceFacade.getEmployeeStore()).willReturn(getStore("Mannheim"));

		final boolean result = statusCheck.isHandoverButtonShownNonIntegratedScenario(orderData);
		assertFalse(result);
	}

	@Test
	public void testisHandoverButtonShownIntegratedScenarioWrongPOS() throws Exception
	{
		final OrderData orderData = createOrdeForIntegratedScenario();
		addOrderEntry(orderData);
		orderData.setConsignments(getListOfConsignment("Walldorf", ConsignmentStatus.WAITING));
		given(instorecsAssistedServiceFacade.getEmployeeStore()).willReturn(getStore("Mannheim"));

		final boolean result = statusCheck.isHandoverButtonShownIntegratedScenario(orderData);
		assertFalse(result);
	}

	@Test
	public void testisHandoverButtonShownNonIntegratedScenario() throws Exception
	{
		final OrderData orderData = createOrderForNonIntegratedScenario();
		orderData.setConsignments(getListOfConsignment("Walldorf", ConsignmentStatus.READY_FOR_PICKUP));
		given(instorecsAssistedServiceFacade.getEmployeeStore()).willReturn(getStore("Walldorf"));

		final boolean result = statusCheck.isHandoverButtonShownNonIntegratedScenario(orderData);
		assertTrue(result);
	}

	@Test
	public void testisHandoverButtonShownIntegratedScenario() throws Exception
	{
		final OrderData orderData = createOrdeForIntegratedScenario();
		addOrderEntry(orderData);
		orderData.setConsignments(getListOfConsignment("Walldorf", ConsignmentStatus.WAITING));
		given(instorecsAssistedServiceFacade.getEmployeeStore()).willReturn(getStore("Walldorf"));

		final boolean result = statusCheck.isHandoverButtonShownIntegratedScenario(orderData);
		assertTrue(result);
	}

	@Test
	public void testisHandoverButtonShownViaIntegratedScenario() throws Exception
	{
		final OrderData orderData = createOrdeForIntegratedScenario();
		addOrderEntry(orderData);
		orderData.setConsignments(getListOfConsignment("Walldorf", ConsignmentStatus.WAITING));
		given(instorecsAssistedServiceFacade.getEmployeeStore()).willReturn(getStore("Walldorf"));

		final Boolean result = statusCheck.isHandoverButtonShown(orderData);
		assertTrue(result.booleanValue());
	}

	@Test
	public void testisHandoverButtonShownViaNonIntegratedScenario() throws Exception
	{
		final OrderData orderData = createOrderForNonIntegratedScenario();
		addOrderEntry(orderData);
		orderData.setConsignments(getListOfConsignment("Walldorf", ConsignmentStatus.READY_FOR_PICKUP));
		given(instorecsAssistedServiceFacade.getEmployeeStore()).willReturn(getStore("Walldorf"));

		final Boolean result = statusCheck.isHandoverButtonShown(orderData);
		assertTrue(result.booleanValue());
	}

	@Test
	public void testisOrderValidForIntegratedScenario() throws Exception
	{
		OrderData orderData = createOrderForNonIntegratedScenario();
		boolean result = statusCheck.isOrderValidForIntegratedScenario(orderData);
		assertFalse(result);

		orderData.setStatus(OrderStatus.CREATED);
		result = statusCheck.isOrderValidForIntegratedScenario(orderData);
		assertFalse(result);

		orderData = createOrdeForIntegratedScenario();
		result = statusCheck.isOrderValidForIntegratedScenario(orderData);
		assertTrue(result);

		orderData.setStatus(OrderStatus.CHECKED_VALID);
		result = statusCheck.isOrderValidForIntegratedScenario(orderData);
		assertTrue(result);

		orderData.setStatus(OrderStatus.CHECKED_INVALID);
		result = statusCheck.isOrderValidForIntegratedScenario(orderData);
		assertFalse(result);

		orderData.getDeliveryMode().setCode("something");
		result = statusCheck.isOrderValidForIntegratedScenario(orderData);
		assertFalse(result);
	}

	@Test
	public void testIsInvoiceLinkShown() throws Exception
	{
		final OrderData orderData = createCancelledOrder();
		Boolean result = statusCheck.isInvoiceLinkShown(orderData);
		assertFalse(result.booleanValue());

		orderData.setStatus(OrderStatus.COMPLETED);
		orderData.setConsignments(getListOfConsignment("Walldorf", ConsignmentStatus.READY_FOR_PICKUP));
		given(instorecsAssistedServiceFacade.getEmployeeStore()).willReturn(null);
		result = statusCheck.isInvoiceLinkShown(orderData);
		assertFalse(result.booleanValue());

		addOrderEntry(orderData);
		given(instorecsAssistedServiceFacade.getEmployeeStore()).willReturn(getStore("Walldorf"));
		result = statusCheck.isInvoiceLinkShown(orderData);
		assertFalse(result.booleanValue());

		orderData.setConsignments(getListOfConsignment("Walldorf", ConsignmentStatus.SHIPPED));
		result = statusCheck.isInvoiceLinkShown(orderData);
		assertTrue(result.booleanValue());

		given(instorecsAssistedServiceFacade.getEmployeeStore()).willReturn(getStore("Mannheim"));
		result = statusCheck.isInvoiceLinkShown(orderData);
		assertFalse(result.booleanValue());
	}

	private PointOfServiceData getStore(final String name)
	{
		final PointOfServiceData pos = new PointOfServiceData();
		pos.setName(name);

		return pos;
	}

	private List<ConsignmentData> getListOfConsignment(final String posName, final ConsignmentStatus status)
	{
		final List<ConsignmentData> consignments = new ArrayList<>();

		final ConsignmentData consignment = new ConsignmentData();
		consignment.setDeliveryPointOfService(getStore(posName));
		consignment.setStatus(status);

		consignments.add(consignment);

		return consignments;
	}

	protected OrderData createCancelledOrder()
	{
		final OrderData orderData = new OrderData();
		orderData.setStatus(OrderStatus.CANCELLED);
		return orderData;
	}

	protected OrderData createOrdeForIntegratedScenario()
	{
		final OrderData orderData = new OrderData();
		orderData.setStatus(OrderStatus.CREATED);
		final DeliveryModeData deliveryMode = new DeliveryModeData();
		deliveryMode.setCode("pickup");
		orderData.setDeliveryMode(deliveryMode);
		return orderData;
	}

	protected OrderData createOrderForNonIntegratedScenario()
	{
		final OrderData orderData = new OrderData();
		orderData.setStatus(OrderStatus.ORDER_SPLIT);
		return orderData;
	}

	protected void addOrderEntry(final OrderData orderData)
	{
		final List<OrderEntryData> entries = new ArrayList();
		final OrderEntryData orderEntry = new OrderEntryData();
		final PointOfServiceData deliveryPointOfService = new PointOfServiceData();
		deliveryPointOfService.setName("Walldorf");
		orderEntry.setDeliveryPointOfService(deliveryPointOfService);
		entries.add(orderEntry);
		orderData.setEntries(entries);
	}

}
