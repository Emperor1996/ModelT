/*****************************************************************************
    Class:        DefaultOaaOrderEntryContributorTest
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.

 *****************************************************************************/
package com.sap.retail.oaa.orderexchange.outbound.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.sap.core.configuration.SAPConfigurationService;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.OrderEntryCsvColumns;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.sap.retail.oaa.model.constants.SapoaamodelConstants;
import com.sap.retail.oaa.model.model.ScheduleLineModel;
import com.sap.retail.oaa.orderexchange.constants.OaaOrderEntryCsvColumns;


@UnitTest
@SuppressWarnings("javadoc")
public class DefaultOaaOrderEntryContributorTest
{
	private static final Logger logger = Logger.getLogger(DefaultOaaOrderEntryContributorTest.class);

	private DefaultOaaOrderEntryContributor cut;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	private static final Object SHIPPING_POINT = "OAAC";

	private static final String VENDOR_ID = "VEND1";

	private static String CODE;
	private static String SITE_NAME_100;
	private static String ITEM_CATEGORY_NAME = "ITEM_CATEGORY";

	private Integer ENTRY_NUMBER_100_INT;
	private Integer ENTRY_NUMBER_200_INT;
	private Integer ENTRY_NUMBER_300_INT;

	private Date SCHEDULE_LINE_100_DATE;
	private Double SCHEDULE_LINE_100_QUANTITY;
	private Date SCHEDULE_LINE_200_DATE;
	private Double SCHEDULE_LINE_200_QUANTITY;
	private Date SCHEDULE_LINE_300_DATE;
	private Double SCHEDULE_LINE_300_QUANTITY;

	@Before
	public void setUp()
	{
		cut = new DefaultOaaOrderEntryContributor();
		cut.setDatePattern("yyyy-MM-dd HH:mm:ss.S");

		CODE = "Code";
		SITE_NAME_100 = "3600";

		ENTRY_NUMBER_100_INT = Integer.valueOf(100);
		ENTRY_NUMBER_200_INT = Integer.valueOf(200);
		ENTRY_NUMBER_300_INT = Integer.valueOf(300);

		synchronized (sdf)
		{
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			try
			{
				SCHEDULE_LINE_100_DATE = sdf.parse("15/04/2015");
				SCHEDULE_LINE_200_DATE = sdf.parse("16/06/2014");
				SCHEDULE_LINE_300_DATE = sdf.parse("17/08/2018");
			}
			catch (final ParseException e)
			{
				logger.error("Parsing error", e);
			}

		}

		SCHEDULE_LINE_100_QUANTITY = new Double(5);
		SCHEDULE_LINE_200_QUANTITY = new Double(1);
		SCHEDULE_LINE_300_QUANTITY = new Double(3);
	}

	@Test
	public void testGetColumns()
	{
		final Set<String> columns = cut.getColumns();

		assertTrue(columns.contains(OrderCsvColumns.ORDER_ID));
		assertTrue(columns.contains(OrderEntryCsvColumns.ENTRY_NUMBER));
		assertTrue(columns.contains(OaaOrderEntryCsvColumns.SCHEDULE_LINES));
		assertTrue(columns.contains(OaaOrderEntryCsvColumns.SITE_ID));
		assertTrue(columns.contains(OaaOrderEntryCsvColumns.CAC_SHIPPING_POINT));
	}

	@Test
	public void testCreateRowWithSource()
	{
		final OrderModel order = new OrderModel();
		final List<AbstractOrderEntryModel> entries = new ArrayList<AbstractOrderEntryModel>();
		final OrderEntryModel entry1 = new OrderEntryModel();
		final OrderEntryModel entry2 = new OrderEntryModel();
		final PointOfServiceModel site1 = new PointOfServiceModel();

		entry1.setEntryNumber(ENTRY_NUMBER_100_INT);
		site1.setName(SITE_NAME_100);
		entry1.setSapSource(site1);
		entries.add(entry1);

		entry2.setEntryNumber(ENTRY_NUMBER_200_INT);
		entries.add(entry2);

		order.setCode(CODE);
		order.setEntries(entries);

		final List<Map<String, Object>> rows = cut.createRows(order);

		assertEquals(1, rows.size());

		final Map<String, Object> row = rows.get(0);
		assertEquals(CODE, row.get(OrderCsvColumns.ORDER_ID));
		assertEquals(ENTRY_NUMBER_100_INT, row.get(OrderEntryCsvColumns.ENTRY_NUMBER));
		assertEquals(SITE_NAME_100, row.get(OaaOrderEntryCsvColumns.SITE_ID));
		assertEquals(SITE_NAME_100, row.get(OaaOrderEntryCsvColumns.SITE_ID));
	}

