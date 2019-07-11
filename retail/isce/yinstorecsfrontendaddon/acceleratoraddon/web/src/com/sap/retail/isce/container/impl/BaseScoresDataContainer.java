/*****************************************************************************
Class:        BaseScoresDataContainer
Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import de.hybris.platform.commercefacades.user.data.CustomerData;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.sap.retail.isce.container.DataContainerContext;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.service.sap.ISCEConfigurationService;
import com.sap.retail.isce.service.util.SpringUtil;


/**
 * Base class for dealing with score objects like Generic Scores or Object Scores.
 */
public abstract class BaseScoresDataContainer extends DataContainerODataDefaultImpl
{
	protected static final String DATA_CONTAINER_CONTEXT_PARAM_NAME = "objectScoresDataContainer";
	protected static final String SERVICE_URI = "/sap/opu/odata/sap/CUAN_ISCE_COMMON_SRV";
	protected static final String SERVICE_ENDPOINT_NAME = "ISCEObjectScores";

	protected static final String ELEMENT_AND = " and ";
	protected static final String ELEMENT_QUOTE = "'";
	protected static final String ELEMENT_EQ = " eq ";
	protected static final String ELEMENT_COMMA = ",";


	protected static final String PROPERTY_SCORE_ID = "ScoreId";
	protected static final String PROPERTY_SCORE_VALUE = "ScoreValue";
	protected static final String PROPERTY_FORMATTED_SCORE_VALUE = "FormattedScoreValue";
	protected static final String PROPERTY_EMAIL_ADDRESS = "EMailAddress";
	protected static final String PROPERTY_OBJECT_TYPE = "ObjectType";
	protected static final String PROPERTY_OBJECT_TYPE_CONCRETE_VALUE = "CUAN_CONSUMER";

	protected SpringUtil springUtil;


	/**
	 * Default constructor with parameters.
	 */
	public BaseScoresDataContainer(final ISCEConfigurationService isceConfigurationService)
	{
		this.serviceURI = SERVICE_URI;
		this.serviceEndpointName = SERVICE_ENDPOINT_NAME;
		this.resultName = SERVICE_ENDPOINT_NAME;
		if (isceConfigurationService != null && isceConfigurationService.getYMktHttpDestination() != null)
		{
			this.httpDestinationName = isceConfigurationService.getYMktHttpDestination().getHttpDestinationName();
		}
	}

	/**
	 * Sets the springUtil context.
	 *
	 * @param springUtil
	 *           the springUtil to set
	 */
	@Required
	public void setSpringUtil(final SpringUtil springUtil)
	{
		this.springUtil = springUtil;
	}

	/**
	 * Writes trace information.
	 */
	protected void traceInformation(final Logger log)
	{
		super.traceAllInformation(log, getCustomerData().getUid());
	}

	@Override
	public String getTraceableFilter()
	{
		return getFilter().replace(getCustomerData().getUid(), "<value not traced>");
	}

	/**
	 * Get customer data from context map and check for being null. If yes, throw exception.
	 *
	 * @return customerData the customerData after checking for null
	 */
	protected CustomerData getCustomerData()
	{
		final CustomerData customerData = (CustomerData) this.dataContainerContext.getContextMap().get(
				DataContainerContext.CUSTOMER_DATA);

		if (customerData == null)
		{
			throw new DataContainerRuntimeException("CustomerData could not be retrieved from ContextMap");
		}
		return customerData;
	}

	@Override
	public String getSelect()
	{
		return new StringBuilder(PROPERTY_SCORE_ID).append(ELEMENT_COMMA).append(PROPERTY_SCORE_VALUE).append(ELEMENT_COMMA)
				.append(PROPERTY_FORMATTED_SCORE_VALUE).toString();
	}

	@Override
	public String getFilter()
	{
		final CustomerData customerData = getCustomerData();

		return new StringBuilder(PROPERTY_EMAIL_ADDRESS).append(ELEMENT_EQ).append(ELEMENT_QUOTE).append(customerData.getUid())
				.append(ELEMENT_QUOTE).append(ELEMENT_AND).append(PROPERTY_OBJECT_TYPE).append(ELEMENT_EQ).append(ELEMENT_QUOTE)
				.append(PROPERTY_OBJECT_TYPE_CONCRETE_VALUE).append(ELEMENT_QUOTE).append(ELEMENT_AND).toString();
	}

}