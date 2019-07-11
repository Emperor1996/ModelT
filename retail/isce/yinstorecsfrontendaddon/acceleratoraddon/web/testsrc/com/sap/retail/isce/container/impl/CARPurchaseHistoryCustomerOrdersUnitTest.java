/*****************************************************************************
 Class:        CARPurchaseHistoryCustomerOrdersUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;


/**
 * Unit Test class for CARPurchaseHistoryCustomerOrders
 */
@UnitTest
public class CARPurchaseHistoryCustomerOrdersUnitTest
{
	private CARPurchaseHistoryCustomerOrders classUnderTest;
	private final Date date = new Date();
	private final PriceData grossSalesVolumePrice = new PriceData();
	private static final String PURCHASE_TYPE = "purchaseType";
	private static final String ORDER_NUMBER = "orderNumber";

	@Before
	public void setUp()
	{
		classUnderTest = new CARPurchaseHistoryCustomerOrders(date, grossSalesVolumePrice, PURCHASE_TYPE, ORDER_NUMBER);
	}

	@Test
	public void testConstructor()
	{
		assertNotNull("classUnderTest is null", classUnderTest);
		assertEquals("getDate - not equal ", date, classUnderTest.getDate());
		assertEquals("getGrossSalesVolumePrice - not equal ", grossSalesVolumePrice, classUnderTest.getGrossSalesVolumePrice());
		assertEquals("getPurchaseType - not equal ", PURCHASE_TYPE, classUnderTest.getPurchaseType());
		assertEquals("getOrderNumber - not equal ", ORDER_NUMBER, classUnderTest.getOrderNumber());
	}

	@Test
	public void testSetters()
	{
		classUnderTest.setDate(date);
		assertEquals("getDate - not equal ", date, classUnderTest.getDate());

		classUnderTest.setGrossSalesVolumePrice(grossSalesVolumePrice);
		assertEquals("getGrossSalesVolumePrice - not equal ", grossSalesVolumePrice, classUnderTest.getGrossSalesVolumePrice());

		classUnderTest.setOrderNumber(ORDER_NUMBER);
		assertEquals("getPurchaseType - not equal ", PURCHASE_TYPE, classUnderTest.getPurchaseType());

		classUnderTest.setPurchaseType(PURCHASE_TYPE);
		assertEquals("getOrderNumber - not equal ", ORDER_NUMBER, classUnderTest.getOrderNumber());
	}

}
