/*****************************************************************************
 Interface:        OrderHandoverInput.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap;

import java.util.List;


/**
 * The interface gathers all data of the order which should be handovered
 *
 */
public interface OrderHandoverInput
{

	/**
	 * The operation enum contains all operation which could be performed on order
	 *
	 */
	public enum Operation
	{
		GOODSISSUE, INVOICE, DELIVERY, PICK;
	}

	/**
	 * @return String Order Id to be handovered
	 */
	public String getOrderId();

	/**
	 * @return List of Operation to be performed on order
	 */
	public List<Operation> getOperations();


	/**
	 * @return true if the invoice document should be delivered in PDF format
	 */
	public boolean isGetInvoiceAsPDF();

}