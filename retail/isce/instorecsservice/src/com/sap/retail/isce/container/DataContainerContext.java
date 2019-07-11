/*****************************************************************************
 Interface:        DataContainerContext
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container;

import java.util.Map;


/**
 * Context of a data container.
 *
 */
public interface DataContainerContext
{
	public static final String CUSTOMER_DATA = "CUSTOMER_DATA";

	public Map<String, Object> getContextMap();
}
