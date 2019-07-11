/*****************************************************************************
 Class:        OrderHandOverOutputImplTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.sap.retail.isce.service.sap.OrderHandoverInput;


@UnitTest
public class OrderHandOverOutputImplTest
{

	private OrderHandOverOutputImpl classUnderTest;


	@Before
	public void setUp()
	{
		classUnderTest = new OrderHandOverOutputImpl();
	}

	@Test
	public void testSplitNull()
	{
		final List<String> idList = classUnderTest.split(null);
		assertEquals(0, idList.size());
	}


	@Test
	public void testSplitEmptyString()
	{
		final List<String> idList = classUnderTest.split("");
		assertEquals(0, idList.size());
	}

	@Test
	public void testSetterGetter()
	{
		assertEquals(Collections.emptyList(), classUnderTest.getGoodsIssueIds());
		assertEquals(Collections.emptyList(), classUnderTest.getPickingIds());
		assertEquals(Collections.emptyList(), classUnderTest.getDeliveryIds());
		assertEquals(Collections.emptyList(), classUnderTest.getInvoiceIds());

		classUnderTest.setDeliveryId("deliveryId");
		assertEquals("deliveryId", classUnderTest.getDeliveryId());

		classUnderTest.setGoodsIssueId("goodsIssueId");
		assertEquals("goodsIssueId", classUnderTest.getGoodsIssueId());

		classUnderTest.setInvoiceId("invoiceId");
		assertEquals("invoiceId", classUnderTest.getInvoiceId());

		classUnderTest.setPickingId("pickingId");
		assertEquals("pickingId", classUnderTest.getPickingId());

		final com.sap.retail.isce.service.sap.OrderHandoverInput.Operation failedOp = com.sap.retail.isce.service.sap.OrderHandoverInput.Operation.DELIVERY;
		classUnderTest.setFailedOp(failedOp);
		assertEquals(com.sap.retail.isce.service.sap.OrderHandoverInput.Operation.DELIVERY, classUnderTest.getFailedOp());

		final OrderHandoverInput orderHandoverInput = new OrderHandOverInputImpl();
		classUnderTest.setInput(orderHandoverInput);
		assertEquals(orderHandoverInput, classUnderTest.getInput());

		final byte[] invoiceAsPDF = null;
		classUnderTest.setInvoiceAsPDF(invoiceAsPDF);
		assertEquals(invoiceAsPDF, classUnderTest.getInvoiceAsPDF());

		classUnderTest.setMessages(Collections.emptyList());
		assertEquals(Collections.emptyList(), classUnderTest.getMessages());

		classUnderTest.setStatus(com.sap.retail.isce.service.sap.OrderHandOverOutput.Status.ERROR);
		assertEquals(com.sap.retail.isce.service.sap.OrderHandOverOutput.Status.ERROR, classUnderTest.getStatus());

	}



	@Test
	public void testSplitOneId()
	{
		final List<String> idList = classUnderTest.split("000123");
		assertEquals(1, idList.size());
	}

	@Test
	public void testSplitTwoIds()
	{
		final List<String> idList = classUnderTest.split("000123 456000");
		assertEquals(2, idList.size());
	}

	@Test
	public void testGetFirstEmpty()
	{
		final String id = classUnderTest.getFirst(Collections.EMPTY_LIST);
		assertNull(id);
	}

	@Test
	public void testGetFirstOneId()
	{
		final String id = classUnderTest.getFirst(Collections.singletonList("000123"));
		assertEquals("000123", id);
	}

	@Test
	public void testGetFirstTwoId()
	{
		final List<String> idList = new ArrayList<>();
		idList.add("000123");
		idList.add("456000");
		final String id = classUnderTest.getFirst(idList);
		assertEquals("000123", id);
	}
}
