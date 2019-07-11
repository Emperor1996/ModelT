/*****************************************************************************
 Class:        ObjectScoresDataContainer
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;

import com.sap.retail.isce.container.DataContainerProperty;
import com.sap.retail.isce.container.DataContainerPropertyLevel;
import com.sap.retail.isce.exception.DataContainerPropertyLevelException;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.model.CMSISCECustomer360ComponentModel;
import com.sap.retail.isce.service.sap.ISCEConfigurationService;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;


/**
 * Implementation class for oData based container providing "Object Scores".
 */
public class ObjectScoresDataContainer extends BaseScoresDataContainer
{

	protected static final String SCORE_NAME_AGE = "CONTACT_AGE_SCORE";
	protected static final String SCORE_NAME_SENTIMENT = "CONTACT_SENTIMENT_SCORE";
	protected static final String SCORE_NAME_ACTIVITY = "CONTACT_ACTIVITY_SCORE";

	protected static final String CONTAINER_NAME_AGE = "AGE";
	protected static final String CONTAINER_NAME_SENTIMENT = "SENTIMENT";
	protected static final String CONTAINER_NAME_ACTIVITY = "ACTIVITY";

	private static final String RES_KEY_LOC_DC_NAME_STATISTICAL_DATA = "instorecs.customer360.statistical";

	protected static Logger log = Logger.getLogger(ObjectScoresDataContainer.class.getName());

	protected String ageInYears = null;
	protected String activityScore = null;
	protected String sentimentScore = null;
	protected String ageInYearsDescription;
	protected String activityScoreDescription;
	protected String sentimentScoreDescription;

	protected String scoreId = null;
	protected String containerId = null;

	protected DataContainerProperty activityScoreProperty;
	protected DataContainerProperty sentimentScoreProperty;

	/**
	 * Default constructor with parameters.
	 */
	public ObjectScoresDataContainer(final String scoreId, final String containerId,
			final ISCEConfigurationService isceConfigurationService)
	{
		super(isceConfigurationService);
		this.containerId = containerId;
		this.scoreId = scoreId;
		this.containerName = ObjectScoresDataContainer.class.getName() + containerId;
	}

	/**
	 * Default constructor.
	 */
	public ObjectScoresDataContainer(final ISCEConfigurationService isceConfigurationService)
	{
		super(isceConfigurationService);
		this.containerId = "";
		this.scoreId = "";
		this.containerName = ObjectScoresDataContainer.class.getName() + containerId;
	}


	@Override
	public String getContainerContextParamName()
	{
		return DATA_CONTAINER_CONTEXT_PARAM_NAME + this.containerId;
	}

	@Override
	public String getLocalizedContainerName()
	{
		switch (this.containerId.toUpperCase())
		{
			case CONTAINER_NAME_AGE:
				return this.messageSource.getMessage("instorecs.customer360.profile", null, this.i18nService.getCurrentLocale());
			case CONTAINER_NAME_SENTIMENT:
				return this.messageSource.getMessage(RES_KEY_LOC_DC_NAME_STATISTICAL_DATA, null, this.i18nService.getCurrentLocale());
			case CONTAINER_NAME_ACTIVITY:
				return this.messageSource.getMessage(RES_KEY_LOC_DC_NAME_STATISTICAL_DATA, null, this.i18nService.getCurrentLocale());
			default:
				return this.messageSource.getMessage(RES_KEY_LOC_DC_NAME_STATISTICAL_DATA, null, this.i18nService.getCurrentLocale());
		}

	}

