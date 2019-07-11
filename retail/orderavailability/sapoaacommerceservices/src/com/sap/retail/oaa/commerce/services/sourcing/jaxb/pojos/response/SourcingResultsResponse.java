/*****************************************************************************
    Class:        SourcingResultsResponse
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.

 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.sourcing.jaxb.pojos.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;


/**
 * Jaxb Pojo for XML reading
 */
public class SourcingResultsResponse
{
	private List<SourcingResultResponse> sourcingResults;

	public SourcingResultsResponse()
	{
		super();
		this.sourcingResults = new ArrayList<>();
	}

	@XmlElement(name = "SOURCING_RESULT")
	public List<SourcingResultResponse> getSourcingResults()
	{
		return sourcingResults;
	}

	/**
	 * @param sourcingResults
	 *           the sourcingResults to set
	 */
	public void setSourcingResult(final List<SourcingResultResponse> sourcingResults)
	{
		this.sourcingResults = sourcingResults;
	}
}
