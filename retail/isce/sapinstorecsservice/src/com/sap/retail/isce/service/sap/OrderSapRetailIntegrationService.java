/*****************************************************************************
 Interface:        OrderSapRetailIntegrationService.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap;

import com.sap.retail.isce.sap.exception.SAPISCEException;



/**
 * The service provides integration for hybris orders with ERP Retail back-end
 *
 */
public interface OrderSapRetailIntegrationService
{

	/**
	 * oData service name in the sap back end
	 */
	public static String SERVICE_NAME = "RETAILSTORE_CUST_ENG_SRV";

	/**
	 * prefix path of oData service name in the sap back end e.g. /sap/opu/odata/sap/
	 */
	public static String SERVICE_PREFIX_PATH = "/sap/opu/odata/sap/";


	/**
	 * @param input
	 *           OrderHandoverInput object to pass data from hybris to the oData service
	 * @return OrderHandoverOutput object to pass data from the oData service to the hybris
	 * @throws SAPISCEException
	 */
	public OrderHandOverOutput handoverOrder(final OrderHandoverInput input) throws SAPISCEException;

	/**
	 * @param orderCode
	 *           Id of the order document in back end, after the order is handed over successfully the invoice document
	 *           is created in PDF format
	 * @return byte stream with the invoice document as PDF
	 * @throws SAPISCEException
	 */
	public byte[] getInvoiceForOrder(final String orderCode) throws SAPISCEException;
}