	@Override
	public void extractOwnDataFromResult(final HttpODataResult httpODataResult)
	{
		Integer intValue = null;
		BigDecimal bigdecValue;
		String score = "";
		String formattedScore;

		createProperties();

		if (httpODataResult == null)
		{
			this.setDataToNull();
			return;
		}

		final List<ODataEntry> oDataEntries = httpODataResult.getEntities();

		if (oDataEntries == null || oDataEntries.isEmpty())
		{
			this.setDataToNull();
			return;
		}

		for (final ODataEntry oDataEntry : oDataEntries)
		{
			if (oDataEntry == null)
			{
				this.setDataToNull();
				return;
			}

			final Map<String, Object> oDataEntryProperties = oDataEntry.getProperties();

			checkODataPropertiesAreNotNull(oDataEntryProperties);

			bigdecValue = (BigDecimal) oDataEntryProperties.get(PROPERTY_SCORE_VALUE);
			if (bigdecValue != null)
			{
				// we expect only values that are pure integers without fractional digits: score = bigdecValue.toString(); //NOSONAR
				intValue = Integer.valueOf(bigdecValue.intValue());
				score = intValue.toString();
			}

			formattedScore = oDataEntryProperties.get(PROPERTY_FORMATTED_SCORE_VALUE).toString();

			setScoreMembers(intValue, score, formattedScore, oDataEntryProperties);
		}
	}

	/**
	 * Check properties in oData request for being null. If yes, throw exception.
	 *
	 * @param oDataEntryProperties
	 */
	protected void checkODataPropertiesAreNotNull(final Map<String, Object> oDataEntryProperties)
	{
		if (oDataEntryProperties == null || oDataEntryProperties.get(PROPERTY_SCORE_ID) == null
				|| oDataEntryProperties.get(PROPERTY_SCORE_VALUE) == null)
		{
			log.error("oDataEntry has no Properties assigned");
			throw new DataContainerRuntimeException("oDataEntry has no Properties assigned");
		}
	}

	/**
	 * Set the values of the various score members.
	 *
	 * @param intValue
	 * @param score
	 * @param formattedScore
	 * @param oDataEntryProperties
	 */
	protected void setScoreMembers(final Integer intValue, final String score, final String formattedScore,
			final Map<String, Object> oDataEntryProperties)
	{
		switch (oDataEntryProperties.get(PROPERTY_SCORE_ID).toString())
		{
			case SCORE_NAME_AGE:
				this.ageInYears = score;
				this.ageInYearsDescription = formattedScore;
				break;

			case SCORE_NAME_ACTIVITY:
				this.activityScoreProperty.setValue(intValue);
				this.activityScore = score;
				this.activityScoreDescription = formattedScore;
				break;
			case SCORE_NAME_SENTIMENT:
				this.sentimentScoreProperty.setValue(intValue);
				this.sentimentScore = score;
				this.sentimentScoreDescription = formattedScore;
				break;
			default:
				break;
		}
	}

	@Override
	public String getFilter()
	{
		return new StringBuffer(super.getFilter()).append(PROPERTY_SCORE_ID).append(ELEMENT_EQ).append(ELEMENT_QUOTE)
				.append(this.scoreId).append(ELEMENT_QUOTE).toString();
	}


	@Override
	public void setDataInErrorState()
	{
		setDataToNull();
	}

	@Override
	public void encodeHTML()
	{
		this.ageInYears = this.encodeHTML(this.ageInYears);
		this.ageInYearsDescription = this.encodeHTML(this.ageInYearsDescription);

		this.sentimentScore = this.encodeHTML(this.sentimentScore);
		this.sentimentScoreDescription = this.encodeHTML(this.sentimentScoreDescription);

		this.activityScore = this.encodeHTML(this.activityScore);
		this.activityScoreDescription = this.encodeHTML(this.activityScoreDescription);

		switch (this.containerId.toUpperCase())
		{
			case CONTAINER_NAME_SENTIMENT:
				this.sentimentScoreProperty.encodeHTML();
				break;
			case CONTAINER_NAME_ACTIVITY:
				this.activityScoreProperty.encodeHTML();
				break;
			default:
				//
		}

	}

	@Override
	public void determineDataForCMSComponent()
	{
		switch (this.containerId.toUpperCase())
		{
			case CONTAINER_NAME_SENTIMENT:
				determineSentimentPropertyLevels();

				break;
			case CONTAINER_NAME_ACTIVITY:
				determineActivityPropertyLevels(this.cmsComponentModel);
				break;
			default:
				// this container has no properties with levels
		}
	}

