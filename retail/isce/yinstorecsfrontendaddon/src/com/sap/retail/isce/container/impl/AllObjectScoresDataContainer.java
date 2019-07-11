/*****************************************************************************
 Class:        AllObjectScoresDataContainer
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;

import com.sap.retail.isce.service.sap.odata.HttpODataResult;
import com.sap.retail.isce.service.sap.result.ISCEObjectScoreResult;


/**
 * Data Container holding all ObjectScore Names and Ids
 */
public class AllObjectScoresDataContainer extends DataContainerODataDefaultImpl
{

	protected static final String DATA_CONTAINER_CONTEXT_PARAM_NAME = "allObjectScoresDataContainer";
	protected static Logger log = Logger.getLogger(AllObjectScoresDataContainer.class.getName());

	protected static final String SERVICE_URI = "/sap/opu/odata/sap/CUAN_ISCE_COMMON_SRV";
	protected static final String HTTP_DESTINATION = "ISCEHybrisMarketingHTTPDestination";
	protected static final String SERVICE_ENDPOINT_NAME = "ISCEObjectScores";

	protected static final String PROPERTY_SCORE_DESC = "ScoreDesc";
	protected static final String PROPERTY_SCORE_ID = "ScoreId";
	protected static final String PROPERTY_OBJECT_TYPE = "ObjectType";
	protected static final String PROPERTY_OBJECT_TYPE_CONCRETE_VALUE = "CUAN_CONSUMER";

	protected List<ISCEObjectScoreResult> objectScoresList = null;
	protected List<ISCEObjectScoreResult> unencodedObjectScoresList = null;

	/**
	 * Default Constructor.
	 */
	public AllObjectScoresDataContainer()
	{
		super();

		this.serviceURI = SERVICE_URI;
		this.serviceEndpointName = SERVICE_ENDPOINT_NAME;
		this.resultName = SERVICE_ENDPOINT_NAME;
		this.containerName = this.getClass().getName();
		this.httpDestinationName = HTTP_DESTINATION;
	}

	@Override
	public String getFilter()
	{
		return new StringBuilder("(").append(PROPERTY_OBJECT_TYPE).append(" eq '").append(PROPERTY_OBJECT_TYPE_CONCRETE_VALUE)
				.append("')").toString();
	}

	@Override
	public String getSelect()
	{
		return new StringBuilder(PROPERTY_SCORE_ID).append(",").append(PROPERTY_SCORE_DESC).toString();
	}

	@Override
	public void extractOwnDataFromResult(final HttpODataResult httpODataResult)
	{
		objectScoresList = new ArrayList<>();
		unencodedObjectScoresList = new ArrayList<>();

		if (httpODataResult == null || httpODataResult.getEntities() == null)
		{
			return;
		}

		final List<ODataEntry> oDataEntries = httpODataResult.getEntities();

		for (final ODataEntry oDataEntry : oDataEntries)
		{
			if (oDataEntry == null)
			{
				continue;
			}

			final Map<String, Object> oDataEntryProperties = oDataEntry.getProperties();

			final String scoreDescription = (String) oDataEntryProperties.get(PROPERTY_SCORE_DESC);
			final String scoreId = (String) oDataEntryProperties.get(PROPERTY_SCORE_ID);

			this.unencodedObjectScoresList.add(new ISCEObjectScoreResult(scoreId, scoreDescription, null));
			this.objectScoresList.add(new ISCEObjectScoreResult(encodeHTML(scoreId), encodeHTML(scoreDescription), null));
		}
	}

	/**
	 * Returns the list containing the HTML encoded object scores (description and weight).
	 *
	 * @return the objectScoresList the HTML encoded list of objectscores
	 */
	public List<ISCEObjectScoreResult> getObjectScoresList()
	{
		return this.objectScoresList;
	}

	/**
	 * Returns the list containing the unencoded object scores (description and weight).
	 *
	 * @return the objectScoresList the unencoded list of objectscores
	 */
	public List<ISCEObjectScoreResult> getUnencodedObjectScoresList()
	{
		return this.unencodedObjectScoresList;
	}

	@Override
	public String getLocalizedContainerName()
	{
		return de.hybris.platform.util.localization.Localization
				.getLocalizedString("type.CMSISCECustomer360Component.CMSISCECustomer360GenericScores.name");
	}

	@Override
	public String getOrderBy()
	{
		return PROPERTY_SCORE_DESC;
	}

	@Override
	public String getContainerContextParamName()
	{
		return DATA_CONTAINER_CONTEXT_PARAM_NAME;
	}

	@Override
	public void setDataInErrorState()
	{
		objectScoresList = null;
		unencodedObjectScoresList = null;
	}

	@Override
	public void encodeHTML()
	{
		//nothing to do
	}

	@Override
	public void traceInformation()
	{
		super.traceAllInformation(log, null);
	}

	@Override
	public String getTraceableFilter()
	{
		return null;
	}
}
