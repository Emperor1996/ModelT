/*****************************************************************************
 Class:        StatisticalDataContainer
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Required;

import com.sap.retail.isce.container.DataContainer;
import com.sap.retail.isce.container.DataContainerProperty;
import com.sap.retail.isce.container.DataContainerPropertyLevel;
import com.sap.retail.isce.exception.DataContainerPropertyLevelException;
import com.sap.retail.isce.exception.DataContainerServiceException;
import com.sap.retail.isce.model.CMSISCECustomer360ComponentModel;
import com.sap.retail.isce.service.util.SpringUtil;
import com.sap.retail.isce.service.util.StatisticalDataSalesVolumeUtil;


/**
 * Class to merge statistical data from hybris marketing and hybris commerce.
 */
public class StatisticalDataContainer extends DataContainerCombinedDefaultImpl
{
	private static final String BEAN_NAME_DC_PROP_CURRENCY_BD = "dataContainerPropertyCurrencyBD";
	private static final String BEAN_NAME_DC_PROP_INTEGER = "dataContainerPropertyInteger";

	protected static Logger log = Logger.getLogger(StatisticalDataContainer.class.getName());

	protected Map<String, DataContainer> dataContainerMap;

	private PriceDataFactory priceDataFactory;
	protected SpringUtil springUtil;
	protected StatisticalDataSalesVolumeUtil statisticalDataSalesVolumeUtil;
	protected DataContainerProperty salesVolumeProperty;
	protected DataContainerProperty averageSalesVolumeProperty;
	protected DataContainerProperty lastPurchaseDateProperty;
	protected DataContainerProperty storePurchaseRatioProperty;

	private final List<String> dataContainerNames = Arrays.asList("CARStatisticalDataContainerOverallVolume",
			"CARStatisticalDataContainerOverallCount", "CARStatisticalDataContainerSixMonthVolume",
			"CARStatisticalDataContainerLastPurchase", "CARStatisticalDataContainerOverallItemsCount");

	/*
	 * default constructor
	 */
	StatisticalDataContainer()
	{
		this.containerName = StatisticalDataContainer.class.getName();
	}

	/**
	 * @return the priceDataFactory
	 */
	public PriceDataFactory getPriceDataFactory()
	{
		return priceDataFactory;
	}

	/**
	 * @param priceDataFactory
	 *           the priceDataFactory to set
	 */
	@Required
	public void setPriceDataFactory(final PriceDataFactory priceDataFactory)
	{
		this.priceDataFactory = priceDataFactory;
	}

	/**
	 * Returns the statisticalDataSalesVolumeUtil.
	 *
	 * @return the statisticalDataSalesVolumeUtil
	 */
	public StatisticalDataSalesVolumeUtil getStatisticalDataSalesVolumeUtil()
	{
		return statisticalDataSalesVolumeUtil;
	}

	/**
	 * Sets the statisticalDataSalesVolumeUtil.
	 *
	 * @param statisticalDataSalesVolumeUtil
	 *           the statisticalDataSalesVolumeUtil to set
	 */
	@Required
	public void setStatisticalDataSalesVolumeUtil(final StatisticalDataSalesVolumeUtil statisticalDataSalesVolumeUtil)
	{
		this.statisticalDataSalesVolumeUtil = statisticalDataSalesVolumeUtil;
	}

	/**
	 * @param springUtil
	 *           the springUtil to set
	 */
	@Required
	public void setSpringUtil(final SpringUtil springUtil)
	{
		this.springUtil = springUtil;
	}

	@Override
	public List<String> getDataContainerNamesForCombining()
	{
		return this.dataContainerNames;
	}