	/**
	 * Determines the levels for the activity property.
	 *
	 * @param cmsComponentModel
	 *           the model the level customizing is read from
	 */
	protected void determineActivityPropertyLevels(final AbstractCMSComponentModel cmsComponentModel)
	{
		try
		{
			final List<Comparable> cmslevelBoundaryPairs = getActivityPropertylevelBoundaryPairs(cmsComponentModel);

			final List<DataContainerPropertyLevel> levels = this.activityScoreProperty.createLevels(cmslevelBoundaryPairs);
			this.activityScoreProperty.determineCorrespondingLevel();
			adaptIntegerLevelUIValues(levels);
			updateLevelsDescription(levels);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			logExceptionAndAddMessage(e, "instorecs.customer360.statistical.activityScore");
		}
	}

	/**
	 * Calculates the level border pairs for the activity property.
	 *
	 * @param cmsComponentModel
	 *           the model the level customizing is read from
	 * @return the calculated level borders
	 * @throws DataContainerPropertyLevelException
	 */
	protected List<Comparable> getActivityPropertylevelBoundaryPairs(final AbstractCMSComponentModel cmsComponentModel)
			throws DataContainerPropertyLevelException
	{

		if (cmsComponentModel instanceof CMSISCECustomer360ComponentModel)
		{
			final CMSISCECustomer360ComponentModel cmsCus360ComponentModel = (CMSISCECustomer360ComponentModel) cmsComponentModel;
			final Integer level1 = cmsCus360ComponentModel.getCMSISCECustomer360ActivityScoreLevel01();
			final Integer level2 = cmsCus360ComponentModel.getCMSISCECustomer360ActivityScoreLevel02();
			final Integer level3 = cmsCus360ComponentModel.getCMSISCECustomer360ActivityScoreLevel03();
			final Integer level4 = cmsCus360ComponentModel.getCMSISCECustomer360ActivityScoreLevel04();

			final List<Comparable> levelThresholds = new ArrayList<>();
			levelThresholds.add(level1);
			levelThresholds.add(level2);
			levelThresholds.add(level3);
			levelThresholds.add(level4);
			return activityScoreProperty.calculateLevelBoundaryPairs(levelThresholds, true);
		}
		else
		{
			return new ArrayList<>();
		}
	}

	/**
	 * Update the property levels with the UI adapted values
	 *
	 * @param levels
	 *           the levels to be updated
	 */
	protected void adaptIntegerLevelUIValues(final List<DataContainerPropertyLevel> levels)
	{
		if (levels == null || levels.isEmpty())
		{
			return;
		}
		final Iterator iter = levels.iterator();
		DataContainerPropertyLevel level;
		while (iter.hasNext())
		{
			level = (DataContainerPropertyLevel) iter.next();
			adaptIntegerLevelUIValues4Level(iter, level);
		}
	}

	/**
	 * Adapts the values for a single level
	 *
	 * @param iter
	 * @param level
	 */
	private void adaptIntegerLevelUIValues4Level(final Iterator iter, final DataContainerPropertyLevel level)
	{
		int intBoundary;
		if (level.getLowValue() instanceof Integer)
		{
			if (iter.hasNext())
			{
				level.setAdaptedUILowValue(level.getLowValue());
			}
			else
			{
				intBoundary = ((Integer) level.getLowValue()).intValue();
				if (intBoundary > Integer.MIN_VALUE)
				{
					intBoundary = intBoundary - 1;
				}
				level.setAdaptedUILowValue(Integer.valueOf(intBoundary));
			}
		}
		if (level.getHighValue() instanceof Integer)
		{
			level.setAdaptedUIHighValue(level.getHighValue());
		}
	}

