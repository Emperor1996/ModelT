/*****************************************************************************
 Interface:        HttpODataResultManipulator.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.odata;

import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;

import com.sap.retail.isce.service.sap.odata.HttpODataResult.StatusCode;


/**
 * Interface for manipulation the result.
 *
 */
public interface HttpODataResultManipulator
{
	/**
	 * sets the entity data model.
	 *
	 * @param metaData
	 *           the metaData to set
	 */
	public void setMetaData(final Edm metaData);

	/**
	 * sets the http header map
	 *
	 * @param httpHeaders
	 *           the httpHeaders map to set
	 */
	public void setHttpHeaders(final Map<String, List<String>> httpHeaders);

	/**
	 * @param count
	 *           the count to be set
	 */
	public void setCount(Integer count);

	/**
	 * @param body
	 *           the response body to be set
	 */
	public void setResponseBody(String body);

	/**
	 * Sets the http status code
	 *
	 * @param httpStatusCode
	 *           the httpStatusCode to set
	 */
	public void setHttpStatusCode(final HttpStatusCodes httpStatusCode);

	/**
	 * Set the status code.
	 *
	 * @param statusCode
	 *           the statusCode to set
	 */
	public void setStatusCode(final StatusCode statusCode);

	/**
	 * Sets the list of messages.
	 *
	 * @param messages
	 *           the messages to set
	 */
	public void setMessages(final List<BackendMessage> messages);

	/**
	 * Sets the list of OData entities in result object.
	 *
	 * @param entities
	 *           the entity list to be set
	 */
	public void setEntities(final List<ODataEntry> entities);

	/**
	 * Sets the entity in result object.
	 *
	 * @param entity
	 *           the entity to be set
	 */
	public void setEntity(final ODataEntry entity);

	/**
	 * Set the media content.
	 *
	 * @param content
	 *           the byte array containing the content
	 */
	public void setMediaContent(final byte[] content);

}
