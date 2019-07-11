/*****************************************************************************
Class: DefaultDeletePromotionsJob

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
import com.sap.retail.sapppspricing.model.DeletePromotionsCronJobModel;


/**
 * JobPerformable to delete promotions of the given criteria. Delegates to the PPS DB service layer
 */
public class DefaultDeletePromotionsJob extends DeleteExpiredObjectsJob<DeletePromotionsCronJobModel> implements
		JobPerformable<DeletePromotionsCronJobModel>
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultDeletePricesJob.class);

	@Override
	protected Logger getLogger()
	{
		return LOG;
	}

	@Override
	public PerformResult perform(final DeletePromotionsCronJobModel cronJobModel)
	{
		final Integer daysSinceExpiry = cronJobModel.getDaysSinceExpiry();

		if (daysSinceExpiry < 0)
		{
			LOG.error("Days since expiry value is not valid. This parameter must be greater equal than 0");
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}

		try
		{
			LOG.info("Deleting promotions expired since at least {} days", daysSinceExpiry);
			final Boolean deleteActivePromotions = cronJobModel.getDeleteActivePromotions();
			LOG.info("Also deleting active promotions: {}", deleteActivePromotions);
			final Timestamp expiryDate = daysBeforeNow(LocalDateTime.now(), daysSinceExpiry.longValue());
			new ApplicationContextProviderImpl().getContext().getBean("sapContextProvider", ContextProvider.class).init();
			final int deletedPromotions = getDbServiceAccessor().getPromotionService().deletePromotions(expiryDate,
					deleteActivePromotions.booleanValue());
			LOG.info("Number of deleted promotions: {}", deletedPromotions);
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		}
		catch (final Exception e)
		{
			LOG.error("Deletion of promotions failed", e);
			return new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
		}
	}


}
