/*****************************************************************************
 Class:        DataContainerContextDefaultImpl
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import java.util.HashMap;
import java.util.Map;

import com.sap.retail.isce.container.DataContainerContext;


/**
 * Default implementation class for Data Container Context.
 *
 */
public class DataContainerContextDefaultImpl implements DataContainerContext
{
	protected Map<String, Object> contextData = new HashMap();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerContext#getContextMap()
	 */
	@Override
	public Map<String, Object> getContextMap()
	{
		return contextData;
	}

}
