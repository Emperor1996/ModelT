/**
 *
 */
package com.sap.retail.oaa.orderexchange.outbound.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.outbound.RawItemContributor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Adds ROCC OAA specific Fields to Order for Order Replication
 */
public class DefaultOaaOrderContributor implements RawItemContributor<OrderModel>
{
	private final Set<String> columns = new HashSet<>(Arrays.asList(OrderCsvColumns.ORDER_ID));

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.sap.orderexchange.outbound.RawItemContributor#getColumns()
	 */
	@Override
	public Set<String> getColumns()
	{
		return columns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.sap.orderexchange.outbound.RawItemContributor#createRows(de.hybris.platform.servicelayer.model
	 * .AbstractItemModel)
	 */
	@Override
	public List<Map<String, Object>> createRows(final OrderModel order)
	{
		final List<Map<String, Object>> result = new ArrayList<>();
		final Map<String, Object> row = new HashMap<>();

		row.put(OrderCsvColumns.ORDER_ID, order.getCode());

		result.add(row);
		return result;
	}

}
