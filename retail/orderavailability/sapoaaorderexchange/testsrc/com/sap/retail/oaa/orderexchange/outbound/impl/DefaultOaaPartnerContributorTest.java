/**
 *
 */
package com.sap.retail.oaa.orderexchange.outbound.impl;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.OrderEntryCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.PartnerCsvColumns;

import java.util.HashMap;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


/**
 * @author SAP
 *
 */
@UnitTest
public class DefaultOaaPartnerContributorTest
{
	private static final String VENDOR = "VEND1";
	private static final Integer ENTRY_NUMBER_100_INT = new Integer(10);
	private static final String CODE = "123";
	private DefaultOaaPartnerContributor classUnderTest = null;

	@Before
	public void setUp()
	{
		classUnderTest = new DefaultOaaPartnerContributor();
		classUnderTest.setBatchIdAttributes(new HashMap<>());

	}

	@Test
	public void testGetColumns()
	{
		final Set<String> columns = classUnderTest.getColumns();

		assertTrue(columns.contains(PartnerCsvColumns.PARTNER_CODE));
		assertTrue(columns.contains(OrderCsvColumns.ORDER_ID));
		assertTrue(columns.contains(OrderEntryCsvColumns.ENTRY_NUMBER));
		assertTrue(columns.contains(PartnerCsvColumns.PARTNER_ROLE_CODE));


	}

	//	@Test
	//	public void testCreateRows()
	//	{
	//
	//		final OrderModel order = new OrderModel();
	//		final List<AbstractOrderEntryModel> entries = new ArrayList<AbstractOrderEntryModel>();
	//		final OrderEntryModel entry1 = new OrderEntryModel();
	//
	//		entry1.setEntryNumber(ENTRY_NUMBER_100_INT);
	//
	//		order.setCode(CODE);
	//
	//		entries.add(entry1);
	//		entry1.setSapVendor(VENDOR);
	//		order.setEntries(entries);
	//
	//
	//		final List<Map<String, Object>> rows = classUnderTest.createRows(order);
	//
	//		assertEquals(1, rows.size());
	//
	//		final Map<String, Object> row = rows.get(0);
	//		assertEquals(CODE, row.get(OrderCsvColumns.ORDER_ID));
	//		assertEquals(ENTRY_NUMBER_100_INT, row.get(OrderEntryCsvColumns.ENTRY_NUMBER));
	//		assertEquals(VENDOR, row.get(PartnerCsvColumns.PARTNER_CODE));
	//
	//	}

}
