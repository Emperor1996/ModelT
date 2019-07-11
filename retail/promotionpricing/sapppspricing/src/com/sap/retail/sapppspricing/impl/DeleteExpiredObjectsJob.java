/*****************************************************************************
Class: DeleteExpiredObjectsJob

@Copyright (c) 2016, SAP SE, Germany, All rights reserved.

 *****************************************************************************/

package com.sap.retail.sapppspricing.impl;

import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.user.UserService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

import org.slf4j.Logger;

import com.sap.ppengine.dataaccess.common.util.DBServiceAccessor;


/**
 * Abstract class holding common parts of delete promotions and delete prices
 *
 * @param <T>
 *           CronJob type
 */
public abstract class DeleteExpiredObjectsJob<T extends CronJobModel> extends AbstractJobPerformable<T>
{
	private static final String SAP_PPS_CRONJOB_USER_GROUP = "sap_pps_cronjob";
	private DBServiceAccessor dbServiceAccessor;
	private UserService userService;

	protected abstract Logger getLogger();

	protected Timestamp daysBeforeNow(final LocalDateTime now, final long dayCount)
	{
		return Timestamp.valueOf(now.minusDays(dayCount));
	}

	@Override
	public boolean isPerformable()
	{
		final Set<UserGroupModel> userGroups = userService.getAllUserGroupsForUser(userService.getCurrentUser());
		getLogger().info("Executed by User: {}", userService.getCurrentUser().getUid());
		for (final UserGroupModel spcUserGroup : userGroups)
		{
			if (SAP_PPS_CRONJOB_USER_GROUP.equals(spcUserGroup.getUid()))
			{
				return true;
			}
		}
		getLogger().error(
				"User is not allowed to execute this Cron Job. Only users from User Group {} are allowed to execute it !",
				SAP_PPS_CRONJOB_USER_GROUP);
		return false;
	}


	@SuppressWarnings("javadoc")
	public DBServiceAccessor getDbServiceAccessor()
	{
		return dbServiceAccessor;
	}

	@SuppressWarnings("javadoc")
	public void setDbServiceAccessor(final DBServiceAccessor dbServiceAccessor)
	{
		this.dbServiceAccessor = dbServiceAccessor;
	}

	@SuppressWarnings("javadoc")
	public UserService getUserService()
	{
		return userService;
	}

	@SuppressWarnings("javadoc")
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

}
