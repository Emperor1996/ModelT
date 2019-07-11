/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.sap.retail.oaa.orderexchange.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.sap.orderexchange.outbound.OrderExchangeRepair;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.sap.retail.oaa.orderexchange.constants.ActionIds;
import com.sap.retail.oaa.orderexchange.constants.SapoaaorderexchangeConstants;




/**
 * Order sourcing is executed again in case of CAR was not available or sourcing wasn't successful
 */
public class OaaOrderExchangeRepairJob extends AbstractJobPerformable<CronJobModel>
{

	/**
	 *
	 */
	protected static final String STATUS_CODE_ORDER_CHECK_FAILED = "Order check failed.";

	private static final Logger LOG = Logger.getLogger(OaaOrderExchangeRepairJob.class);

	private OrderExchangeRepair orderExchangeRepair;
	private BusinessProcessService businessProcessService;


	@Override
	public PerformResult perform(final CronJobModel job)
	{
		List<OrderProcessModel> objectsToRepair;
		objectsToRepair = orderExchangeRepair.findAllProcessModelsToRepair(SapoaaorderexchangeConstants.ORDER_PROCESS_NAME,
				STATUS_CODE_ORDER_CHECK_FAILED);

		final Integer objectsToRepairCount = Integer.valueOf(objectsToRepair.size());
		LOG.info("Number of objects that are being repaired: " + objectsToRepairCount);

		for (final OrderProcessModel businessProcessModel : objectsToRepair)
		{
			businessProcessService.restartProcess(businessProcessModel, ActionIds.CHECK_SAP_ORDER);
		}

		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}



	@SuppressWarnings("javadoc")
	@Required
	public void setOrderExchangeRepair(final OrderExchangeRepair orderExchangeRepair)
	{
		this.orderExchangeRepair = orderExchangeRepair;
	}


	@SuppressWarnings("javadoc")
	@Required
	public void setBusinessProcessService(final BusinessProcessService businessProcessService)
	{
		this.businessProcessService = businessProcessService;
	}

}
