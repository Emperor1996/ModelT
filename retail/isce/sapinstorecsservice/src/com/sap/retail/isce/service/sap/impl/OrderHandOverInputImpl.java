/*****************************************************************************
 Class:        OrderHandOverInputImpl.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.impl;

import java.util.ArrayList;
import java.util.List;

import com.sap.retail.isce.service.sap.OrderHandoverInput;


public class OrderHandOverInputImpl implements OrderHandoverInput
{
	private String orderId;
	private final List<Operation> ops = new ArrayList<>(4);
	private boolean getInvoiceAsPDF;



	/**
	 * @param getInvoiceAsPDF
	 *           the getInvoiceAsPDF to set
	 */
	public void setGetInvoiceAsPDF(final boolean getInvoiceAsPDF)
	{
		this.getInvoiceAsPDF = getInvoiceAsPDF;
	}




	@Override
	public String getOrderId()
	{
		return orderId;
	}




	/**
	 * @param orderId
	 *           the orderId to set
	 */
	public void setOrderId(final String orderId)
	{
		this.orderId = orderId;
	}


	@Override
	public List<Operation> getOperations()
	{
		return ops;
	}



	public void addOperation(final Operation op)
	{
		ops.add(op);
	}





	@Override
	public boolean isGetInvoiceAsPDF()
	{
		return getInvoiceAsPDF;
	}


}
