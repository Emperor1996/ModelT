/*****************************************************************************
 Class:        DataContainerODataDefaultImpl
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.commons.InlineCount;

import com.sap.retail.isce.container.DataContainerOData;
import com.sap.retail.isce.exception.DataContainerRuntimeException;

/**
 * Default implementation class for DataContainerOData.
 */
public abstract class DataContainerODataDefaultImpl extends DataContainerDefaultImpl implements DataContainerOData
{
	private static final Logger LOG = Logger.getLogger(DataContainerODataDefaultImpl.class.getName());
	private static final String LOG_TEXT_DCRT_EX_TROWN = ". DataContainerRuntimeException was thrown.";

	protected String httpDestinationName = null; // must not be null when used at runtime
	protected String serviceURI = null; // must not be null when used at runtime
	protected String serviceEndpointName = null;
	protected String keyPredicate = null;
	protected String navigationProperties = null;
	protected String filter = null;
	protected String select = null;
	protected Integer top = null;
	protected Integer skip = null;
	protected InlineCount inlineCount = null;
	protected String resultName = null; // must not be null
	protected String orderBy = null;
	protected Map<String, String> freeQueryMap = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerOData#getHttpDestinationName()
	 */
	@Override
	public String getHttpDestinationName()
	{
		if (this.httpDestinationName == null || this.httpDestinationName.isEmpty())
		{
			LOG.error("Missing httpDestinationName in data container " + this.getContainerName() + LOG_TEXT_DCRT_EX_TROWN);
			throw new DataContainerRuntimeException("Missing httpDestinationName in data container " + this.getContainerName());
		}
		return this.httpDestinationName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerOData#getServiceURI()
	 */
	@Override
	public String getServiceURI()
	{
		if (this.serviceURI == null || this.serviceURI.isEmpty())
		{
			LOG.error("Missing serviceURI in data container " + this.getContainerName() + LOG_TEXT_DCRT_EX_TROWN);
			throw new DataContainerRuntimeException("Missing serviceURI in data container " + this.getContainerName());
		}
		return this.serviceURI;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerOData#getServiceEndpointName()
	 */
	@Override
	public String getServiceEndpointName()
	{
		if (this.serviceEndpointName == null || this.serviceEndpointName.isEmpty())
		{
			LOG.error("Missing serviceEndpointName in data container " + this.getContainerName() + LOG_TEXT_DCRT_EX_TROWN);
			throw new DataContainerRuntimeException("Missing serviceEndpointName in data container " + this.getContainerName());
		}
		return this.serviceEndpointName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerOData#getKeyPredicate()
	 */
	@Override
	public String getKeyPredicate()
	{
		return this.keyPredicate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerOData#getNavigationProperties()
	 */
	@Override
	public String getNavigationProperties()
	{
		return this.navigationProperties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerOData#getFilter()
	 */
	@Override
	public String getFilter()
	{
		return this.filter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerOData#getTraceableFilter()
	 */
	@Override
	public String getTraceableFilter()
	{
		return "<implement proper getTraceableFilter method for specific container>";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerOData#getOrderBy()
	 */
	@Override
	public String getOrderBy()
	{
		return this.orderBy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerOData#getTop()
	 */
	@Override
	public Integer getTop()
	{
		return this.top;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerOData#getSkip()
	 */
	@Override
	public Integer getSkip()
	{
		return this.skip;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerOData#getSelect()
	 */
	@Override
	public String getSelect()
	{
		return this.select;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerOData#getInlineCount()
	 */
	@Override
	public InlineCount getInlineCount()
	{
		return this.inlineCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerOData#getFreeQueryMap()
	 */
	@Override
	public Map<String, String> getFreeQueryMap()
	{
		return freeQueryMap;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerOData#getResultName()
	 */
	@Override
	public String getResultName()
	{
		if ((this.resultName == null) || this.resultName.isEmpty())
		{
			LOG.error("Missing resultName in data container " + this.getContainerName() + LOG_TEXT_DCRT_EX_TROWN);
			throw new DataContainerRuntimeException("Missing resultName in data container " + this.getContainerName());
		}
		return this.resultName;
	}

	/**
	 * Method to trace all OData Container related information.
	 *
	 * @param log
	 *           the logger to use for tracing.
	 * @param doNotTraceInFilter
	 *           a value that should not be displayed in the trace of the filter.
	 */
	protected void traceAllInformation(final Logger log, final String doNotTraceInFilter)
	{
		if (log.isDebugEnabled())
		{
			log.debug("getContainerName()=" + getContainerName());
			log.debug("getServiceURI()=" + getServiceURI());
			log.debug("getResultName()=" + getResultName());
			log.debug("getServiceEndpointName()=" + getServiceEndpointName());
			log.debug("getHttpDestinationName()=" + getHttpDestinationName());
			log.debug("getSelect()=" + getSelect());
			if (doNotTraceInFilter == null || doNotTraceInFilter.isEmpty())
			{
				log.debug("getFilter()=" + getFilter());
			}
			else
			{
				log.debug("getFilter()=" + getFilter().replace(doNotTraceInFilter, "<value not traced>"));
			}
			log.debug("getOrderBy()=" + getOrderBy());
			log.debug("getTop()=" + getTop());
			log.debug("getSkip()=" + getSkip());
			log.debug("getInlineCount()=" + getInlineCount());
		}
	}
}
