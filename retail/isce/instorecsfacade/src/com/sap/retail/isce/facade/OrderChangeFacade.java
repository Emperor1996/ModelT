/*****************************************************************************
 Interface:        OrderChangeFacade
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade;


/**
 * Interface for OrderChangeFacade Facade.
 */
public interface OrderChangeFacade
{
	/**
	 * Copies the entries of the given order to the cart and cancels the order. for the order entries only Product, Unit,
	 * Quantity, Configuration and GiveAway are copied.
	 *
	 * @param orderCode
	 *           a list of data container to be filled by the underlying oData call
	 * 
	 * @return true if everything went well, false else
	 */
	public boolean copyOrderToCartAndCancel(String orderCode);
}
