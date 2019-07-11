/*****************************************************************************
    Class:        DefaultOaaOrderEntryContributor
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.

 *****************************************************************************/
package com.sap.retail.oaa.orderexchange.outbound.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.sap.core.configuration.SAPConfigurationService;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.OrderEntryCsvColumns;
import de.hybris.platform.sap.orderexchange.outbound.RawItemContributor;
import de.hybris.platform.site.BaseSiteService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.sap.retail.oaa.model.constants.SapoaamodelConstants;
import com.sap.retail.oaa.model.model.ScheduleLineModel;
import com.sap.retail.oaa.orderexchange.constants.OaaOrderEntryCsvColumns;


/**
 * Adds ROCC OAA specific Fields to Order Entry for Order Replication
 */
public class DefaultOaaOrderEntryContributor implements RawItemContributor<OrderModel>
{
	private static final Logger LOG = Logger.getLogger(DefaultOaaOrderEntryContributor.class);

	private final Set<String> columns = new HashSet<>(Arrays.asList(OrderCsvColumns.ORDER_ID, OrderEntryCsvColumns.ENTRY_NUMBER,
			OaaOrderEntryCsvColumns.SITE_ID, OaaOrderEntryCsvColumns.CAC_SHIPPING_POINT, OaaOrderEntryCsvColumns.VENDOR_ITEM_CATEGORY,
			OaaOrderEntryCsvColumns.SCHEDULE_LINES));

	private String datePattern;

	private SAPConfigurationService sapCoreConfigurationService;
	private BaseSiteService baseSiteService;


	/**
	 * @param baseSiteService
	 *           the baseSiteService to set
	 */
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	/**
	 * @param sapCoreConfigurationService
	 *           the sapCoreConfigurationService to set
	 */
	public void setSapCoreConfigurationService(final SAPConfigurationService sapCoreConfigurationService)
	{
		this.sapCoreConfigurationService = sapCoreConfigurationService;
	}

	/**
	 * The date pattern to use when converting Date objects to Strings in UTC timezone This value is read from a property
	 * 'datahubadapter.datahuboutbound.date.pattern'
	 *
	 * @param datePattern
	 *           the date pattern
	 */
	@Required
	public void setDatePattern(final String datePattern)
	{
		this.datePattern = datePattern;
	}

	@Override
	public Set<String> getColumns()
	{
		return columns;
	}

	@Override
	public List<Map<String, Object>> createRows(final OrderModel order)
	{
		final List<AbstractOrderEntryModel> entries = order.getEntries();
		final List<Map<String, Object>> result = new ArrayList<>();

		for (final AbstractOrderEntryModel entry : entries)
		{
			//When no OAA specific fields are filled -> put no entry to result
			if (entry.getScheduleLines() == null && entry.getSapSource() == null && entry.getDeliveryPointOfService() == null)
			{
				continue;
			}

			final Map<String, Object> row = new HashMap<>();
			addOrderEntryKeyFields(order, entry, row);

			//Set ScheduleLines
			if (entry.getScheduleLines() != null)
			{
				addScheduleLines(entry, row);
			}

			//Set Source
			if (entry.getSapSource() != null)
			{
				row.put(OaaOrderEntryCsvColumns.SITE_ID, entry.getSapSource().getName());
			}

			//Set Shipping Point for Click and Collect
			if (entry.getDeliveryPointOfService() != null)
			{
				row.put(OaaOrderEntryCsvColumns.CAC_SHIPPING_POINT, entry.getDeliveryPointOfService().getSapoaa_cacShippingPoint());
			}

			//Set item category for vendor delivered items
			if (entry.getSapVendor() != null)
			{
				baseSiteService.setCurrentBaseSite(order.getSite(), false);

				row.put(OaaOrderEntryCsvColumns.VENDOR_ITEM_CATEGORY,
						sapCoreConfigurationService.getProperty(SapoaamodelConstants.PROPERTY_SAPOAA_VENDOR_ITEM_CATEGORY));
			}


			result.add(row);
		}
		return result;
	}

	/**
	 * @param order
	 * @param entry
	 * @param row
	 */
	public void addOrderEntryKeyFields(final OrderModel order, final AbstractOrderEntryModel entry, final Map<String, Object> row)
	{
		row.put(OrderCsvColumns.ORDER_ID, order.getCode());
		row.put(OrderEntryCsvColumns.ENTRY_NUMBER, entry.getEntryNumber());
	}

	/**
	 * @param entry
	 * @param row
	 */
	public void addScheduleLines(final AbstractOrderEntryModel entry, final Map<String, Object> row)
	{
		LOG.info(entry.getScheduleLines().size() + " : Schedule Lines for entry: " + entry.getEntryNumber());
		String scheduleLines = new String();
		for (final ScheduleLineModel scheduleLine : entry.getScheduleLines())
		{
			if (!scheduleLines.isEmpty())
			{
				scheduleLines = scheduleLines + "/";
			}

			scheduleLines = scheduleLines + scheduleLine.getConfirmedQuantity().toString() + ";"
					+ DateFormatUtils.formatUTC(scheduleLine.getConfirmedDate(), datePattern);
		}
		row.put(OaaOrderEntryCsvColumns.SCHEDULE_LINES, scheduleLines);
	}
}
