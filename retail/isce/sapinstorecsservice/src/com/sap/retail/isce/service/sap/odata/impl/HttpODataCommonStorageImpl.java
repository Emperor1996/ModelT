/*****************************************************************************
 Class:        HttpODataCommonStorageImpl.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/

package com.sap.retail.isce.service.sap.odata.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sap.retail.isce.service.sap.odata.HttpODataCommonStorage;


/**
 * Default Implementation of HttpODataCommonStorage
 */
public class HttpODataCommonStorageImpl implements HttpODataCommonStorage
{
	protected Map<String, Map<String, Object>> destinations = new ConcurrentHashMap<>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.service.sap.odata.HttpODataCommonStorage#getDestination(java.lang.String)
	 */
	@Override
	public Map<String, Object> getDestination(final String destinationName)
	{
		return destinations.get(destinationName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.service.sap.odata.HttpODataCommonStorage#putDestination(java.lang.String, java.util.Map)
	 */
	@Override
	public void putDestination(final String destinationName, final Map<String, Object> destination)
	{
		destinations.put(destinationName, destination);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.service.sap.odata.HttpODataCommonStorage#removeDestination(java.lang.String)
	 */
	@Override
	public void removeDestination(final String destinationName)
	{
		destinations.remove(destinationName);
	}
}
