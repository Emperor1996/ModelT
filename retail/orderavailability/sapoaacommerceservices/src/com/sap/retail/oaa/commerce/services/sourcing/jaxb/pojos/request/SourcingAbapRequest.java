/*****************************************************************************
    Class:        SourcingAbapRequest
    Copyright (c) 2015, SAP SE, Germany, All rights reserved.

 *****************************************************************************/
package com.sap.retail.oaa.commerce.services.sourcing.jaxb.pojos.request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Jaxb Pojo for XML creation
 */
@XmlRootElement(namespace = "http://www.sap.com/abapxml", name = "abap")
public class SourcingAbapRequest
{

	private Values values;

	public SourcingAbapRequest()
	{
		super();
	}

	public SourcingAbapRequest(final Values values)
	{
		super();
		this.values = values;
	}

	@XmlElement(namespace = "http://www.sap.com/abapxml")
	public Values getValues()
	{
		return values;
	}

	/**
	 * @param values
	 *           the values to set
	 */
	public void setValues(final Values values)
	{
		this.values = values;
	}

	@Override
	public String toString()
	{
		return "Abap [values=" + values.toString() + "]";
	}

}
