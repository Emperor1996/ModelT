/*****************************************************************************
 Class:        ItemsOfInterestDataContainer
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import de.hybris.platform.commercefacades.user.data.CustomerData;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;

import com.sap.retail.isce.container.DataContainerContext;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.service.sap.ISCEConfigurationService;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;
import com.sap.retail.isce.service.sap.result.ISCEItemOfInterestResult;


/**
 * Implementation class for oData based container providing "Items Of Interest".
 */
public class ItemsOfInterestDataContainer extends DataContainerODataDefaultImpl
{
	protected static Logger log = Logger.getLogger(ItemsOfInterestDataContainer.class.getName());

	protected static final String DATA_CONTAINER_CONTEXT_PARAM_NAME = "itemsOfInterestDataContainer";
	protected static final String SERVICE_URI = "/sap/opu/odata/sap/CUAN_ISCE_COMMON_SRV";
	protected static final String SERVICE_ENDPOINT_NAME = "ContactAnalyses";

	protected static final String ELEMENT_AND = " and ";
	protected static final String ELEMENT_QUOTE = "'";
	protected static final String ELEMENT_EQ = " eq ";
	protected static final String ELEMENT_COMMA = ",";
	protected static final String ELEMENT_GE = " ge ";
	protected static final String ELEMENT_LE = " le ";
	protected static final String ELEMENT_FN_DATETIME = "datetime";

	protected static final String PROPERTY_INTEREST_DESCRITPTION = "InterestDescription";
	protected static final String PROPERTY_INTEREST_CODE = "InterestCode";
	protected static final String PROPERTY_INTEREST_COUNT = "InterestCount";
	protected static final String PROPERTY_VALUATION_AVERAGE = "ValuationAverage";
	protected static final String PROPERTY_TIMESTAMP = "InteractionTimestamp";
	protected static final String PROPERTY_EMAIL_ADDRESS = "EMailAddress";

	protected static final String ODATA_DATE_PATTERN = "yyyy-MM-dd";
	protected static final String TIMESTAMP_APPENDIX_MIN = "T00:00:00.0000000";
	protected static final String TIMESTAMP_APPENDIX_MAX = "T23:59:59.0000000";


	protected List itemsOfInterestList = new ArrayList<ISCEItemOfInterestResult>();

	/**
	 * Default constructor.
	 */
	public ItemsOfInterestDataContainer(final ISCEConfigurationService isceConfigurationService)
	{
		if (isceConfigurationService != null && isceConfigurationService.getYMktHttpDestination() != null)
		{
			this.httpDestinationName = isceConfigurationService.getYMktHttpDestination().getHttpDestinationName();
		}
		this.serviceURI = SERVICE_URI;
		this.serviceEndpointName = SERVICE_ENDPOINT_NAME;
		this.resultName = SERVICE_ENDPOINT_NAME;
		this.containerName = ItemsOfInterestDataContainer.class.getName();
	}

	@Override
	public String getLocalizedContainerName()
	{
		return this.messageSource.getMessage("instorecs.customer360.interests", null, this.i18nService.getCurrentLocale());
	}

	@Override
	public void extractOwnDataFromResult(final HttpODataResult httpODataResult)
	{
		if (httpODataResult == null)
		{
			this.setDataToNull();
			return;
		}
		final List<ODataEntry> oDataEntries = httpODataResult.getEntities();

		if (oDataEntries == null)
		{
			return;
		}

		String interestDescription;
		String interestCode;
		BigInteger valuationAverage;

		for (final ODataEntry oDataEntry : oDataEntries)
		{
			final Map<String, Object> oDataEntryProperties = oDataEntry.getProperties();
			if (oDataEntryProperties == null)
			{
				log.error("oDataEntry has no Properties assigned");
				throw new DataContainerRuntimeException("oDataEntry has no Properties assigned");
			}

			interestDescription = oDataEntryProperties.get(PROPERTY_INTEREST_DESCRITPTION).toString();
			interestCode = oDataEntryProperties.get(PROPERTY_INTEREST_CODE).toString();
			valuationAverage = ((BigDecimal) oDataEntryProperties.get(PROPERTY_VALUATION_AVERAGE)).toBigInteger();
			if (valuationAverage != BigInteger.ZERO)
			{
				itemsOfInterestList.add(new ISCEItemOfInterestResult(interestCode, interestDescription, valuationAverage));
			}
		}
	}

	@Override
	public String getFilter()
	{
		final CustomerData customerData = getCustomerData();

		final String dateFilter = getDateFilter();

		return new StringBuilder(PROPERTY_EMAIL_ADDRESS).append(ELEMENT_EQ).append(ELEMENT_QUOTE).append(customerData.getUid())
				.append(ELEMENT_QUOTE).append(ELEMENT_AND).append(dateFilter).toString();
	}

	@Override
	public String getSelect()
	{
		return new StringBuilder(PROPERTY_INTEREST_CODE).append(ELEMENT_COMMA).append(PROPERTY_INTEREST_DESCRITPTION)
				.append(ELEMENT_COMMA).append(PROPERTY_INTEREST_COUNT).append(ELEMENT_COMMA).append(PROPERTY_VALUATION_AVERAGE)
				.toString();
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
		this.itemsOfInterestList = null;
	}

	@Override
	public void encodeHTML()

	{
		if (this.itemsOfInterestList == null)
		{
			return;
		}

		for (int i = 0; i < this.itemsOfInterestList.size(); i++)
		{
			final ISCEItemOfInterestResult interest = (ISCEItemOfInterestResult) this.itemsOfInterestList.get(i);

			interest.setInterestCode(this.encodeHTML(interest.getInterestCode()));
			interest.setInterestDescription(this.encodeHTML(interest.getInterestDescription()));
		}
	}

	@Override
	public String getContainerContextParamName()
	{
		return DATA_CONTAINER_CONTEXT_PARAM_NAME;
	}

	/**
	 * Return the list containing the items of interest.
	 *
	 * @return the itemsOfInterestList
	 */
	public List getItemsOfInterestList()
	{
		return this.itemsOfInterestList;
	}

	/**
	 * Return the "date" part of the filter clause.
	 *
	 * @return date filter
	 */
	public String getDateFilter()
	{
		final Calendar today = Calendar.getInstance();
		final Calendar todayMinusOneYear = Calendar.getInstance();
		todayMinusOneYear.add(Calendar.YEAR, -1);

		final SimpleDateFormat sdf = new SimpleDateFormat();
		sdf.applyPattern(ODATA_DATE_PATTERN);

		String formattedMinDate = sdf.format(todayMinusOneYear.getTime());
		formattedMinDate = formattedMinDate + TIMESTAMP_APPENDIX_MIN;

		String formattedMaxDate = sdf.format(today.getTime());
		formattedMaxDate = formattedMaxDate + TIMESTAMP_APPENDIX_MAX;

		return new StringBuilder(PROPERTY_TIMESTAMP).append(ELEMENT_GE).append(ELEMENT_FN_DATETIME).append(ELEMENT_QUOTE)
				.append(formattedMinDate).append(ELEMENT_QUOTE).append(ELEMENT_AND).append(PROPERTY_TIMESTAMP).append(ELEMENT_LE)
				.append(ELEMENT_FN_DATETIME).append(ELEMENT_QUOTE).append(formattedMaxDate).append(ELEMENT_QUOTE).toString();
	}

	@Override
	public void traceInformation()
	{
		super.traceAllInformation(log, getCustomerData().getUid());
	}

	@Override
	public String getTraceableFilter()
	{
		return getFilter().replace(getCustomerData().getUid(), "<value not traced>");
	}
}
