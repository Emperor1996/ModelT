/*****************************************************************************
 Class:        OrderHandOverOutputImpl.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sap.retail.isce.service.sap.OrderHandOverOutput;
import com.sap.retail.isce.service.sap.OrderHandoverInput;
import com.sap.retail.isce.service.sap.OrderHandoverInput.Operation;
import com.sap.retail.isce.service.sap.odata.BackendMessage;


public class OrderHandOverOutputImpl implements OrderHandOverOutput
{

	private OrderHandoverInput input;
	private List<String> goodsIssueIds = Collections.emptyList();
	private List<String> pickingIds = Collections.emptyList();
	private List<String> deliveryIds = Collections.emptyList();
	private List<String> invoiceIds = Collections.emptyList();
	private Status status;
	private List<BackendMessage> messages = Collections.emptyList();
	private Operation failedOp;
	private byte[] invoiceAsPDF;

	/**
	 * @return the input
	 */
	@Override
	public OrderHandoverInput getInput()
	{
		return input;
	}

	/**
	 * @return the goodsIssueIds
	 */
	@Override
	public List<String> getGoodsIssueIds()
	{
		return goodsIssueIds;
	}


	/**
	 * @return the pickingIds
	 */
	@Override
	public List<String> getPickingIds()
	{
		return pickingIds;
	}


	/**
	 * @return the deliveryIds
	 */
	@Override
	public List<String> getDeliveryIds()
	{
		return deliveryIds;
	}


	/**
	 * @return the invoiceIds
	 */
	@Override
	public List<String> getInvoiceIds()
	{
		return invoiceIds;
	}

	/**
	 * @return the deliveryId
	 */
	@Override
	public String getDeliveryId()
	{
		return getFirst(deliveryIds);
	}


	/**
	 * @return the goodsIssueId
	 */
	@Override
	public String getGoodsIssueId()
	{
		return getFirst(goodsIssueIds);
	}

	/**
	 * @return the pickingId
	 */
	@Override
	public String getPickingId()
	{
		return getFirst(pickingIds);
	}

	/**
	 * @return the invoiceId
	 */
	@Override
	public String getInvoiceId()
	{
		return getFirst(invoiceIds);
	}


	/**
	 * @return the status
	 */
	@Override
	public Status getStatus()
	{
		return status;
	}

	/**
	 * @return the messages
	 */
	@Override
	public List<BackendMessage> getMessages()
	{
		return messages;
	}

	/**
	 * @param input
	 *           the input to set
	 */
	public void setInput(final OrderHandoverInput input)
	{
		this.input = input;
	}

	/**
	 * @param goodsIssueId
	 *           the goodsIssueId to set
	 */
	public void setGoodsIssueId(final String goodsIssueId)
	{
		this.goodsIssueIds = split(goodsIssueId);
	}



	/**
	 * @param pickingId
	 *           the pickingId to set
	 */
	public void setPickingId(final String pickingId)
	{
		this.pickingIds = split(pickingId);
	}

	/**
	 * @param invoiceId
	 *           the invoiceId to set
	 */
	public void setInvoiceId(final String invoiceId)
	{
		this.invoiceIds = split(invoiceId);
	}

	/**
	 * @param status
	 *           the status to set
	 */
	public void setStatus(final Status status)
	{
		this.status = status;
	}

	/**
	 * @param messages
	 *           the messages to set
	 */
	public void setMessages(final List<BackendMessage> messages)
	{
		this.messages = messages;
	}

	/**
	 * @return the failedOp
	 */
	@Override
	public Operation getFailedOp()
	{
		return failedOp;
	}

	/**
	 * @param failedOp
	 *           the failedOp to set
	 */
	public void setFailedOp(final Operation failedOp)
	{
		this.failedOp = failedOp;
	}


	/**
	 * @param deliveryId
	 *           the deliveryId to set
	 */
	public void setDeliveryId(final String deliveryId)
	{
		this.deliveryIds = split(deliveryId);
	}

	/**
	 * @return the invoiceAsPDF
	 */
	@Override
	public byte[] getInvoiceAsPDF()
	{
		return invoiceAsPDF;
	}

	/**
	 * @param invoiceAsPDF
	 *           the invoiceAsPDF to set
	 */
	public void setInvoiceAsPDF(final byte[] invoiceAsPDF)
	{
		this.invoiceAsPDF = invoiceAsPDF;
	}


	protected List<String> split(final String idString)
	{
		List idList;
		if (idString == null || idString.length() == 0)
		{
			idList = Collections.emptyList();
		}
		else
		{
			final String[] idArray = idString.split(" ");
			if (idArray.length > 1)
			{
				idList = Arrays.asList(idArray);
			}
			else
			{
				idList = Collections.singletonList(idString);
			}
		}
		return idList;
	}


	protected String getFirst(final List<String> idList)
	{
		String id = null;
		if (!idList.isEmpty())
		{
			id = idList.get(0);
		}
		return id;
	}

}
