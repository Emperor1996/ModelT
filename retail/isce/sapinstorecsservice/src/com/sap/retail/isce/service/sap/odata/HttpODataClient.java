/*****************************************************************************
 Interface:        HttpODataClient.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.odata;

import de.hybris.platform.sap.core.configuration.http.HTTPDestination;

import java.util.List;
import java.util.Map;

import com.sap.retail.isce.service.sap.odata.impl.DataContainerBatchPart;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataException;


/**
 * The HttpODataClient communicates with oData end points using the <a href="http://www.odata.org">oData</a> protocol.
 * It will handle HTTP-Authentication, -Connections and -Errors. Additionally SSO-Cookies, XSRF-Tokens and Entity Data
 * Model are evaluated.<br>
 * <br>
 */
public interface HttpODataClient
{
	/**
	 * Executes a batch call to a Service at a HttpDestination, composed of one or several batchs for calling this
	 * service parts.
	 *
	 * @param dcBatchParts
	 *           List of data container batch parts.
	 * @param httpDestination
	 *           destination to be used
	 * @param serviceURI
	 *           The service URI is the service root URI without the HTTPDestination part, e.g.
	 *           /sap/opu/odata/sap/CUAN_COMMON_SRV
	 * @return a Map of HttpODataResult objects, one for every Batch parts.
	 * @throws HttpODataException
	 *            in case of OData failure
	 */
	public Map<String, HttpODataResult> executeBatchCall(final List<DataContainerBatchPart> dcBatchParts,
			final HTTPDestination httpDestination, final String serviceURI) throws HttpODataException;


	/**
	 * Writes an entity using HTTP-Put.
	 *
	 * @param entitySetName
	 *           entity set name of the entity to write
	 * @param entityData
	 *           entity data to be written; must be provided as name-value pairs.
	 * @param httpDestination
	 *           destination to be used
	 * @param serviceURI
	 *           The service URI is the service root URI without the HTTPDestination part, e.g.
	 *           /sap/opu/odata/sap/CUAN_COMMON_SRV
	 * @return result of the call, contains any messages raised by the back-end and the status
	 * @throws HttpODataException
	 *            in case of OData failure
	 */
	public HttpODataResult putEntity(final String entitySetName, final Map<String, Object> entityData,
			final HTTPDestination httpDestination, final String serviceURI) throws HttpODataException;


	/**
	 * Reads entity data using HTTP-Get.
	 *
	 * @param entitySetName
	 *           name of the entity set to read
	 * @param filter
	 *           filter string
	 * @param httpDestination
	 *           destination to be used
	 * @param serviceURI
	 *           The service URI is the service root URI without the HTTPDestination part, e.g.
	 *           /sap/opu/odata/sap/CUAN_COMMON_SRV
	 * @return result containing the requested entity data, any messages raised by the back-end and the status
	 * @throws HttpODataException
	 *            in case of a communication failure
	 */
	public HttpODataResult readFeed(final String entitySetName, final String filter, final HTTPDestination httpDestination,
			final String serviceURI) throws HttpODataException;

	/**
	 * Gets a resource / media file using HTTP-Get
	 *
	 * @param entitySetName
	 *           name of the entity set to read
	 * @param entityKeys
	 *           key of the entity to read
	 * @param contentType
	 *           HTTP-Mime type of the requested data
	 * @param httpDestination
	 *           destination to be used The service URI is the service root URI without the HTTPDestination part, e.g.
	 *           /sap/opu/odata/sap/CUAN_COMMON_SRV
	 * @return result containing the requested resource / media file, any messages raised by the back-end and the status
	 * @throws HttpODataException
	 *            in case of a communication failure
	 */
	public HttpODataResult getMediaFile(String entitySetName, Map<String, Object> entityKeys, String contentType,
			final HTTPDestination httpDestination, final String serviceURI) throws HttpODataException;

}