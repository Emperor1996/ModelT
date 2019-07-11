/*****************************************************************************
 Class:        DataContainerBatchPart
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.odata.impl;

import org.apache.olingo.odata2.api.client.batch.BatchPart;


/**
 * Wrapper for a batch part to add additional attributes to a batch.
 */
public class DataContainerBatchPart
{
	private String resultName;
	private String dataContainerName;
	private BatchPart batchPart;

	/**
	 * @return the resultName
	 */
	public String getResultName()
	{
		return resultName;
	}

	/**
	 * @param resultName
	 *           the resultName to set
	 */
	public void setResultName(final String resultName)
	{
		this.resultName = resultName;
	}

	/**
	 * @return the dataContainerName
	 */
	public String getDataContainerName()
	{
		return dataContainerName;
	}

	/**
	 * @param dataContainerName
	 *           the dataContainerName to set
	 */
	public void setDataContainerName(final String dataContainerName)
	{
		this.dataContainerName = dataContainerName;
	}

	/**
	 * @return the batchPart
	 */
	public BatchPart getBatchPart()
	{
		return batchPart;
	}

	/**
	 * @param batchPart
	 *           the batchPart to set
	 */
	public void setBatchPart(final BatchPart batchPart)
	{
		this.batchPart = batchPart;
	}
}
