/*****************************************************************************
 Interface:        OrderHandOverOutput.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap;

import java.util.List;

import com.sap.retail.isce.service.sap.OrderHandoverInput;
import com.sap.retail.isce.service.sap.OrderHandoverInput.Operation;
import com.sap.retail.isce.service.sap.odata.BackendMessage;


/**
 * The Interface delivers the information after back end call. As a part of handover process in sap back end the
 * delivery document needs to be created, the goods need to be picked and goods issue document needs to be created. If
 * whole process is successful the invoice document in PDF format would be created.
 *
 */
public interface OrderHandOverOutput
{

	/**
	 * Enum contains possible order statuses after handover process was performed
	 *
	 */
	public enum Status
	{
		ERROR, OK
	}

	/**
	 * Get messages after backend call for the order handover
	 *
	 * @return List of BackendMessages
	 */
	public List<BackendMessage> getMessages();

	/**
	 * @return order status which order has got after handover was performed
	 */
	public Status getStatus();

	/**
	 * @return Invoice Id of the handovered order
	 */
	public String getInvoiceId();

	/**
	 * @return picking documents id
	 */
	public String getPickingId();

	/**
	 * @return good issues document Id
	 */
	public String getGoodsIssueId();

	/**
	 * @return delivery Id
	 */
	public String getDeliveryId();

	/**
	 * @return OrderHandoverInput object, for which the handover was performed in backend
	 */
	public OrderHandoverInput getInput();

	/**
	 * @return Operation which failed in backend
	 */
	public Operation getFailedOp();

	/**
	 * @return byte stream which the invoice document as PDF
	 */
	public byte[] getInvoiceAsPDF();

	/**
	 * @return list of Invoice Ids for the handovered order
	 */
	public abstract List<String> getInvoiceIds();

	/**
	 * @return List of delivery Id
	 */
	public abstract List<String> getDeliveryIds();

	/**
	 * @return List of picking document Ids
	 */
	public abstract List<String> getPickingIds();

	/**
	 * @return List of good issues ids
	 */
	public abstract List<String> getGoodsIssueIds();

}
