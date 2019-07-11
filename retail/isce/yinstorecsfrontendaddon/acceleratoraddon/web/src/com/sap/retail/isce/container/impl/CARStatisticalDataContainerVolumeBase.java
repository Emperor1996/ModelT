/*****************************************************************************
 Class:        CARStatisticalDataContainerVolumeBase
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.springframework.beans.factory.annotation.Required;

import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.retail.isce.service.sap.ISCEConfigurationService;
import com.sap.retail.isce.service.sap.odata.HttpODataResult;
import com.sap.retail.isce.service.sap.result.ISCESalesVolumeResult;
import com.sap.retail.isce.service.util.StatisticalDataSalesVolumeUtil;


/**
 * Abstract Base class for all implementations of oData based containers providing volume based statistical data from
 * the car system.
 */
public abstract class CARStatisticalDataContainerVolumeBase extends CARStatisticalDataContainerBase
{

	public static final String TRANSACTION_CURRENCY_PROPERTY = "TransactionCurrency";
	public static final String TOTAL_NET_AMOUNT_PROPERTY = "TotalNetAmount";
	public static final String TAX_AMOUNT_PROPERTY = "TaxAmount";
	public static final String ORDER_CHANNEL_PROPERTY = "OrderChannel";

	protected static final String SELECTED_FIELDS = "TotalNetAmount,TransactionCurrency,OrderChannel,TaxAmount";

	protected static Logger log = Logger.getLogger(CARStatisticalDataContainerVolumeBase.class.getName());

	private StatisticalDataSalesVolumeUtil statisticalDataSalesVolumeUtil;

	private ISCESalesVolumeResult salesVolume = null;
	private Double storePurchaseRatio = new Double(0);

	/**
	 * Default constructor.
	 *
	 * @param isceConfigurationService
	 *           the ISCE configuration to use
	 */
	public CARStatisticalDataContainerVolumeBase(final ISCEConfigurationService isceConfigurationService)
	{
		super(isceConfigurationService);
		this.select = SELECTED_FIELDS;
	}

