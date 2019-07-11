/*****************************************************************************
 Class:        GenericScoresDataContainer
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;

import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.model.CMSISCECustomer360ComponentModel;
import com.sap.retail.isce.service.sap.ISCEConfigurationService;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;
import com.sap.retail.isce.service.sap.result.ISCEObjectScoreResult;


/**
 * Implementation class for oData based container providing "Generic Scores". Values of "Generic Scores" are maintained
 * in CMS as a String list separated by column.
 */
public class GenericScoresDataContainer extends BaseScoresDataContainer
{
	private static final Object ELEMENT_OR = " or ";
	private static final String OPEN_PARENTHESIS = "(";
	private static final Object CLOSE_PARENTHESIS = ")";
	protected static final String PROPERTY_SCORE_DESCRIPTION = "ScoreDesc";

	protected List genericScoresList = new ArrayList<ISCEObjectScoreResult>();
	protected String genericScoresProperties = null;
	protected static Logger log = Logger.getLogger(GenericScoresDataContainer.class.getName());


	/**
	 * Default constructor.
	 *
	 */
	public GenericScoresDataContainer(final ISCEConfigurationService isceConfigurationService)
	{
		super(isceConfigurationService);
		this.containerName = GenericScoresDataContainer.class.getName();
		this.genericScoresProperties = null;
	}

	/**
	 * Gets the genericScoresProperties.
	 *
	 * @return the genericScoresProperties. If nothing is maintained, returned empty string.
	 */
	public String getGenericScoresProperties()
	{
		if (this.genericScoresProperties == null && this.cmsComponentModel != null)
		{
			final CMSISCECustomer360ComponentModel cmsCus360ComponentModel = (CMSISCECustomer360ComponentModel) cmsComponentModel;
			this.genericScoresProperties = cmsCus360ComponentModel.getCMSISCECustomer360GenericScores();
		}
		if (this.genericScoresProperties == null)
		{
			return "";
		}
		return genericScoresProperties;
	}

	/**
	 * Sets the genericScoresProperties.
	 *
	 * @param genericScoresProperties
	 *           the genericScoresProperties to set
	 */
	public void setGenericScoresProperties(final String genericScoresProperties)
	{
		this.genericScoresProperties = genericScoresProperties;
	}

	/**
	 * Returns the list containing the generic scores (description and weight).
	 *
	 * @return the genericScoresList
	 */
	public List getGenericScoresList()
	{
		return this.genericScoresList;
	}

	@Override
	public String getFilter()
	{

		final String[] scores = getGenericScoresProperties().split(",");

		final StringBuilder filter = new StringBuilder(super.getFilter()).append(OPEN_PARENTHESIS);

		for (int i = 0; i < scores.length; i++)
		{
			filter.append(PROPERTY_SCORE_ID).append(ELEMENT_EQ).append(ELEMENT_QUOTE).append(scores[i].trim()).append(ELEMENT_QUOTE);
			if (i < scores.length - 1)
			{
				filter.append(ELEMENT_OR);
			}
		}
		filter.append(CLOSE_PARENTHESIS);

		return filter.toString();
	}

	@Override
	public String getSelect()
	{
		return super.getSelect() + ELEMENT_COMMA + PROPERTY_SCORE_DESCRIPTION;
	}

	@Override
	public void setDataInErrorState()
	{
		this.genericScoresList = new ArrayList<ISCEObjectScoreResult>();
	}

	protected void setDataToNull()
	{
		this.genericScoresList = null;
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

		for (final ODataEntry oDataEntry : oDataEntries)
		{
			if (oDataEntry == null)
			{
				this.setDataToNull();
				return;
			}

			addScoreToGenericList(oDataEntry);
		}
	}

	/**
	 * @param oDataEntry
	 */
	protected void addScoreToGenericList(final ODataEntry oDataEntry)
	{
		final Map<String, Object> oDataEntryProperties = oDataEntry.getProperties();

		if (oDataEntryProperties == null || oDataEntryProperties.get(PROPERTY_SCORE_ID) == null
				|| oDataEntryProperties.get(PROPERTY_FORMATTED_SCORE_VALUE) == null)
		{
			log.error("oDataEntry has no Properties assigned");
			throw new DataContainerRuntimeException("oDataEntry has no Properties assigned");
		}

		final String scoreId = oDataEntryProperties.get(PROPERTY_SCORE_ID).toString();
		final String scoreDescription = oDataEntryProperties.get(PROPERTY_SCORE_DESCRIPTION).toString();
		final String scoreValueFormatted = oDataEntryProperties.get(PROPERTY_FORMATTED_SCORE_VALUE).toString();

		this.genericScoresList.add(new ISCEObjectScoreResult(scoreId, scoreDescription, scoreValueFormatted));
	}

	@Override
	public String getLocalizedContainerName()
	{
		return this.messageSource.getMessage("instorecs.customer360.genericScoresAttr", null, this.i18nService.getCurrentLocale());
	}

	@Override
	public String getContainerContextParamName()
	{
		return DATA_CONTAINER_CONTEXT_PARAM_NAME;
	}

	@Override
	public void encodeHTML()
	{
		//
	}

	@Override
	public void traceInformation()
	{
		super.traceInformation(log);
	}

}