/*****************************************************************************
Class: DefaultDeletePricesJob

@Copyright (c) 2016, SAP SE, Germany, All rights reserved.

 *****************************************************************************/

package com.sap.retail.sapppspricing.impl;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.JobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.ppengine.core.ContextProvider;
import com.sap.ppengine.core.spring.impl.ApplicationContextProviderImpl;
import com.sap.retail.sapppspricing.model.DeletePricesCronJobModel;


/**
 * JobPerformable to delete prices of the given criteria. Delegates to the PPS DB service layer
 */
public class DefaultDeletePricesJob extends DeleteExpiredObjectsJob<DeletePricesCronJobModel> implements
		JobPerformable<DeletePricesCronJobModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultDeletePricesJob.class);

	@Override
	protected Logger getLogger()
	{
		return LOG;
	}

	@Override
	public PerformResult perform(final DeletePricesCronJobModel cronJobModel)
	{
		final Integer daysSinceExpiryPrice = cronJobModel.getDaysSinceExpiryPrice();


		if (daysSinceExpiryPrice < 0)
		{
			LOG.error("Days since expiry value is not valid. This parameter must be greater equal than 0");
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		try
		{
			LOG.info("Deleting prices expired since at least {} days", daysSinceExpiryPrice);
			final Timestamp expiryDate = daysBeforeNow(LocalDateTime.now(), daysSinceExpiryPrice.longValue());
			new ApplicationContextProviderImpl().getContext().getBean("sapContextProvider", ContextProvider.class).init();
			final int deleteCount = getDbServiceAccessor().getBasePriceService().deleteBasePricesByDate(expiryDate);
			LOG.info("Number of deleted prices: {}", deleteCount);
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		catch (final Exception e)
		{
			LOG.error("Deletion of prices failed", e);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
	}

}