	@Override
	public void setDataInErrorState()
	{
		final String sessionCurrencyISOCode = this.statisticalDataSalesVolumeUtil.getCurrentCurrency().getIsocode();
		this.salesVolume = new ISCESalesVolumeResult(BigDecimal.ZERO, 0, sessionCurrencyISOCode);
		this.storePurchaseRatio = new Double(0);
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
	 * @return the storePurchaseRatio
	 */
	public Double getStorePurchaseRatio()
	{
		return storePurchaseRatio;
	}

	/**
	 * Returns the sales volume.
	 *
	 * @return the sales volume.
	 */
	public BigDecimal getSalesVolume()
	{
		return salesVolume.getSalesVolume();
	}

	/**
	 * Returns the currency ISO code of the sales volume.
	 *
	 * @return the currency ISO code of the sales volume
	 */
	public String getSalesVolumeCurrencyISOCode()
	{
		return salesVolume.getCurrencyISOCode();
	}

	/**
	 * Sets the online sales volume result object.
	 *
	 * @param salesVolume
	 *           the online sales volume to set
	 */
	public void setSalesVolumeResult(final ISCESalesVolumeResult salesVolume)
	{
		this.salesVolume = salesVolume;
	}


	/**
	 * Checks if the given properties are maintained, if TRANSACTION_CURRENCY_PROPERTY, TOTAL_NET_AMOUNT_PROPERTY,
	 * TAX_AMOUNT_PROPERTY and ORDER_CHANNEL_PROPERTY are well maintained.
	 *
	 * @param Map
	 *           <String, Object> oDataEntryProperties : a map with all the properties
	 * @return boolean : true if all required properties are properly maintained, false otherwise
	 */
	private boolean isDataEntryPropertiesMaintained(Map<String, Object> oDataEntryProperties)
	{
		if (oDataEntryProperties == null)
		{
			return false;
		}
		if (oDataEntryProperties.get(TRANSACTION_CURRENCY_PROPERTY) == null
				|| oDataEntryProperties.get(TOTAL_NET_AMOUNT_PROPERTY) == null
				|| oDataEntryProperties.get(TAX_AMOUNT_PROPERTY) == null || oDataEntryProperties.get(ORDER_CHANNEL_PROPERTY) == null)
		{
			return false;
		}
		return true;
	}

	/**
	 * Builds a list of ISCESalesVolumeResult objects based on the given search result.
	 *
	 * @param oDataEntries
	 *           the OData result list, to build a ISCESalesVolumeResult list from
	 *
	 * @return List<ISCESalesVolumeResult> the list of ISCESalesVolumeResult objects
	 */
	protected List<ISCESalesVolumeResult> buildSalesVolumeResultList(final List<ODataEntry> oDataEntries)
	{
		final List isceSalesVolumeResultList = new ArrayList<ISCESalesVolumeResult>();
		BigDecimal resultSalesVolume;
		String resultISOCurrency;
		String resultOrderChannel;
		ISCESalesVolumeResult.Channel isceResultChannel;

		final List<String> posOrderChannels = getPosOrderChannels();
		final List<String> onlineOrderChannels = getOnlineOrderChannels();

		for (final ODataEntry oDataEntry : oDataEntries)
		{
			final Map<String, Object> oDataEntryProperties = oDataEntry.getProperties();
			if (!this.isDataEntryPropertiesMaintained(oDataEntryProperties))
			{
				log.error("oDataEntry has not all required Properties assigned");
				throw new DataContainerRuntimeException("oDataEntry has not all required Properties assigned");
			}

			isceResultChannel = ISCESalesVolumeResult.Channel.ANY;

			resultSalesVolume = ((BigDecimal) oDataEntryProperties.get(TOTAL_NET_AMOUNT_PROPERTY))
					.add((BigDecimal) oDataEntryProperties.get(TAX_AMOUNT_PROPERTY));
			resultISOCurrency = (String) oDataEntryProperties.get(TRANSACTION_CURRENCY_PROPERTY);
			resultOrderChannel = (String) oDataEntryProperties.get(ORDER_CHANNEL_PROPERTY);

			if (onlineOrderChannels.contains(resultOrderChannel))
			{
				isceResultChannel = ISCESalesVolumeResult.Channel.ONLINE;
			}
			else if (posOrderChannels.contains(resultOrderChannel))
			{
				isceResultChannel = ISCESalesVolumeResult.Channel.POS;
			}

			isceSalesVolumeResultList.add(new ISCESalesVolumeResult(resultSalesVolume, 0, resultISOCurrency, isceResultChannel));
		}
		return isceSalesVolumeResultList;
	}

	@Override
	public void extractOwnDataFromResult(final HttpODataResult httpODataResult)
	{
		this.setDataInErrorState();

		if (httpODataResult == null)
		{
			return;
		}
		final List<ODataEntry> oDataEntries = httpODataResult.getEntities();

		final List<ISCESalesVolumeResult> isceSalesVolumeResultList = buildSalesVolumeResultList(oDataEntries);

		final ISCESalesVolumeResult onlineSalesVolume = this.statisticalDataSalesVolumeUtil.calculateSalesVolumeForTargetCurrency(
				this.statisticalDataSalesVolumeUtil.getCurrentCurrency(), isceSalesVolumeResultList, this,
				ISCESalesVolumeResult.Channel.ONLINE);

		final ISCESalesVolumeResult posSalesVolumePOS = this.statisticalDataSalesVolumeUtil.calculateSalesVolumeForTargetCurrency(
				this.statisticalDataSalesVolumeUtil.getCurrentCurrency(), isceSalesVolumeResultList, this,
				ISCESalesVolumeResult.Channel.POS);

		this.salesVolume.setSalesVolume(onlineSalesVolume.getSalesVolume().add(posSalesVolumePOS.getSalesVolume()));
		if (!this.salesVolume.getSalesVolume().equals(BigDecimal.ZERO))
		{
			this.storePurchaseRatio = new Double(posSalesVolumePOS.getSalesVolume().doubleValue()
					/ this.salesVolume.getSalesVolume().doubleValue() * 100.0);
		}
	}

	@Override
	public void traceInformation()
	{
		super.traceAllInformation(log, null);
	}

}
