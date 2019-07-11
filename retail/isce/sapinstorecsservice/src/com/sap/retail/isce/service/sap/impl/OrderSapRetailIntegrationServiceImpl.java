/*****************************************************************************
 Class:        OrderSapRetailIntegrationServiceImpl.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.impl;

import de.hybris.platform.sap.core.configuration.ConfigurationPropertyAccess;
import de.hybris.platform.sap.core.configuration.global.SAPGlobalConfigurationService;
import de.hybris.platform.sap.core.configuration.http.HTTPDestination;
import de.hybris.platform.sap.core.configuration.http.HTTPDestinationService;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.springframework.beans.factory.annotation.Required;

import com.sap.retail.isce.sap.exception.SAPISCEException;
import com.sap.retail.isce.service.sap.OrderHandOverOutput;
import com.sap.retail.isce.service.sap.OrderHandoverInput;
import com.sap.retail.isce.service.sap.OrderHandoverInput.Operation;
import com.sap.retail.isce.service.sap.OrderSapRetailIntegrationService;
import com.sap.retail.isce.service.sap.constants.SapinstorecsserviceConstants;
import com.sap.retail.isce.service.sap.odata.BackendMessage;
import com.sap.retail.isce.service.sap.odata.BackendMessage.Severity;
import com.sap.retail.isce.service.sap.odata.BackendMessageUtil;
import com.sap.retail.isce.service.sap.odata.HttpODataClient;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;
import com.sap.retail.isce.service.sap.odata.impl.HttpODataException;


public class OrderSapRetailIntegrationServiceImpl implements OrderSapRetailIntegrationService
{
	/**
	 *
	 */
	private static final String TEXT_FAILED = "' failed";
	private static final String SPACE = " ";

	protected static Logger log = Logger.getLogger(OrderSapRetailIntegrationServiceImpl.class.getName());

	private HttpODataClient httpODataClient;
	private BackendMessageUtil messageUtil;
	private HTTPDestination destination;
	private SAPGlobalConfigurationService globalConfigurationService;
	private HTTPDestinationService httpDestinationService;

	protected HTTPDestination getHttpDestination()
	{
		if (destination == null && this.globalConfigurationService != null && this.httpDestinationService != null)
		{
			final ConfigurationPropertyAccess httpDestinationPropertyMap = getGlobalConfigurationService().getPropertyAccess(
					"sapcommon_erpHttpDestination");
			final String httpDestinationName = (String) httpDestinationPropertyMap.getProperty("httpDestinationName");
			destination = getHttpDestinationService().getHTTPDestination(httpDestinationName);
		}

		return destination;
	}

	protected HTTPDestination getDestination()
	{
		return destination;
	}

	public void setDestination(final HTTPDestination destination)
	{
		this.destination = destination;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sap.retail.isce.service.sap.OrderSapRetailIntegrationService#handoverOrder(com.sap.retail.isce.service.sap
	 * .OrderHandoverInput)
	 */
	@Override
	public OrderHandOverOutput handoverOrder(final OrderHandoverInput input) throws SAPISCEException
	{
		final Map<String, Object> entityData = buildEntityData(input);

		OrderHandOverOutputImpl output = null;
		try
		{
			traceHandoverOrder(entityData);

			final HttpODataResult result = httpODataClient.putEntity("Documents", entityData, this.getHttpDestination(),
					OrderSapRetailIntegrationService.SERVICE_PREFIX_PATH + OrderSapRetailIntegrationService.SERVICE_NAME);

			traceResult(result);

			output = parseOdataResponse(result);
			output.setInput(input);

			if (!OrderHandOverOutput.Status.OK.equals(output.getStatus()))
			{
				String failingOp = null;
				final List<String> failingOps = result.getHttpHeaders().get("failing_operation");
				if (null != failingOps)
				{
					failingOp = failingOps.get(0);
				}
				final StringBuilder builder = new StringBuilder();
				builder.append("Handover for document with id '");
				builder.append(input.getOrderId());
				builder.append(TEXT_FAILED);
				if (null != failingOp)
				{
					builder.append(" for operation ");
					builder.append(failingOp);
				}

				builder.append("; messages: ");

				log.error(builder.toString());

				for (final BackendMessage message : result.getMessages())
				{
					log.error(message.toString());
				}
			}

			if (input.isGetInvoiceAsPDF() && output.getInvoiceId() != null && !output.getInvoiceId().trim().isEmpty())
			{
				final HttpODataResult mediaResult = getInvoicePDFDoucment(output.getInvoiceId());
				output.setMessages(messageUtil.mergeMessageLists(output.getMessages(), mediaResult.getMessages()));
				output.setInvoiceAsPDF(mediaResult.getMediaContent());
			}
		}
		catch (final HttpODataException ex)
		{
			throw new SAPISCEException("Handover failed", ex);
		}
		return output;
	}

	/**
	 * Traces handoverOrder related information.
	 *
	 * @param entityData
	 *           the order data to be traced.
	 */
	protected void traceHandoverOrder(final Map<String, Object> entityData)
	{
		if (log.isDebugEnabled())
		{
			log.debug("handoverOrder");
			log.debug("EntitySetname=Documents");
			log.debug("entityData=" + convertMapToString(entityData));
			traceDestinationAndURI();
		}
	}

	/**
	 * Traces destination and URI related information.
	 */
	protected void traceDestinationAndURI()
	{
		if (log.isDebugEnabled())
		{
			log.debug("httpDestination=" + this.getHttpDestination().getHttpDestinationName());
			log.debug("serviceUri=" + OrderSapRetailIntegrationService.SERVICE_PREFIX_PATH
					+ OrderSapRetailIntegrationService.SERVICE_NAME);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.service.sap.OrderSapRetailIntegrationService#getInvoiceForOrder(java.lang.String)
	 */
	@Override
	public byte[] getInvoiceForOrder(final String orderCode) throws SAPISCEException
	{
		byte[] pdfDocument = null;
		try
		{
			if (log.isDebugEnabled())
			{
				log.debug("getInvoiceForOrder");
				log.debug("EntitySetname=FollowupDocs");
				log.debug("filter=" + "OrderId eq '" + orderCode + "' and DocumentType eq 'M'");
				traceDestinationAndURI();
			}

			final HttpODataResult result = httpODataClient.readFeed("FollowupDocs", "OrderId eq '" + orderCode
					+ "' and DocumentType eq 'M'", this.getHttpDestination(), OrderSapRetailIntegrationService.SERVICE_PREFIX_PATH
					+ OrderSapRetailIntegrationService.SERVICE_NAME);

			traceResult(result);

			handleInvoiceForOrderErrorMessage(orderCode, result);

			final ODataEntry entry = result.getEntity();
			if (entry != null)
			{
				final Map<String, Object> properties = result.getEntity().getProperties();
				final String invoiceId = properties.get("DocumentId").toString();

				final HttpODataResult mediaResult = getInvoicePDFDoucment(invoiceId);
				pdfDocument = mediaResult.getMediaContent();
			}
			else
			{
				log.error("No invoice found for order " + orderCode);
			}
		}
		catch (final HttpODataException ex)
		{
			throw new SAPISCEException("Failed to get Invoice", ex);
		}

		return pdfDocument;
	}

	/**
	 * Handles the potential error messages of the Invoice Odata Call.
	 *
	 * @param orderCode
	 *           String the order number
	 * @param result
	 *           HttpODataResult the result of the OData call
	 */
	protected void handleInvoiceForOrderErrorMessage(final String orderCode, final HttpODataResult result)
	{
		if (messageUtil.hasErrorMessage(result.getMessages()))
		{
			final StringBuilder builder = new StringBuilder();
			builder.append("getting invoice for order '");
			builder.append(orderCode);
			builder.append(TEXT_FAILED);

			for (final BackendMessage message : result.getMessages())
			{
				if (Severity.ERROR.equals(message.getSeverity()))
				{
					builder.append(message);
					builder.append("; ");
				}
			}
			log.error(builder.toString());
		}
	}

	protected HttpODataResult getInvoicePDFDoucment(final String invoiceId) throws HttpODataException
	{
		final Map<String, Object> mediaKeys = new HashMap<>(4);
		mediaKeys.put("MediaDocId", invoiceId);
		mediaKeys.put("MimeType", "application/pdf");

		if (log.isDebugEnabled())
		{
			log.debug("getInvoicePDFDoucment");
			log.debug("EntitySetname=MediaDocs");
			log.debug("entityKeys=" + convertMapToString(mediaKeys));
			traceDestinationAndURI();
		}

		final HttpODataResult result = httpODataClient.getMediaFile("MediaDocs", mediaKeys,
				SapinstorecsserviceConstants.PDF_MIME_TYPE, this.getHttpDestination(),
				OrderSapRetailIntegrationService.SERVICE_PREFIX_PATH + OrderSapRetailIntegrationService.SERVICE_NAME);

		traceResult(result);

		if (messageUtil.hasErrorMessage(result.getMessages()))
		{
			final StringBuilder builder = new StringBuilder();
			builder.append("getting invoice with id '");
			builder.append(invoiceId);
			builder.append(TEXT_FAILED);

			for (final BackendMessage message : result.getMessages())
			{
				if (Severity.ERROR.equals(message.getSeverity()))
				{
					builder.append(message);
					builder.append("; ");
				}
			}
			log.error(builder.toString());
		}
		return result;

	}

	/**
	 * @param result
	 */
	protected void traceResult(final HttpODataResult result)
	{
		if (result.getHttpStatusCode() != null)
		{
			log.debug("Result HttpStatusCode =" + result.getHttpStatusCode().getStatusCode());
		}
	}

	/**
	 * Returns a string representation of the given map.
	 *
	 * @param map
	 *           the map to convert.
	 * @return a string representation of the map content.
	 */
	protected String convertMapToString(final Map<String, Object> map)
	{
		final StringBuilder entityKeys = new StringBuilder();
		final Set<Entry<String, Object>> mediaKeysEntries = map.entrySet();
		String separator = "";
		for (final Entry<String, Object> entry : mediaKeysEntries)
		{
			entityKeys.append(separator).append(entry.getKey()).append(":").append(entry.getValue());
			separator = ", ";
		}
		return entityKeys.toString();
	}

	protected OrderHandOverOutputImpl parseOdataResponse(final HttpODataResult result)
	{
		final OrderHandOverOutputImpl output = new OrderHandOverOutputImpl();
		for (final Entry<String, List<String>> headerEntry : result.getHttpHeaders().entrySet())
		{
			final String headerFieldName = headerEntry.getKey();
			final String headerFieldValue = headerEntry.getValue().get(0);

			if (log.isDebugEnabled())
			{
				log.debug("processing headerFieldName=" + headerFieldName + "; headerFieldValue=" + headerFieldValue);
			}

			if (headerFieldName == null)
			{
				continue;
			}
			switch (headerFieldName)
			{
				case "status":
					output.setStatus(OrderHandOverOutput.Status.valueOf(headerFieldValue));
					break;

				case "goodsissues":
					output.setGoodsIssueId(headerFieldValue);
					break;

				case "picking":
					output.setPickingId(headerFieldValue);
					break;

				case "invoices":
					output.setInvoiceId(headerFieldValue);
					break;

				case "deliveries":
					output.setDeliveryId(headerFieldValue);
					break;

				case "failing_operation":
					setFailedOpOData(output, headerFieldValue);
					break;

				default:
					break;
			}
			output.setMessages(result.getMessages());

		}
		return output;
	}

	/**
	 * @param output
	 * @param headerFieldValue
	 */
	protected void setFailedOpOData(final OrderHandOverOutputImpl output, final String headerFieldValue)
	{
		if ("PICK/GOODSISSUE".equals(headerFieldValue))
		{
			output.setFailedOp(Operation.PICK);
		}
		else
		{
			output.setFailedOp(Operation.valueOf(headerFieldValue));
		}
	}

	protected Map<String, Object> buildEntityData(final OrderHandoverInput input)
	{
		final Map<String, Object> entityData = new HashMap<>();
		entityData.put("DocumentId", input.getOrderId());
		entityData.put("DocumentType", "C");
		final StringBuilder operation = new StringBuilder();
		final Iterator<Operation> opItr = input.getOperations().iterator();
		while (opItr.hasNext())
		{
			operation.append(opItr.next());
			if (opItr.hasNext())
			{
				operation.append(SPACE);
			}
		}
		entityData.put("Operation", operation.toString());
		return entityData;
	}


	@Required
	public void setHttpODataClient(final HttpODataClient httpODataClient)
	{
		this.httpODataClient = httpODataClient;
	}


	protected BackendMessageUtil getMessageUtil()
	{
		return messageUtil;
	}

	@Required
	public void setMessageUtil(final BackendMessageUtil messageUtil)
	{
		this.messageUtil = messageUtil;
	}

	/**
	 * used for testing
	 *
	 * @return underlying oData client
	 */
	HttpODataClient getHttpODataClient()
	{
		return this.httpODataClient;
	}

	public SAPGlobalConfigurationService getGlobalConfigurationService()
	{
		return globalConfigurationService;
	}

	@Required
	public void setGlobalConfigurationService(final SAPGlobalConfigurationService globalConfigurationService)
	{
		this.globalConfigurationService = globalConfigurationService;
	}

	public HTTPDestinationService getHttpDestinationService()
	{
		return httpDestinationService;
	}

	@Required
	public void setHttpDestinationService(final HTTPDestinationService httpDestinationService)
	{
		this.httpDestinationService = httpDestinationService;
	}
}
