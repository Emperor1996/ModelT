/*****************************************************************************
 Class:        CARStatisticalDataContainerBase
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import de.hybris.platform.servicelayer.user.UserService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.sap.retail.isce.service.sap.ISCEConfigurationService;


/**
 * Abstract Base class for all implementations of oData based containers providing statistical data from the car system.
 */
public abstract class CARStatisticalDataContainerBase extends DataContainerODataDefaultImpl
{


	public static final String FROM_DATE_PROPERTY = "CreationDate";

	protected static final String SERVICE_ENDPOINT_NAME = "MultiChannelSalesOrdersQuery";
	protected static final String SERVICE_ENTITY_NAME = "MultiChannelSalesOrdersQueryResults";
	protected static final String NAVIGATION_PROPERTY = "/Results";
	protected static final String SERVICE_PARAMETER = "P_SAPClient";

	private static final String ODATA_DATE_FORMAT = "yyyyMMdd";

	private UserService userService;
	private final ISCEConfigurationService isceConfigurationService;


	/**
	 * Default constructor.
	 *
	 * @param isceConfigurationService
	 *           the ISCE configuration to use
	 */
	public CARStatisticalDataContainerBase(final ISCEConfigurationService isceConfigurationService)
	{
		super();
		this.isceConfigurationService = isceConfigurationService;
		this.serviceURI = getISCEConfigurationService().getCARServiceName();
		this.serviceEndpointName = SERVICE_ENDPOINT_NAME;
		this.keyPredicate = SERVICE_PARAMETER + "='" + getISCEConfigurationService().getCARSAPClient() + "'";
		this.navigationProperties = NAVIGATION_PROPERTY;
		this.resultName = SERVICE_ENTITY_NAME;
		if (getISCEConfigurationService().getCARHttpDestination() != null)
		{
			this.httpDestinationName = getISCEConfigurationService().getCARHttpDestination().getHttpDestinationName();
		}
	}

	/**
	 * Writes log information.
	 */
	protected void traceInformation(final Logger logger)
	{
		super.traceAllInformation(logger, null);
	}

	/**
	 * Returns the user service.
	 *
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
	}

	/**
	 * Sets the user service.
	 *
	 * @param userService
	 *           the userService to set
	 */
	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * Returns the configuration service.
	 *
	 * @return the isceConfigurationService
	 */
	public ISCEConfigurationService getISCEConfigurationService()
	{
		return isceConfigurationService;
	}

	@Override
	public String getLocalizedContainerName()
	{
		return this.messageSource.getMessage("instorecs.customer360.statistical", null, this.i18nService.getCurrentLocale());
	}

	/**
	 * Determines the list of Online Order Channels from the customizing.
	 *
	 * @return List<String> the list or Online Order channels
	 */
	protected List<String> getOnlineOrderChannels()
	{
		return Arrays.asList(getISCEConfigurationService().getCAROnlineOrderChannels().replaceAll(" ", "").split(","));
	}

	/**
	 * Determines the list of POS Order Channels from the customizing.
	 *
	 * @return List<String> the list or POS Order channels
	 */
	protected List<String> getPosOrderChannels()
	{
		return Arrays.asList(getISCEConfigurationService().getCARPOSOrderChannels().replaceAll(" ", "").split(","));
	}

	@Override
	public void encodeHTML()
	{
		//
	}

	/**
	 * Returns the current users customer number.
	 *
	 * @return the users customer number.
	 */
	protected String getCustomerNumber()
	{

		return this.getUserService().getCurrentUser().getProperty("CustomerID");
	}

	@Override
	public String getFilter()
	{

		final StringBuilder filter = new StringBuilder("CustomerNumber eq '").append(this.getCustomerNumber()).append("' and (");
		String orderChannelOr = "";

		final List<String> channels = new ArrayList<>(this.getOnlineOrderChannels());
		channels.addAll(this.getPosOrderChannels());

		for (final String orderChannel : channels)
		{
			filter.append(orderChannelOr).append("OrderChannel eq '").append(orderChannel).append("'");
			orderChannelOr = " or ";
		}

		return filter.append(")").toString();
	}


	@Override
	public String getTraceableFilter()
	{
		return null;
	}

	/**
	 * The filter with a time range of the last six months
	 *
	 * @return The filter with a time range of the last six months
	 */
	protected String getLastSixMonthFilter()
	{
		final String filterDate = new SimpleDateFormat(ODATA_DATE_FORMAT).format(this.calculateDateMinusSixMonths(new Date()));

		return FROM_DATE_PROPERTY + " ge '" + filterDate + "'";
	}

	/**
	 * Calculates the date 6 months before the given reference date.
	 *
	 * @param referenceDate
	 *           the date to base the calculation on
	 * @return the reference date minus 6 months
	 */
	public Date calculateDateMinusSixMonths(final Date referenceDate)
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTime(referenceDate);
		cal.add(Calendar.MONTH, -6);
		return cal.getTime();
	}

}