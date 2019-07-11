/*****************************************************************************
    Class:        OaaOrderEntryCsvColumns
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.

 *****************************************************************************/
package com.sap.retail.oaa.orderexchange.constants;

/**
 * Constants for ROCC OAA order exchange
 */
public final class OaaOrderEntryCsvColumns
{
	@SuppressWarnings("javadoc")
	public static final String ORDER_ID = "orderId";

	@SuppressWarnings("javadoc")
	public static final String ORDER_ENTRY_ID = "orderEntryId";

	@SuppressWarnings("javadoc")
	public static final String SITE_ID = "siteId";

	@SuppressWarnings("javadoc")
	public static final String CAC_SHIPPING_POINT = "cacShippingPoint";

	@SuppressWarnings("javadoc")
	public static final String SCHEDULE_LINES = "scheduleLines";

	@SuppressWarnings("javadoc")
	public static final String VENDOR_ITEM_CATEGORY = "vendorItemCategory";

	private OaaOrderEntryCsvColumns()
	{
		// private constructor to avoid instantiation
	}

}
