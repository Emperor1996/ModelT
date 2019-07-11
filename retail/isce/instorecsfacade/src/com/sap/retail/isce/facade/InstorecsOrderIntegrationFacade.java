/*****************************************************************************
 Interface:        InstorecsOrderIntegrationFacade
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade;

import com.sap.retail.isce.sap.exception.SAPISCEException;
import com.sap.retail.isce.service.sap.OrderHandOverOutput;


/**
 *
 * The facade provides methods relevant for the handover of pick-up orders.
 *
 */
public interface InstorecsOrderIntegrationFacade
{
	/**
	 * Calls the OrderSapRetailIntegrationService to trigger the handover process.
	 *
	 * @param orderCode
	 *           Order Code to be handovered
	 * @return OrderHandOverOutput object which contains various order information like status of the order after
	 *         handover was done, invoice document, etc.
	 * @throws SAPISCEException
	 */
	public OrderHandOverOutput handOverOrder(final String orderCode) throws SAPISCEException;

	/**
	 * @param orderCode
	 *           Order Code to be handed over
	 * @return PDF document as a byte string
	 * @throws SAPISCEException
	 */
	public byte[] getInvoiceDocumentForOrderCode(String orderCode) throws SAPISCEException;
}