	@Test
	public void testCreateRowWithVendor()
	{
		final OrderModel order = new OrderModel();
		final List<AbstractOrderEntryModel> entries = new ArrayList<AbstractOrderEntryModel>();
		final OrderEntryModel entry1 = new OrderEntryModel();
		final OrderEntryModel entry2 = new OrderEntryModel();
		final PointOfServiceModel site1 = new PointOfServiceModel();

		entry1.setEntryNumber(ENTRY_NUMBER_100_INT);
		site1.setName(SITE_NAME_100);
		entry1.setSapSource(site1);
		entry1.setSapVendor(VENDOR_ID);
		entries.add(entry1);

		entries.add(entry2);

		order.setCode(CODE);
		order.setEntries(entries);

		final SAPConfigurationService sapCoreConfigurationServiceMock = EasyMock.createMock(SAPConfigurationService.class);
		EasyMock.expect(sapCoreConfigurationServiceMock.getProperty(SapoaamodelConstants.PROPERTY_SAPOAA_VENDOR_ITEM_CATEGORY))
				.andReturn(ITEM_CATEGORY_NAME).once();
		EasyMock.replay(sapCoreConfigurationServiceMock);
		cut.setSapCoreConfigurationService(sapCoreConfigurationServiceMock);


		final BaseSiteService baseSiteServiceMock = EasyMock.createMock(BaseSiteService.class);
		final BaseSiteModel baseSiteModel = new BaseSiteModel();
		baseSiteServiceMock.setCurrentBaseSite(baseSiteModel, false);
		order.setSite(baseSiteModel);
		cut.setBaseSiteService(baseSiteServiceMock);
		EasyMock.expectLastCall();
		EasyMock.replay(baseSiteServiceMock);

		final List<Map<String, Object>> rows = cut.createRows(order);

		assertEquals(1, rows.size());

		final Map<String, Object> row = rows.get(0);
		assertEquals(CODE, row.get(OrderCsvColumns.ORDER_ID));
		assertEquals(ENTRY_NUMBER_100_INT, row.get(OrderEntryCsvColumns.ENTRY_NUMBER));
		assertEquals(SITE_NAME_100, row.get(OaaOrderEntryCsvColumns.SITE_ID));
		assertEquals(SITE_NAME_100, row.get(OaaOrderEntryCsvColumns.SITE_ID));
		assertEquals(ITEM_CATEGORY_NAME, row.get(OaaOrderEntryCsvColumns.VENDOR_ITEM_CATEGORY));
	}



	@Test
	public void testCreateRowWithShippingPoint()
	{
		final OrderModel order = new OrderModel();
		final List<AbstractOrderEntryModel> entries = new ArrayList<AbstractOrderEntryModel>();
		final OrderEntryModel entry1 = new OrderEntryModel();
		final OrderEntryModel entry2 = new OrderEntryModel();
		final PointOfServiceModel site1 = new PointOfServiceModel();
		site1.setSapoaa_cacShippingPoint("OAAC");

		entry1.setEntryNumber(ENTRY_NUMBER_100_INT);
		site1.setName(SITE_NAME_100);
		entry1.setSapSource(site1);
		entry1.setDeliveryPointOfService(site1);
		entries.add(entry1);

		entry2.setEntryNumber(ENTRY_NUMBER_200_INT);
		entries.add(entry2);

		order.setCode(CODE);
		order.setEntries(entries);

		final List<Map<String, Object>> rows = cut.createRows(order);

		assertEquals(1, rows.size());

		final Map<String, Object> row = rows.get(0);
		assertEquals(CODE, row.get(OrderCsvColumns.ORDER_ID));
		assertEquals(ENTRY_NUMBER_100_INT, row.get(OrderEntryCsvColumns.ENTRY_NUMBER));
		assertEquals(SHIPPING_POINT, row.get(OaaOrderEntryCsvColumns.CAC_SHIPPING_POINT));
	}


