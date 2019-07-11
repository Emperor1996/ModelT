/*****************************************************************************
    Class:        AdditionalInformations
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.

 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.sourcing.jaxb.pojos.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;


/**
 * Jaxb Pojo for XML reading
 */
public class AdditionalInformations
{
	private List<AdditionalInformationItem> additionalInformations;

	public AdditionalInformations()
	{
		super();
		this.additionalInformations = new ArrayList<>();
	}


	@XmlElement(name = "ADDITIONAL_INFORMATION_ITEM")
	public List<AdditionalInformationItem> getAdditionalInformations()
	{
		return additionalInformations;
	}

	/**
	 * @param additionalInformations
	 *           the additionalInformations to set
	 */
	public void setAdditionalInformations(final List<AdditionalInformationItem> additionalInformations)
	{
		this.additionalInformations = additionalInformations;
	}
}