	@Override
	public String getContainerContextParamName()
	{
		return "statisticalDataContainer";
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.container.impl.DataContainerDefaultImpl#determineDataForCMSComponent()
	 */
	@Override
	public void determineDataForCMSComponent()
	{
		determineAverageSalesVolumePropertyLevels(this.cmsComponentModel);
		determineSalesVolumePropertyLevels(this.cmsComponentModel);
		determineLastPurchaseDatePropertyLevels(this.cmsComponentModel);
		determineStorePurchaseRatioPropertyLevels(this.cmsComponentModel);
	}

	/**
	 * Determines the levels for the averageSalesVolume property.
	 *
	 * @param cmsComponentModel
	 *           the model the level customizing is read from
	 */
	protected void determineAverageSalesVolumePropertyLevels(final AbstractCMSComponentModel cmsComponentModel)
	{
		try
		{
			final List<Comparable> cmslevelBoundaryPairs = getAverageSalesVolumePropertylevelBoundaryPairs(cmsComponentModel);
			final List<DataContainerPropertyLevel> levels = this.averageSalesVolumeProperty.createLevels(cmslevelBoundaryPairs);
			this.averageSalesVolumeProperty.determineCorrespondingLevel();

			adaptBigDecimalLevelUIValues(levels);
			updateLevelsDescription(levels);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			logExceptionAndAddMessage(e, "instorecs.customer360.statistical.averageSalesVolume");
		}
	}

	/**
	 * Calculates the level border pairs for the AverageSalesVolume property.
	 *
	 * @param cmsComponentModel
	 *           the model the level customizing is read from
	 * @return the calculated level borders
	 * @throws DataContainerPropertyLevelException
	 */
	protected List<Comparable> getAverageSalesVolumePropertylevelBoundaryPairs(final AbstractCMSComponentModel cmsComponentModel)
			throws DataContainerPropertyLevelException
	{
		if (cmsComponentModel instanceof CMSISCECustomer360ComponentModel)
		{
			final CMSISCECustomer360ComponentModel cmsCus360ComponentModel = (CMSISCECustomer360ComponentModel) cmsComponentModel;
			final Integer level1 = cmsCus360ComponentModel.getCMSISCECustomer360AveragePurchaseVolumeLevel01();
			final Integer level2 = cmsCus360ComponentModel.getCMSISCECustomer360AveragePurchaseVolumeLevel02();
			final Integer level3 = cmsCus360ComponentModel.getCMSISCECustomer360AveragePurchaseVolumeLevel03();
			final Integer level4 = cmsCus360ComponentModel.getCMSISCECustomer360AveragePurchaseVolumeLevel04();

			final List<Comparable> levelThresholds = createBigDecimalLevelThresholdList(level1, level2, level3, level4);

			return averageSalesVolumeProperty.calculateLevelBoundaryPairs(levelThresholds, true);
		}
		else
		{
			return new ArrayList<>();
		}
	}

	/**
	 * Creates the list of level thresholds
	 *
	 * @param level1
	 * @param level2
	 * @param level3
	 * @param level4
	 * @return the created list
	 */
	protected List<Comparable> createBigDecimalLevelThresholdList(final Integer level1, final Integer level2,
			final Integer level3, final Integer level4)
	{
		final BigDecimal bdLevel1 = (level1 != null) ? BigDecimal.valueOf(level1.intValue()) : null;
		final BigDecimal bdLevel2 = (level2 != null) ? BigDecimal.valueOf(level2.intValue()) : null;
		final BigDecimal bdLevel3 = (level3 != null) ? BigDecimal.valueOf(level3.intValue()) : null;
		final BigDecimal bdLevel4 = (level4 != null) ? BigDecimal.valueOf(level4.intValue()) : null;

		final List<Comparable> levelThresholds = new ArrayList<>();
		levelThresholds.add(bdLevel1);
		levelThresholds.add(bdLevel2);
		levelThresholds.add(bdLevel3);
		levelThresholds.add(bdLevel4);
		return levelThresholds;
	}

	/**
	 * Determines the levels for the salesVolume property.
	 *
	 * @param cmsComponentModel
	 *           the model the level customizing is read from
	 */
	protected void determineSalesVolumePropertyLevels(final AbstractCMSComponentModel cmsComponentModel)
	{
		try
		{
			final List<Comparable> cmslevelBoundaryPairs = getSalesVolumePropertylevelBoundaryPairs(cmsComponentModel);
			final List<DataContainerPropertyLevel> levels = this.salesVolumeProperty.createLevels(cmslevelBoundaryPairs);
			this.salesVolumeProperty.determineCorrespondingLevel();
			adaptBigDecimalLevelUIValues(levels);
			updateLevelsDescription(levels);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			logExceptionAndAddMessage(e, "instorecs.customer360.statistical.salesVolume");
		}
	}

	/**
	 * Calculates the level border pairs for the SalesVolume property.
	 *
	 * @param cmsComponentModel
	 *           the model the level customizing is read from
	 * @return the calculated level borders
	 * @throws DataContainerPropertyLevelException
	 */
	protected List<Comparable> getSalesVolumePropertylevelBoundaryPairs(final AbstractCMSComponentModel cmsComponentModel)
			throws DataContainerPropertyLevelException
	{
		if (cmsComponentModel instanceof CMSISCECustomer360ComponentModel)
		{
			final CMSISCECustomer360ComponentModel cmsCus360ComponentModel = (CMSISCECustomer360ComponentModel) cmsComponentModel;
			final Integer level1 = cmsCus360ComponentModel.getCMSISCECustomer360SalesVolumeLevel01();
			final Integer level2 = cmsCus360ComponentModel.getCMSISCECustomer360SalesVolumeLevel02();
			final Integer level3 = cmsCus360ComponentModel.getCMSISCECustomer360SalesVolumeLevel03();
			final Integer level4 = cmsCus360ComponentModel.getCMSISCECustomer360SalesVolumeLevel04();

			final List<Comparable> levelThresholds = createBigDecimalLevelThresholdList(level1, level2, level3, level4);

			return salesVolumeProperty.calculateLevelBoundaryPairs(levelThresholds, true);
		}
		else
		{
			return new ArrayList<>();
		}
	}

	/**
	 * Determines the levels for the lastPurchaseDate property.
	 *
	 * @param cmsComponentModel
	 *           the model the level customizing is read from
	 */
	protected void determineLastPurchaseDatePropertyLevels(final AbstractCMSComponentModel cmsComponentModel)
	{
		try
		{
			final List<Comparable> cmslevelBoundaryPairs = getLastPurchaseDatePropertylevelBoundaryPairs(cmsComponentModel);

			final List<DataContainerPropertyLevel> levels = this.lastPurchaseDateProperty.createLevels(cmslevelBoundaryPairs);
			this.lastPurchaseDateProperty.determineCorrespondingLevel();
			adaptIntegerLevelUIValues(levels, false);
			updateLevelsDescription(levels);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			logExceptionAndAddMessage(e, "instorecs.customer360.statistical.lastPurchaseDate");
		}
	}

	/**
	 * Calculates the level border pairs for the lastPurchaseDate property.
	 *
	 * @param cmsComponentModel
	 *           the model the level customizing is read from
	 * @return the calculated level borders
	 * @throws DataContainerPropertyLevelException
	 */
	protected List<Comparable> getLastPurchaseDatePropertylevelBoundaryPairs(final AbstractCMSComponentModel cmsComponentModel)
			throws DataContainerPropertyLevelException
	{
		if (cmsComponentModel instanceof CMSISCECustomer360ComponentModel)
		{
			final CMSISCECustomer360ComponentModel cmsCus360ComponentModel = (CMSISCECustomer360ComponentModel) cmsComponentModel;
			final Integer level1 = cmsCus360ComponentModel.getCMSISCECustomer360LastPurchaseDateLevel01();
			final Integer level2 = cmsCus360ComponentModel.getCMSISCECustomer360LastPurchaseDateLevel02();
			final Integer level3 = cmsCus360ComponentModel.getCMSISCECustomer360LastPurchaseDateLevel03();
			final Integer level4 = cmsCus360ComponentModel.getCMSISCECustomer360LastPurchaseDateLevel04();

			final List<Comparable> levelThresholds = new ArrayList<>();
			levelThresholds.add(level1);
			levelThresholds.add(level2);
			levelThresholds.add(level3);
			levelThresholds.add(level4);

			return lastPurchaseDateProperty.calculateLevelBoundaryPairs(levelThresholds, false);
		}
		else
		{
			return new ArrayList<>();
		}
	}

	/**
	 * Determines the levels for the storePurchaseRatio property.
	 *
	 * @param cmsComponentModel
	 *           the model the level customizing is read from
	 */
	protected void determineStorePurchaseRatioPropertyLevels(final AbstractCMSComponentModel cmsComponentModel)
	{
		try
		{
			final List<Comparable> cmslevelBoundaryPairs = getStorePurchaseRatioPropertylevelBoundaryPairs(cmsComponentModel);

			final List<DataContainerPropertyLevel> levels = this.storePurchaseRatioProperty.createLevels(cmslevelBoundaryPairs);
			this.storePurchaseRatioProperty.determineCorrespondingLevel();
			adaptIntegerLevelUIValues(levels, true);
			updateLevelsDescription(levels);
		}
		catch (final DataContainerPropertyLevelException e)
		{
			logExceptionAndAddMessage(e, "instorecs.customer360.statistical.storePurchaseRatio");
		}
	}

	/**
	 * Calculates the level border pairs for the StorePurchase property.
	 *
	 * @param cmsComponentModel
	 *           the model the level customizing is read from
	 * @return the calculated level borders
	 * @throws DataContainerPropertyLevelException
	 */
	protected List<Comparable> getStorePurchaseRatioPropertylevelBoundaryPairs(final AbstractCMSComponentModel cmsComponentModel)
			throws DataContainerPropertyLevelException
	{
		if (cmsComponentModel instanceof CMSISCECustomer360ComponentModel)
		{
			final CMSISCECustomer360ComponentModel cmsCus360ComponentModel = (CMSISCECustomer360ComponentModel) cmsComponentModel;
			final Integer level1 = cmsCus360ComponentModel.getCMSISCECustomer360StoreOnlineRatioLevel01();
			final Integer level2 = cmsCus360ComponentModel.getCMSISCECustomer360StoreOnlineRatioLevel02();
			final Integer level3 = cmsCus360ComponentModel.getCMSISCECustomer360StoreOnlineRatioLevel03();
			final Integer level4 = cmsCus360ComponentModel.getCMSISCECustomer360StoreOnlineRatioLevel04();

			final List<Comparable> levelThresholds = new ArrayList<>();
			levelThresholds.add(level1);
			levelThresholds.add(level2);
			levelThresholds.add(level3);
			levelThresholds.add(level4);

			return storePurchaseRatioProperty.calculateLevelBoundaryPairs(levelThresholds, true);
		}
		else
		{
			return new ArrayList<>();
		}
	}

	/**
	 * Calculated the distance between last purchase date and today in days
	 *
	 * @return distance in days
	 */
	protected Integer getLastPurchaseDateDistance()
	{
		final Date lastPurchaseDate = getLastPurchaseDate();
		if (lastPurchaseDate == null)
		{
			return null;
		}
		else
		{
			return Integer.valueOf(Days.daysBetween(new DateTime(lastPurchaseDate), new DateTime(getToday())).getDays());
		}
	}

	/**
	 * get the date of today
	 *
	 * @return the date of today
	 */
	protected Date getToday()
	{
		return new Date();
	}

	/**
	 * Update the property level UI values with the adapted UI values
	 *
	 * @param levels
	 *           the levels to be updated
	 */
	protected void adaptBigDecimalLevelUIValues(final List<DataContainerPropertyLevel> levels)
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
			adaptBigDecimalLevelUIValues4Level(iter, level);
		}
	}

	/**
	 * Adapts the values for a single level
	 *
	 * @param iter
	 * @param level
	 */
	private void adaptBigDecimalLevelUIValues4Level(final Iterator iter, final DataContainerPropertyLevel level)
	{
		if (level.getLowValue() instanceof BigDecimal)
		{
			if (iter.hasNext())
			{
				level.setAdaptedUILowValue(getPrice((BigDecimal) level.getLowValue()));
			}
			else
			{
				level.setAdaptedUILowValue(getPrice(((BigDecimal) level.getLowValue()).subtract(BigDecimal.valueOf(1))));
			}
		}
		if (level.getHighValue() instanceof BigDecimal)
		{
			level.setAdaptedUIHighValue(getPrice((BigDecimal) level.getHighValue()));
		}
	}

	/**
	 * Update the property levels with the formatted values
	 *
	 * @param levels
	 * @param ascending
	 */
	protected void adaptIntegerLevelUIValues(final List<DataContainerPropertyLevel> levels, final boolean ascending)
	{
		DataContainerPropertyLevel level;
		boolean findFirst = true;

		if (levels == null || levels.isEmpty())
		{
			return;
		}
		final Iterator iter = levels.iterator();

		while (iter.hasNext())
		{
			level = (DataContainerPropertyLevel) iter.next();
			if (level.getLowValue() instanceof Integer)
			{
				findFirst = adaptIntegerLevelUILowValue(ascending, level, findFirst, iter);
			}
			if (level.getHighValue() instanceof Integer)
			{
				adaptIntegerLevelUIHighValue(ascending, level, iter);
			}
		}
	}

	/**
	 * Adapt the high integer level value for UI
	 *
	 * @param ascending
	 * @param level
	 * @param iter
	 */
	protected void adaptIntegerLevelUIHighValue(final boolean ascending, final DataContainerPropertyLevel level,
			final Iterator iter)
	{
		if (!(level.getHighValue() instanceof Integer))
		{
			return;
		}
		Integer high = (Integer) level.getHighValue();
		final int intHigh = high.intValue();

		// last descending found
		if (!ascending && !iter.hasNext() && intHigh < Integer.MAX_VALUE)
		{
			high = Integer.valueOf(intHigh + 1);
		}
		level.setAdaptedUIHighValue(high);
	}

	/**
	 * Adapt the low integer level value for UI
	 *
	 * @param ascending
	 * @param level
	 * @param firstTime
	 * @param iter
	 * @return false if the first descending level was found and there are further levels, otherwise false
	 */
	protected boolean adaptIntegerLevelUILowValue(final boolean ascending, final DataContainerPropertyLevel level,
			final boolean firstTime, final Iterator iter)
	{
		if (!(level.getLowValue() instanceof Integer))
		{
			return true;
		}

		boolean first = firstTime;

		Integer low = (Integer) level.getLowValue();
		final int intLow = low.intValue();

		// last ascending found
		if (ascending && !iter.hasNext() && intLow > Integer.MIN_VALUE)
		{
			low = Integer.valueOf(intLow - 1);
		}
		// first descending found
		if (!ascending && iter.hasNext() && firstTime && intLow > Integer.MIN_VALUE)
		{
			first = false;
			low = Integer.valueOf(intLow - 1);
		}
		level.setAdaptedUILowValue(low);
		return first;
	}

	@Override
	public String getLocalizedContainerName()
	{
		return messageSource.getMessage("instorecs.customer360.statistical", null, this.i18nService.getCurrentLocale());
	}

	@Override
	public void setDataInErrorState()
	{
		final Set<Entry<String, DataContainer>> entrySet = this.dataContainerMap.entrySet();
		DataContainer container;
		for (final Entry<String, DataContainer> entry : entrySet)
		{
			container = entry.getValue();
			if (container != null)
			{
				container.setDataInErrorState();
			}
		}
		handlePropertiesInErrorState();
	}

	@Override
	public String getContainerName()
	{
		return StatisticalDataContainer.class.getName();
	}

	@Override
	public void encodeHTML()
	{
		salesVolumeProperty.encodeHTML();
		averageSalesVolumeProperty.encodeHTML();
		lastPurchaseDateProperty.encodeHTML();
		storePurchaseRatioProperty.encodeHTML();
	}

	@Override
	public void combineData(final Map<String, DataContainer> dataContainerMap) throws DataContainerServiceException
	{
		if (dataContainerMap == null || dataContainerMap.size() != dataContainerNames.size())
		{
			throw new DataContainerServiceException("Not all required Statistical Context containers available");
		}

		this.dataContainerMap = dataContainerMap;

		final Set<Entry<String, DataContainer>> entrySet = this.dataContainerMap.entrySet();
		DataContainer container;
		for (final Entry<String, DataContainer> entry : entrySet)
		{
			container = entry.getValue();
			if (container == null)
			{
				throw new DataContainerServiceException("Not all required Statistical Context containers available");
			}
		}

		checkContainerCurrencyValidity();
		createProperties();
	}

	/**
	 * This method checks, if the ISO currency of the container data is the same as the one of the session currency. If
	 * not, an Exception is thrown.
	 *
	 * @throws DataContainerServiceException
	 */
	protected void checkContainerCurrencyValidity() throws DataContainerServiceException
	{
		final String sessionCurrencyISOCode = statisticalDataSalesVolumeUtil.getCurrentCurrency().getIsocode();

		if (!this.getDataContainerOverallVolume().getSalesVolumeCurrencyISOCode().equals(sessionCurrencyISOCode)
				|| !this.getDataContainerSixMonthVolume().getSalesVolumeCurrencyISOCode().equals(sessionCurrencyISOCode))
		{
			this.setDataInErrorState();
			throw new DataContainerServiceException("CombineData failed, not all container currencies match the session currency");
		}
	}

	/**
	 * Returns a overall sales volume price based on the summed up sales volume data.
	 *
	 * @return PriceData the summed up overall sales volume price data.
	 */
	public PriceData getOverallSalesVolumePrice()
	{
		return getPrice(this.getDataContainerOverallVolume().getSalesVolume());
	}

	/**
	 * Returns a overall sales volume property based on the summed up sales volume data.
	 *
	 * @return property for the summed up overall sales volume attribute.
	 */
	public DataContainerProperty getSalesVolumeProperty()
	{
		return this.salesVolumeProperty;
	}

	/**
	 * Returns the average number of items per sales transaction.
	 *
	 * @return int the average number of items per sales transaction.
	 */
	public long getAverageNoOfItems()
	{
		if (this.getDataContainerOverallCount().getNumberOfTransactions() == 0)
		{
			return 0;
		}

		return Math.round((double) this.getDataContainerOverallItemsCount().getNumberOfTransactions()
				/ this.getDataContainerOverallCount().getNumberOfTransactions());
	}

	/**
	 * Returns the store purchase ratio as a formatted String.
	 *
	 * @return String the store purchase ratio formatted as percent value
	 */
	public String getStorePurchaseRatio()
	{
		return this.getDataContainerOverallVolume().getStorePurchaseRatio().intValue() + "%";
	}

	/**
	 * Returns the store purchase ratio property.
	 *
	 * @return the store purchase ratio property
	 */
	public DataContainerProperty getStorePurchaseRatioProperty()
	{
		return storePurchaseRatioProperty;
	}

	/**
	 * Returns an average overall sales order volume price, based on all sales volume data. This is the summed up sales
	 * volume divided by the summed up number of orders.
	 *
	 * @return PriceData the average overall sales order volume price data.
	 */
	public PriceData getAverageOverallSalesVolumePrice()
	{
		return calculateAverageSalesVolume(this.getDataContainerOverallVolume().getSalesVolume(), this
				.getDataContainerOverallCount().getNumberOfTransactions());
	}

	/**
	 * Returns a overall sales volume property based on the summed up sales volume data.
	 *
	 * @return property for the summed up overall sales volume attribute.
	 */
	public DataContainerProperty getAverageSalesVolumeProperty()
	{
		return this.averageSalesVolumeProperty;
	}

	/**
	 * Calculates the average sales volume per order based on the given sales volume and number of orders.
	 *
	 * @param salesVolume
	 *           the sales volume of all orders
	 * @param numberOfOrders
	 *           the number of orders the sales volume is originating from
	 * @return a price data object having the average sales volume per order
	 */
	protected PriceData calculateAverageSalesVolume(final BigDecimal salesVolume, final int numberOfOrders)
	{
		BigDecimal decimalAverageSalesValue = BigDecimal.ZERO;

		if (salesVolume == null || (numberOfOrders == 0 && salesVolume != BigDecimal.ZERO))
		{
			return null;
		}

		if (numberOfOrders > 0)
		{
			decimalAverageSalesValue = salesVolume.divide(BigDecimal.valueOf(numberOfOrders), MathContext.DECIMAL32);
		}

		return getPrice(decimalAverageSalesValue);
	}

	/**
	 * Create a price object from the given salesValue.
	 *
	 * @param salesValue
	 * @return the price
	 */
	protected PriceData getPrice(final BigDecimal salesValue)
	{
		return priceDataFactory.create(PriceDataType.BUY, salesValue, statisticalDataSalesVolumeUtil.getCurrentCurrency());
	}

	/**
	 * Returns the sales volume price of the last six month based on the summed up sales volume data.
	 *
	 * @return PriceData the summed up sales volume price data of the last six month
	 */
	public PriceData getSalesVolumePriceLastSixMonths()
	{
		return getPrice(this.getDataContainerSixMonthVolume().getSalesVolume());
	}

	/**
	 * Returns the last purchase date from the online or the POS shop.
	 *
	 * @return Date that represents the last purchase date
	 */
	public Date getLastPurchaseDate()
	{
		final String lastPurchaseDateAsString = getDataContainerLastPurchase().getLastPurchaseDate();

		final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date lastPurchaseDate;
		try
		{
			lastPurchaseDate = dateFormat.parse(lastPurchaseDateAsString);
		}
		catch (final ParseException e)
		{
			return null;
		}
		return lastPurchaseDate;
	}

	/**
	 * Returns the last purchase date property
	 *
	 * @return the last purchase date property
	 */
	public DataContainerProperty getLastPurchaseDateProperty()
	{
		return lastPurchaseDateProperty;
	}

	/**
	 * Creates and initializes the property objects.
	 */
	protected void createProperties()
	{
		String unitSingular;
		String unitPlural;
		final String unitIsoCode;

		unitIsoCode = this.getStatisticalDataSalesVolumeUtil().getCurrentCurrency().getIsocode();

		// averageSalesVolume
		this.averageSalesVolumeProperty = (DataContainerProperty) (springUtil.getBean("dataContainerPropertyCurrencyBD", this
				.getAverageOverallSalesVolumePrice().getValue(), unitIsoCode, unitIsoCode));

		// salesVolume
		this.salesVolumeProperty = (DataContainerProperty) (springUtil.getBean(BEAN_NAME_DC_PROP_CURRENCY_BD, this
				.getDataContainerOverallVolume().getSalesVolume(), unitIsoCode, unitIsoCode));

		// lastPurchaseDat
		unitSingular = this.getMessage("instorecs.customer360.tilepopup.unit.day", null);
		unitPlural = this.getMessage("instorecs.customer360.tilepopup.unit.days", null);
		this.lastPurchaseDateProperty = (DataContainerProperty) (springUtil.getBean(BEAN_NAME_DC_PROP_INTEGER,
				getLastPurchaseDateDistance(), unitSingular, unitPlural));

		// storePurchaseRatio
		unitSingular = this.getMessage("instorecs.customer360.tilepopup.unit.percent", null);
		unitPlural = unitSingular;
		final Integer storePurchaseRatio = Integer.valueOf(this.getDataContainerOverallVolume().getStorePurchaseRatio().intValue());
		this.storePurchaseRatioProperty = (DataContainerProperty) (springUtil.getBean(BEAN_NAME_DC_PROP_INTEGER,
				storePurchaseRatio, unitSingular, unitPlural));
	}

	/**
	 * Handles the property objects in error state.
	 */
	protected void handlePropertiesInErrorState()
	{
		// averageSalesVolume
		this.averageSalesVolumeProperty = (DataContainerProperty) (springUtil.getBean(BEAN_NAME_DC_PROP_CURRENCY_BD, null, "", ""));

		// salesVolume
		this.salesVolumeProperty = (DataContainerProperty) (springUtil.getBean(BEAN_NAME_DC_PROP_CURRENCY_BD, null, "", ""));

		// lastPurchaseDat
		this.lastPurchaseDateProperty = (DataContainerProperty) (springUtil.getBean(BEAN_NAME_DC_PROP_INTEGER, null, "", ""));

		// storePurchaseRatio
		this.storePurchaseRatioProperty = (DataContainerProperty) (springUtil.getBean(BEAN_NAME_DC_PROP_INTEGER, null, "", ""));
	}

	/**
	 * Gets the data container for the last purchase
	 *
	 * @return the CARStatisticalDataContainerLastPurchase
	 */
	private CARStatisticalDataContainerLastPurchase getDataContainerLastPurchase()
	{
		return (CARStatisticalDataContainerLastPurchase) this.dataContainerMap.get("CARStatisticalDataContainerLastPurchase");
	}

	/**
	 * Gets the data container for the overall volume
	 *
	 * @return the CARStatisticalDataContainerOverallVolume
	 */
	private CARStatisticalDataContainerOverallVolume getDataContainerOverallVolume()
	{
		return (CARStatisticalDataContainerOverallVolume) this.dataContainerMap.get("CARStatisticalDataContainerOverallVolume");
	}

	/**
	 * Gets the data container for the overall count
	 *
	 * @return the CARStatisticalDataContainerOverallCount
	 */
	private CARStatisticalDataContainerOverallCount getDataContainerOverallCount()
	{
		return (CARStatisticalDataContainerOverallCount) this.dataContainerMap.get("CARStatisticalDataContainerOverallCount");
	}

	/**
	 * Gets the data container for the six month volume
	 *
	 * @return the CARStatisticalDataContainerSixMonthVolume
	 */
	private CARStatisticalDataContainerSixMonthVolume getDataContainerSixMonthVolume()
	{
		return (CARStatisticalDataContainerSixMonthVolume) this.dataContainerMap.get("CARStatisticalDataContainerSixMonthVolume");
	}

	/**
	 * Gets the data container for the overall items count
	 *
	 * @return the CARStatisticalDataContainerOverallItemsCount
	 */
	private CARStatisticalDataContainerOverallItemsCount getDataContainerOverallItemsCount()
	{
		return (CARStatisticalDataContainerOverallItemsCount) this.dataContainerMap
				.get("CARStatisticalDataContainerOverallItemsCount");
	}

	@Override
	public void traceInformation()
	{
		if (log.isDebugEnabled())
		{
			log.debug("getContainerName()=" + this.getContainerName());
			final StringBuilder combinedContainers = new StringBuilder();
			final List<String> containers = getDataContainerNamesForCombining();
			String separator = "";
			for (final String container : containers)
			{
				combinedContainers.append(separator).append(container);
				separator = ", ";
			}
			log.debug("getDataContainerNamesForCombining()=" + combinedContainers.toString());
		}
	}

}