	@Test
	public void testCreateRowsWithNoOaaData()
	{
		final OrderModel order = new OrderModel();
		final List<AbstractOrderEntryModel> entries = new ArrayList<AbstractOrderEntryModel>();
		final OrderEntryModel entry1 = new OrderEntryModel();

		entry1.setEntryNumber(ENTRY_NUMBER_100_INT);
		entries.add(entry1);
		order.setEntries(entries);

		final List<Map<String, Object>> rows = cut.createRows(order);

		assertEquals(0, rows.size());
	}

	@Test
	public void testCreateRowsWithScheduleLines()
	{
		final OrderModel order = new OrderModel();
		final List<AbstractOrderEntryModel> entries = new ArrayList<AbstractOrderEntryModel>();
		final OrderEntryModel entry1 = new OrderEntryModel();
		final OrderEntryModel entry2 = new OrderEntryModel();
		final OrderEntryModel entry3 = new OrderEntryModel();
		final List<ScheduleLineModel> scheduleLines1 = new ArrayList<ScheduleLineModel>();
		final List<ScheduleLineModel> scheduleLines2 = new ArrayList<ScheduleLineModel>();
		final List<ScheduleLineModel> scheduleLines3 = new ArrayList<ScheduleLineModel>();
		final ScheduleLineModel scheduleLine1 = new ScheduleLineModel();
		final ScheduleLineModel scheduleLine2 = new ScheduleLineModel();
		final ScheduleLineModel scheduleLine3 = new ScheduleLineModel();

		scheduleLine1.setConfirmedDate(SCHEDULE_LINE_100_DATE);
		scheduleLine1.setConfirmedQuantity(SCHEDULE_LINE_100_QUANTITY);

		scheduleLine2.setConfirmedDate(SCHEDULE_LINE_200_DATE);
		scheduleLine2.setConfirmedQuantity(SCHEDULE_LINE_200_QUANTITY);

		scheduleLine3.setConfirmedDate(SCHEDULE_LINE_300_DATE);
		scheduleLine3.setConfirmedQuantity(SCHEDULE_LINE_300_QUANTITY);

		scheduleLines2.add(scheduleLine1);
		scheduleLines3.add(scheduleLine2);
		scheduleLines3.add(scheduleLine3);

		entry1.setScheduleLines(scheduleLines1); //Has 0 ScheduleLine
		entry2.setScheduleLines(scheduleLines2); //Has 1 ScheduleLine
		entry3.setScheduleLines(scheduleLines3); //Has 2 ScheduleLine

		entry1.setEntryNumber(ENTRY_NUMBER_100_INT);
		entry2.setEntryNumber(ENTRY_NUMBER_200_INT);
		entry3.setEntryNumber(ENTRY_NUMBER_300_INT);

		entries.add(entry1);
		entries.add(entry2);
		entries.add(entry3);
		order.setEntries(entries);
		order.setCode(CODE);

		final List<Map<String, Object>> rows = cut.createRows(order);

		assertEquals(3, rows.size());

		final Map<String, Object> row1 = rows.get(0);
		assertEquals(CODE, row1.get(OrderCsvColumns.ORDER_ID));
		assertEquals(ENTRY_NUMBER_100_INT, row1.get(OrderEntryCsvColumns.ENTRY_NUMBER));
		assertEquals("", row1.get(OaaOrderEntryCsvColumns.SCHEDULE_LINES));

		final Map<String, Object> row2 = rows.get(1);
		assertEquals(CODE, row2.get(OrderCsvColumns.ORDER_ID));
		assertEquals(ENTRY_NUMBER_200_INT, row2.get(OrderEntryCsvColumns.ENTRY_NUMBER));
		assertEquals(SCHEDULE_LINE_100_QUANTITY + ";" + "2015-04-15 00:00:00.0", row2.get(OaaOrderEntryCsvColumns.SCHEDULE_LINES));

		final Map<String, Object> row3 = rows.get(2);
		assertEquals(CODE, row3.get(OrderCsvColumns.ORDER_ID));
		assertEquals(ENTRY_NUMBER_300_INT, row3.get(OrderEntryCsvColumns.ENTRY_NUMBER));
		assertEquals(SCHEDULE_LINE_200_QUANTITY + ";" + "2014-06-16 00:00:00.0" + "/" + SCHEDULE_LINE_300_QUANTITY + ";"
				+ "2018-08-17 00:00:00.0", row3.get(OaaOrderEntryCsvColumns.SCHEDULE_LINES));

	}
}