	/**
	 * Determines the levels for the sentiment property.
	 */
	protected void determineSentimentPropertyLevels()
	{
		final List<Comparable> cmslevelBoundaryPairs = getSentimentPropertylevelBoundaryPairs();
		try
		{
			final List<DataContainerPropertyLevel> levels = this.sentimentScoreProperty.createLevels(cmslevelBoundaryPairs);
			this.sentimentScoreProperty.determineCorrespondingLevel();
			updateLevelsDescription(levels);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			logExceptionAndAddMessage(e, "instorecs.customer360.statistical.sentimentScore");
		}
	}

	/**
	 * Determines the level border pairs for the sentiment property.
	 */
	protected List<Comparable> getSentimentPropertylevelBoundaryPairs()
	{
		final List<Comparable> levelBoundaryPairs = new ArrayList<>();

		levelBoundaryPairs.add(Integer.valueOf(1));
		levelBoundaryPairs.add(Integer.valueOf(1));
		levelBoundaryPairs.add(Integer.valueOf(2));
		levelBoundaryPairs.add(Integer.valueOf(2));
		levelBoundaryPairs.add(Integer.valueOf(3));
		levelBoundaryPairs.add(Integer.valueOf(3));
		levelBoundaryPairs.add(Integer.valueOf(4));
		levelBoundaryPairs.add(Integer.valueOf(4));
		levelBoundaryPairs.add(Integer.valueOf(5));
		levelBoundaryPairs.add(Integer.valueOf(5));
		return levelBoundaryPairs;
	}

	/**
	 * Returns the AgeInYears-score.
	 *
	 * @return the ageInYearS
	 */
	public String getAgeInYears()
	{
		return this.ageInYears;
	}

	/**
	 * Returns the activity-score.
	 *
	 * @return the activityScore
	 */
	public String getActivityScore()
	{
		return this.activityScore;
	}

	/**
	 * Returns the activity-score property.
	 *
	 * @return the activity score property
	 */
	public DataContainerProperty getActivityScoreProperty()
	{
		return this.activityScoreProperty;
	}

	/**
	 * Returns the sentiment-score.
	 *
	 * @return the sentimentScore
	 */
	public String getSentimentScore()
	{
		return this.sentimentScore;
	}

	/**
	 * Returns the sentiment-score property.
	 *
	 * @return the sentimentScore property
	 */
	public DataContainerProperty getSentimentScoreProperty()
	{
		return this.sentimentScoreProperty;
	}

	/**
	 * Gets the ageInYear description.
	 *
	 * @return the ageInYearsDescription
	 */
	public String getAgeInYearsDescription()
	{
		return ageInYearsDescription;
	}

	/**
	 * Gets the activitiyScore description.
	 *
	 * @return the activityScoreDescription
	 */
	public String getActivityScoreDescription()
	{
		return activityScoreDescription;
	}

	/**
	 * Gets the sentimenetScore description.
	 *
	 * @return the sentimentScoreDescription
	 */
	public String getSentimentScoreDescription()
	{
		return sentimentScoreDescription;
	}

	/**
	 * Creates the property variables that are used for the UI build.
	 */
	protected void createProperties()
	{
		String unitSingular;
		String unitPlural;

		switch (this.containerId.toUpperCase())
		{
			case CONTAINER_NAME_SENTIMENT:
				unitSingular = this.getMessage("instorecs.customer360.tilepopup.unit.star", null);
				unitPlural = this.getMessage("instorecs.customer360.tilepopup.unit.stars", null);
				this.sentimentScoreProperty = (DataContainerProperty) springUtil.getBean("dataContainerPropertyInteger", null,
						unitSingular, unitPlural);
				break;
			case CONTAINER_NAME_ACTIVITY:
				this.activityScoreProperty = (DataContainerProperty) springUtil.getBean("dataContainerPropertyInteger", null, "", "");
				break;
			default:
				// nothing to do
		}
	}

	/**
	 * Sets "Null" all the members.
	 */
	protected void setDataToNull()
	{
		this.ageInYears = null;
		this.activityScore = null;
		this.sentimentScore = null;
		this.createProperties();
	}

	@Override
	public void traceInformation()
	{
		super.traceInformation(log);
	}

}