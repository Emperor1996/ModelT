/*****************************************************************************
 Class:        StatusCheckImpl
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.frontend.util.impl;

import de.hybris.platform.assistedserviceservices.exception.AssistedServiceAgentNotLoggedInException;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.commercefacades.order.data.ConsignmentData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.core.enums.OrderStatus;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.sap.retail.isce.facade.InstorecsContextFacade;
import com.sap.retail.isce.facade.exception.StoreContextNotDefinedException;
import com.sap.retail.isce.frontend.util.StatusCheck;


/**
 * Class implements StatusCheck interface
 *
 */
public class StatusCheckImpl implements StatusCheck
{
	@Resource
	private InstorecsContextFacade instorecsContextFacade;
	private boolean noConsignmentCheck = false;

	protected static Logger log = Logger.getLogger(StatusCheck.class.getName());

	@Override
	public Boolean isHandoverButtonShown(final OrderData orderData)
	{
		return Boolean.valueOf(isHandoverButtonShownIntegratedScenario(orderData)
				|| isHandoverButtonShownNonIntegratedScenario(orderData));
	}

	protected boolean isHandoverButtonShownIntegratedScenario(final OrderData orderData)
	{
		final PointOfServiceData agentPOS = getAgentsPointOfService();
		if (agentPOS == null || !isOrderValidForIntegratedScenario(orderData))
		{
			return false;
		}

		return noConsignmentCheck
				|| (checkOrderConsignments(orderData, null, ConsignmentStatus.WAITING) && checkPointOfServiceForOrderEntries(
						agentPOS, orderData));
	}

	protected boolean isHandoverButtonShownNonIntegratedScenario(final OrderData orderData)
	{
		final PointOfServiceData agentPOS = getAgentsPointOfService();
		if (agentPOS == null || !isOrderValidForNonIntegratedScenario(orderData))
		{
			return false;
		}

		return noConsignmentCheck || checkOrderConsignments(orderData, agentPOS, ConsignmentStatus.READY_FOR_PICKUP);
	}

	/**
	 * Checks, if for all order entries the point of service is equal to the agents POS.
	 *
	 * @param agentPOS
	 *           the POS of the agent
	 * @param orderData
	 *           the order to check the entries for.
	 * @return true if the all entries have the same point of service as the agents POS, false else.
	 */
	protected boolean checkPointOfServiceForOrderEntries(final PointOfServiceData agentPOS, final OrderData orderData)
	{
		final List<OrderEntryData> entries = orderData.getEntries();

		for (final OrderEntryData orderEntryData : entries)
		{
			final PointOfServiceData deliveryPointOfService = orderEntryData.getDeliveryPointOfService();
			if (deliveryPointOfService == null || !deliveryPointOfService.getName().equals(agentPOS.getName()))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks if the given order has a valid status and delivery mode for the integrated scenario.
	 *
	 * @param orderData
	 *           the order to check.
	 * @return true if status is CREATED or CHECKED_VALID and deliveryMode is PICKUP, else false.
	 */
	protected boolean isOrderValidForIntegratedScenario(final OrderData orderData)
	{
		final OrderStatus status = orderData.getStatus();
		final DeliveryModeData deliveryMode = orderData.getDeliveryMode();
		final boolean validStatus = status != null
				&& (OrderStatus.CREATED.equals(status) || OrderStatus.CHECKED_VALID.equals(status));
		return validStatus && deliveryMode != null && "pickup".equals(deliveryMode.getCode());
	}

	/**
	 * Checks if the given order has a valid status for the non integrated scenario.
	 *
	 * @param orderData
	 *           the order to check.
	 * @return true if status is ORDER_SPLIT, else false.
	 */
	protected boolean isOrderValidForNonIntegratedScenario(final OrderData orderData)
	{
		final OrderStatus status = orderData.getStatus();
		return status != null && OrderStatus.ORDER_SPLIT.equals(status);
	}

	@Override
	public Boolean isInvoiceLinkShown(final OrderData orderData)
	{
		if (!OrderStatus.COMPLETED.equals(orderData.getStatus()))
		{
			return Boolean.FALSE;
		}

		final PointOfServiceData agentPOS = getAgentsPointOfService();
		if (agentPOS == null)
		{
			return Boolean.FALSE;
		}

		return Boolean.valueOf(checkPointOfServiceForOrderEntries(agentPOS, orderData)
				&& checkOrderConsignments(orderData, null, ConsignmentStatus.SHIPPED));
	}

	protected boolean checkOrderConsignments(final OrderData orderData, final PointOfServiceData agentPOS,
			final ConsignmentStatus checkStatus)
	{
		final List<ConsignmentData> consignments = orderData.getConsignments();

		if (consignments == null || consignments.isEmpty())
		{
			return false;
		}

		for (final ConsignmentData consignment : consignments)
		{
			if (checkConsignmentStatusAndPOS(consignment, agentPOS, checkStatus))
			{
				return true;
			}
		}

		return false;
	}

	private PointOfServiceData getAgentsPointOfService()
	{
		PointOfServiceData agentPOS;
		try
		{
			agentPOS = instorecsContextFacade.getEmployeeStore();
		}
		catch (AssistedServiceAgentNotLoggedInException | StoreContextNotDefinedException e)
		{
			log.info("Can not determine agentPOS, Agent not logged In", e);
			return null;
		}
		return agentPOS;
	}

	private boolean checkConsignmentStatusAndPOS(final ConsignmentData consignment, final PointOfServiceData agentPOS,
			final ConsignmentStatus checkStatus)
	{
		if (!checkStatus.equals(consignment.getStatus()))
		{
			return false;
		}

		if (agentPOS == null)
		{
			return true;
		}

		return consignment.getDeliveryPointOfService() != null
				&& consignment.getDeliveryPointOfService().getName().equals(agentPOS.getName());
	}

	public InstorecsContextFacade getInstorecsContextFacade()
	{
		return instorecsContextFacade;
	}

	public void setInstorecsContextFacade(final InstorecsContextFacade instorecsAssistedServiceFacade)
	{
		this.instorecsContextFacade = instorecsAssistedServiceFacade;
	}

	public void setNoConsignmentCheck(final boolean noConsignmentCheck)
	{
		this.noConsignmentCheck = noConsignmentCheck;
	}
}
