/*****************************************************************************
    Class:        DefaultOaaOrderEntryContributorTest
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.

 *****************************************************************************/
package com.sap.retail.oaa.orderexchange.outbound.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;



@UnitTest
@SuppressWarnings("javadoc")
public class DefaultOaaOrderContributorTest
{

	private DefaultOaaOrderContributor cut;

	private String CODE;

	@Before
	public void setUp()
	{
		cut = new DefaultOaaOrderContributor();
		CODE = "Code";
	}

	@Test
	public void testGetColumns()
	{
		final Set<String> columns = cut.getColumns();

		assertTrue(columns.contains(OrderCsvColumns.ORDER_ID));
	}

	@Test
	public void testCreateRow()
	{
		final OrderModel order = new OrderModel();

		order.setCode(CODE);

		final List<Map<String, Object>> rows = cut.createRows(order);

		assertEquals(1, rows.size());

		final Map<String, Object> row = rows.get(0);
		assertEquals(CODE, row.get(OrderCsvColumns.ORDER_ID));
	}
}
