/*****************************************************************************
 Class:        MarketingAttributesDataContainer
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import de.hybris.platform.commercefacades.user.data.CustomerData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;

import com.sap.retail.isce.container.DataContainerContext;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.service.sap.ISCEConfigurationService;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;


/**
 * Implementation class for oData based container providing Marketing Attributes.
 */
public class MarketingAttributesDataContainer extends DataContainerODataDefaultImpl

{
	protected static final String DATA_CONTAINER_CONTEXT_PARAM_NAME = "marketingAttributesDataContainer";
	protected static final String SERVICE_ENDPOINT_NAME = "ContactPersons";

	protected static final String PROPERTY_MARITAL_STATUS_DESCRIPTION = "MaritalStatusDescription";
	protected static final String PROPERTY_GENDER_DESCRIPTION = "GenderDescription";
	protected static final String PROPERTY_EMAIL_ADDRESS = "EMailAddress";
	protected static final String PROPERTY_DATE_OF_BIRTH = "DateofBirth";

	protected static final String ELEMENT_QUOTE = "'";
	protected static final String ELEMENT_EQ = " eq ";
	protected static final String ELEMENT_COMMA = ",";

	protected static final String ODATA_DATE_PATTERN = "yyyy-MM-dd";

	protected static Logger log = Logger.getLogger(MarketingAttributesDataContainer.class.getName());

	protected String genderDescription = null;
	protected String maritalStatusDescription = null;
	protected GregorianCalendar dateOfBirth = null;
	protected Boolean isBirthDay = null;

	/**
	 * Default constructor.
	 */
	public MarketingAttributesDataContainer(final ISCEConfigurationService isceConfigurationService)
	{
		this.serviceURI = "/sap/opu/odata/sap/CUAN_ISCE_COMMON_SRV";
		this.containerName = MarketingAttributesDataContainer.class.getName();
		this.serviceEndpointName = SERVICE_ENDPOINT_NAME;
		this.resultName = SERVICE_ENDPOINT_NAME;
		if (isceConfigurationService != null && isceConfigurationService.getYMktHttpDestination() != null)
		{
			this.httpDestinationName = isceConfigurationService.getYMktHttpDestination().getHttpDestinationName();
		}
	}

	@Override
	public String getContainerContextParamName()
	{
		return DATA_CONTAINER_CONTEXT_PARAM_NAME;
	}

	/**
	 * @return the genderDescription
	 */
	public String getGenderDescription()
	{
		return this.genderDescription;
	}

	/**
	 * @return the maritalStatusDescription
	 */
	public String getMaritalStatusDescription()
	{
		return this.maritalStatusDescription;
	}

	/**
	 * @return the isBirthday
	 */
	public Boolean getIsBirthDay()
	{
		return isCurrentDateEqualToBirthDate(dateOfBirth);
	}

	/**
	 * Compare current date to given date and return whether they are equal or not.
	 *
	 * @param dateOfBirth
	 * @return TRUE if current date is birth date and FALSE if not.
	 */
	protected Boolean isCurrentDateEqualToBirthDate(final GregorianCalendar dateOfBirth)
	{
		if (dateOfBirth == null)
		{
			return Boolean.FALSE;
		}

		final Calendar today = Calendar.getInstance();
		final SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(ODATA_DATE_PATTERN);

		final String formattedCurrentDate = sdf.format(today.getTime());
		final String formattedBirthDate = sdf.format(dateOfBirth.getTime());

		return Boolean.valueOf(formattedCurrentDate.equals(formattedBirthDate));
	}

	@Override
	public String getLocalizedContainerName()
	{
		return this.messageSource.getMessage("instorecs.customer360.profile", null, this.i18nService.getCurrentLocale());
	}

	@Override
	public void extractOwnDataFromResult(final HttpODataResult httpODataResult)
	{
		if (httpODataResult == null)
		{
			this.setDataToNull();
			return;
		}
		final ODataEntry oDataEntry = httpODataResult.getEntity();
		if (oDataEntry == null)
		{
			return;
		}
		final Map<String, Object> oDataEntryProperties = oDataEntry.getProperties();
		if (oDataEntryProperties == null)
		{
			log.error("oDataEntry has no Properties assigned");
			throw new DataContainerRuntimeException("oDataEntry has no Properties assigned");
		}
		this.genderDescription = oDataEntryProperties.get(PROPERTY_GENDER_DESCRIPTION).toString();
		this.maritalStatusDescription = oDataEntryProperties.get(PROPERTY_MARITAL_STATUS_DESCRIPTION).toString();
		this.dateOfBirth = (GregorianCalendar) oDataEntryProperties.get(PROPERTY_DATE_OF_BIRTH);
	}

	@Override
	public String getFilter()
	{
		final CustomerData customerData = getCustomerData();

		return new StringBuilder(PROPERTY_EMAIL_ADDRESS).append(ELEMENT_EQ).append(ELEMENT_QUOTE).append(customerData.getUid())
				.append(ELEMENT_QUOTE).toString();
	}

	@Override
	public String getSelect()
	{
		return new StringBuilder(PROPERTY_GENDER_DESCRIPTION).append(ELEMENT_COMMA).append(PROPERTY_MARITAL_STATUS_DESCRIPTION)
				.append(ELEMENT_COMMA).append(PROPERTY_DATE_OF_BIRTH).toString();
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
	public void setDataInErrorState()
	{
		setDataToNull();
	}

	protected void setDataToNull()
	{
		this.genderDescription = null;
		this.maritalStatusDescription = null;
	}

	@Override
	public void encodeHTML()
	{
		this.genderDescription = this.encodeHTML(this.genderDescription);
		this.maritalStatusDescription = this.encodeHTML(this.maritalStatusDescription);
	}

	@Override
	public void traceInformation()
	{
		super.traceAllInformation(log, getCustomerData().getUid());
	}

}
