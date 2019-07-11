/*****************************************************************************
 Interface:        HttpODataResult.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/

package com.sap.retail.isce.service.sap.odata;

import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;


/**
 * Container object for results of HTTP-OData calls. *
 */
public interface HttpODataResult
{

	/**
	 * @return the httpStatusCode
	 */
	public HttpStatusCodes getHttpStatusCode();

	/**
	 * @return the httpHeaders
	 */
	public Map<String, List<String>> getHttpHeaders();

	/**
	 * @return the metaData
	 */
	public Edm getMetaData();

	/**
	 * @return count
	 */
	public Integer getCount();

	/**
	 * @return the body of response
	 */
	public String getResponseBody();

	/**
	 * @return the entity
	 */
	public ODataEntry getEntity();

	/**
	 * @return the entity list
	 */
	public List<ODataEntry> getEntities();

	/**
	 * @return back end message list
	 */
	public List<BackendMessage> getMessages();

	/**
	 * @return content of requested media file as string.
	 */
	public byte[] getMediaContent();

	/**
	 * Status code supported by the oData result. Possible values:
	 * <ul>
	 * <li>{@link StatusCode#OK}</li>
	 * <li>{@link StatusCode#ERROR}</li>
	 * </ul>
	 */
	public enum StatusCode
	{
		/**
		 * The status code OK means the data have been properly read from the backend system and have been properly
		 * processed.
		 */
		OK,
		/**
		 * The status code ERROR means the data have not been properly read from the backend system and/or have not been
		 * properly processed.
		 */
		ERROR;
	}

	/**
	 * @return the statusCode
	 */
	public StatusCode getStatusCode();


}