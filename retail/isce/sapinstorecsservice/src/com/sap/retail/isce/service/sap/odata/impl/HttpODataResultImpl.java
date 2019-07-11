/*****************************************************************************
 Class:        HttpODataResultImpl.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/

package com.sap.retail.isce.service.sap.odata.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;

import com.sap.retail.isce.service.sap.odata.BackendMessage;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;
import com.sap.retail.isce.service.sap.odata.HttpODataResultManipulator;


public class HttpODataResultImpl implements HttpODataResult, HttpODataResultManipulator
{

	private HttpStatusCodes httpStatusCode;
	private Map<String, List<String>> httpHeaders;
	private Edm metaData;
	private List<ODataEntry> entities;
	private List<BackendMessage> messages;
	private byte[] mediaContent;
	private StatusCode statusCode;
	private String body;
	private Integer count;



	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sap.retail.isce.service.sap.odata.HttpODataResultManipulator#setHttpStatusCode(org.apache.olingo.odata2.api
	 * .commons.HttpStatusCodes)
	 */
	@Override
	public void setHttpStatusCode(final HttpStatusCodes httpStatusCode)
	{
		this.httpStatusCode = httpStatusCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.service.sap.odata.HttpODataResult#getHttpStatusCode()
	 */
	@Override
	public HttpStatusCodes getHttpStatusCode()
	{
		return this.httpStatusCode;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.service.sap.odata.HttpODataResultManipulator#setHttpHeaders(java.util.Map)
	 */
	@Override
	public void setHttpHeaders(final Map<String, List<String>> httpHeaders)
	{
		this.httpHeaders = httpHeaders;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.service.sap.odata.HttpODataResult#getHttpHeaders()
	 */
	@Override
	public Map<String, List<String>> getHttpHeaders()
	{
		return this.httpHeaders;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.sap.retail.isce.service.sap.odata.HttpODataResultManipulator#setMetaData(org.apache.olingo.odata2.api.edm.Edm)
	 */
	@Override
	public void setMetaData(final Edm metaData)
	{
		this.metaData = metaData;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.service.sap.odata.HttpODataResult#getMetaData()
	 */
	@Override
	public Edm getMetaData()
	{
		return this.metaData;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.sap.retail.isce.service.sap.odata.HttpODataResultManipulator#setEntity(org.apache.olingo.odata2.api.ep.entry
	 * .ODataEntry)
	 */
	@Override
	public void setEntity(final ODataEntry entity)
	{
		this.entities = new ArrayList<>();
		this.entities.add(entity);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.service.sap.odata.HttpODataResult#getEntity()
	 */
	@Override
	public ODataEntry getEntity()
	{
		if (this.entities != null && !this.entities.isEmpty())
		{
			return this.entities.get(0);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.service.sap.odata.HttpODataResultManipulator#setEntities(java.util.List)
	 */
	@Override
	public void setEntities(final List<ODataEntry> entities)
	{
		this.entities = entities;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.service.sap.odata.HttpODataResult#getEntities()
	 */
	@Override
	public List<ODataEntry> getEntities()
	{
		if (this.entities == null)
		{
			this.entities = new ArrayList<>();
		}
		return this.entities;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.service.sap.odata.HttpODataResultManipulator#setMessages(java.util.List)
	 */
	@Override
	public void setMessages(final List<BackendMessage> messages)
	{
		this.messages = messages;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.service.sap.odata.HttpOdataResult#getMessages()
	 */
	@Override
	public List<BackendMessage> getMessages()
	{
		return this.messages;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.service.sap.odata.HttpODataResultManipulator#setMediaContent(byte[])
	 */
	@Override
	public void setMediaContent(final byte[] content)
	{
		this.mediaContent = content;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.service.sap.odata.HttpOdataResult#getMediaContent()
	 */
	@Override
	public byte[] getMediaContent()
	{
		return this.mediaContent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sap.retail.isce.service.sap.odata.HttpODataResultManipulator#setStatusCode(com.sap.retail.isce.service.sap
	 * .odata .HttpOdataResult.StatusCode)
	 */
	@Override
	public void setStatusCode(final StatusCode statusCode)
	{
		this.statusCode = statusCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.service.sap.odata.HttpOdataResult#getStatusCode()
	 */
	@Override
	public StatusCode getStatusCode()
	{
		return this.statusCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.service.sap.odata.HttpOdataResult#getResponseBody()
	 */
	@Override
	public String getResponseBody()
	{
		return this.body;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.service.sap.odata.HttpODataResultManipulator#setResponseBody(java.lang.String)
	 */
	@Override
	public void setResponseBody(final String body)
	{
		this.body = body;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.service.sap.odata.HttpODataResultManipulator#setCount(java.lang.Integer)
	 */
	@Override
	public void setCount(final Integer count)
	{
		this.count = count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.service.sap.odata.HttpOdataResult#getCount()
	 */
	@Override
	public Integer getCount()
	{
		return this.count;
	}
}
