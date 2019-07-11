/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.sap.retail.oaa.orderexchange.outbound.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.OrderEntryCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.PartnerCsvColumns;
import de.hybris.platform.sap.orderexchange.outbound.impl.DefaultPartnerContributor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Builds the Row map for the CSV files for the Partner in an Order
 */
public class DefaultOaaPartnerContributor extends DefaultPartnerContributor
{

	private static final String PARTNER_FUNCTION_VENDOR = "LF";


	@Override
	public Set<String> getColumns()
	{
		final Set<String> columns = super.getColumns();
		columns.addAll(Arrays.asList(OrderEntryCsvColumns.ENTRY_NUMBER));
		return columns;
	}


	@Override
	public List<Map<String, Object>> createRows(final OrderModel order)
	{

		final List<Map<String, Object>> result = super.createRows(order);
		result.stream().forEach(row -> row.put(OrderEntryCsvColumns.ENTRY_NUMBER, "-1"));

		for (final AbstractOrderEntryModel entry : order.getEntries())
		{
			if (entry.getSapVendor() != null)
			{
				final Map<String, Object> row = new HashMap<>();
				row.put(OrderCsvColumns.ORDER_ID, order.getCode());
				row.put(OrderEntryCsvColumns.ENTRY_NUMBER, entry.getEntryNumber());
				row.put(PartnerCsvColumns.PARTNER_CODE, entry.getSapVendor());
				row.put(PartnerCsvColumns.PARTNER_ROLE_CODE, PARTNER_FUNCTION_VENDOR);
				result.add(row);
			}
		}
		return result;
	}




}