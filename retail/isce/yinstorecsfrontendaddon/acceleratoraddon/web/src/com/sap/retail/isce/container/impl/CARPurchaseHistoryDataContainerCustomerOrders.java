/*****************************************************************************
 Class:        CARPurchaseHistoryDataContainerCustomerOrders
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.c2l.CurrencyModel;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
 * Class for retrieving the purchase history from the customer orders and POS data.
 *
 */
public class CARPurchaseHistoryDataContainerCustomerOrders extends CARStatisticalDataContainerBase
{
	protected static final int MAX_ENTRIES_DISPLAYED_PER_PAGE = 5;

	protected static final String SELECTED_FIELDS = "OrderChannel,CreationDate,CreationTime,TransactionNumber,TransactionCurrency,TotalNetAmount,TaxAmount";
	protected static final String ORDERBY_FIELDS = "CreationDate";
	protected static final String ORDERBY_DIRECTION = "desc";
	protected static final String TAX_AMOUNT_PROPERTY = "TaxAmount";
	protected static final String TOTAL_NET_AMOUNT_PROPERTY = "TotalNetAmount";
	protected static final String CREATION_DATE_PROPERTY = "CreationDate";
	protected static final String CREATION_TIME_PROPERTY = "CreationTime";
	protected static final String TRANSACTION_NUMBER_PROPERTY = "TransactionNumber";
	protected static final String ORDER_CHANNEL_PROPERTY = "OrderChannel";
	protected static final String TRANSACTION_CURRENCY_PROPERTY = "TransactionCurrency";

	protected static Logger log = Logger.getLogger(CARPurchaseHistoryDataContainerCustomerOrders.class.getName());

	protected PriceDataFactory priceDataFactory;
	protected StatisticalDataSalesVolumeUtil statisticalDataSalesVolumeUtil;
	protected SearchPageData<CARPurchaseHistoryCustomerOrders> purchaseHistoryData;
	protected int maxEntriesDisplayedPerPage;
	protected int pageNumberDisplayed;

	/**
	 * Default constructor
	 *
	 * @param isceConfigurationService
	 */
	public CARPurchaseHistoryDataContainerCustomerOrders(final ISCEConfigurationService isceConfigurationService)
	{
		super(isceConfigurationService);
	}

	@Override
	public String getSelect()
	{
		return SELECTED_FIELDS;
	}

	@Override
	public String getOrderBy()
	{
		return new StringBuilder().append(ORDERBY_FIELDS).append(" ").append(ORDERBY_DIRECTION).toString();
	}

	@Override
	public Integer getTop()
	{
		Integer carMaxNumberTransactionsPurchaseHistory = getISCEConfigurationService()
				.getCARMaxNumberTransactionsPurchaseHistory();

		// In case the value has been removed from the BackOffice and saved without value - fall back
		if (carMaxNumberTransactionsPurchaseHistory == null)
		{
			carMaxNumberTransactionsPurchaseHistory = Integer.valueOf(100);
		}
		return carMaxNumberTransactionsPurchaseHistory;
	}


	@Override
	public String getContainerName()
	{
		return CARPurchaseHistoryDataContainerCustomerOrders.class.getName();
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

		final List<CARPurchaseHistoryCustomerOrders> purchaseHistoryDataList = buildPurchaseHistoryDataList(oDataEntries);
		this.purchaseHistoryData.setResults(purchaseHistoryDataList);
		this.setMaxEntriesDisplayedPerPage(MAX_ENTRIES_DISPLAYED_PER_PAGE);
	}

	/**
	 * Constructs a list of CARPurchaseHistoryCustomerOrders object required from the User Interface. Each object
	 * corresponds to a line in the table UI, based on the oDataEntries retrieved from the oData call.
	 *
	 * @param oDataEntries
	 *           List of data retrieved from the oData call
	 * @return List<CARPurchaseHistoryCustomerOrders> a list of CARPurchaseHistoryCustomerOrders object required from the
	 *         User Interface. Each object corresponds to a table line in the UI
	 */
	protected List<CARPurchaseHistoryCustomerOrders> buildPurchaseHistoryDataList(final List<ODataEntry> oDataEntries)
	{
		final List<CARPurchaseHistoryCustomerOrders> purchaseHistoryDataList = new ArrayList<>();
		CARPurchaseHistoryCustomerOrders purchaseHistory;

		final List<String> carPosOrderChannelsList = getPosOrderChannels();
		final List<String> carOnlineOrderChannelsList = getOnlineOrderChannels();
		final String localizedOnlineOrderNamePurchaseHistory = getLocalizedOnlineOrderNamePurchaseHistory();
		final String localizedStorePurchaseNamePurchaseHistory = getLocalizedStorePurchaseNamePurchaseHistory();
		final List iscePurchaseHistoryResultList = new ArrayList<ISCESalesVolumeResult>();
		ISCESalesVolumeResult.Channel isceResultChannel;

		String creationDate;
		String creationTime;
		Date purchaseDate;
		BigDecimal taxAmount;
		BigDecimal totalNetAmount;
		String transactionCurrency;
		PriceData grossSalesVolumePrice;
		String transactionNumber;
		String orderChannel;
		ISCESalesVolumeResult salesVolume;
		final CurrencyModel shopCurrencyModel = this.statisticalDataSalesVolumeUtil.getCurrentCurrency();

		for (final ODataEntry oDataEntry : oDataEntries)
		{
			final Map<String, Object> properties = oDataEntry.getProperties();

			if (checkProperties(properties))
			{
				log.error("oDataEntry has not all required Properties assigned");
				throw new DataContainerRuntimeException("oDataEntry has not all required Properties assigned");
			}
			isceResultChannel = ISCESalesVolumeResult.Channel.ANY;
			creationDate = (String) properties.get(CREATION_DATE_PROPERTY);
			creationTime = (String) properties.get(CREATION_TIME_PROPERTY);
			purchaseDate = getPurchaseDate(new StringBuilder().append(creationDate).append(creationTime).toString());

			taxAmount = (BigDecimal) properties.get(TAX_AMOUNT_PROPERTY);
			totalNetAmount = (BigDecimal) properties.get(TOTAL_NET_AMOUNT_PROPERTY);
			transactionCurrency = (String) properties.get(TRANSACTION_CURRENCY_PROPERTY);
			transactionNumber = (String) properties.get(TRANSACTION_NUMBER_PROPERTY);
			orderChannel = (String) properties.get(ORDER_CHANNEL_PROPERTY);
			if (carOnlineOrderChannelsList.contains(orderChannel))
			{
				orderChannel = localizedOnlineOrderNamePurchaseHistory;
				isceResultChannel = ISCESalesVolumeResult.Channel.ONLINE;
			}
			else if (carPosOrderChannelsList.contains(orderChannel))
			{
				orderChannel = localizedStorePurchaseNamePurchaseHistory;
				isceResultChannel = ISCESalesVolumeResult.Channel.POS;
				transactionNumber = ""; // will remove the order navigation link preparation from the UI
			}
			iscePurchaseHistoryResultList.clear();
			iscePurchaseHistoryResultList.add(new ISCESalesVolumeResult(taxAmount.add(totalNetAmount), 0, transactionCurrency,
					isceResultChannel));

			salesVolume = this.statisticalDataSalesVolumeUtil.calculateSalesVolumeForTargetCurrency(shopCurrencyModel,
					iscePurchaseHistoryResultList, this, isceResultChannel);
			grossSalesVolumePrice = getGrossSalesVolumePrice(salesVolume.getSalesVolume(), shopCurrencyModel);

			purchaseHistory = new CARPurchaseHistoryCustomerOrders(purchaseDate, grossSalesVolumePrice, orderChannel,
					transactionNumber);
			purchaseHistoryDataList.add(purchaseHistory);
		}
		return purchaseHistoryDataList;
	}

	/**
	 * Checks the properties against null.
	 *
	 * @param properties
	 *           Map containing the different properties from the oData call.
	 * @return boolean true is every mandatory properties are not null, false otherwise
	 */
	protected boolean checkProperties(final Map<String, Object> properties)
	{
		if (properties == null)
		{
			return true;
		}
		if (checkCreationProperties(properties) || checkAmountProperties(properties) || checkTransactionProperties(properties))
		{
			return true;
		}
		return false;
	}

	/**
	 * Checks some specific properties against null. Several methods to reduce cyclomatic complexity.
	 *
	 * @param properties
	 *           Map containing the different properties from the oData call.
	 * @return boolean true is every mandatory properties are not null, false otherwise
	 */
	protected boolean checkCreationProperties(final Map<String, Object> properties)
	{
		if (properties.get(CREATION_DATE_PROPERTY) == null || properties.get(CREATION_TIME_PROPERTY) == null)
		{
			return true;
		}
		return false;
	}

	/**
	 * Checks some specific properties against null. Several methods to reduce cyclomatic complexity.
	 *
	 * @param properties
	 *           Map containing the different properties from the oData call.
	 * @return boolean true is every mandatory properties are not null, false otherwise
	 */
	protected boolean checkAmountProperties(final Map<String, Object> properties)
	{
		if (properties.get(TAX_AMOUNT_PROPERTY) == null || properties.get(TOTAL_NET_AMOUNT_PROPERTY) == null)
		{
			return true;
		}
		return false;
	}

	/**
	 * Checks some specific properties against null. Several methods to reduce cyclomatic complexity.
	 *
	 * @param properties
	 *           Map containing the different properties from the oData call.
	 * @return boolean true is every mandatory properties are not null, false otherwise
	 */
	protected boolean checkTransactionProperties(final Map<String, Object> properties)
	{
		if (properties.get(TRANSACTION_NUMBER_PROPERTY) == null || properties.get(TRANSACTION_CURRENCY_PROPERTY) == null
				|| properties.get(ORDER_CHANNEL_PROPERTY) == null)
		{
			return true;
		}
		return false;
	}

	/**
	 * Returns the gross sales volume price based on the summed up tax and net amounts.
	 *
	 * @return PriceData the gross sales volume price based on the summed up tax and net amounts
	 */
	public PriceData getGrossSalesVolumePrice(final BigDecimal amount, final CurrencyModel shopCurrencyModel)
	{
		return priceDataFactory.create(PriceDataType.BUY, amount, shopCurrencyModel);
	}

	/**
	 * Returns the purchase date from the online or the POS transaction in yyyyMMddHHmmss format.
	 *
	 * @return Date the purchase date from the online or the POS transaction, or null in parse exception.
	 */
	public Date getPurchaseDate(final String creationDate)
	{
		final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date purchaseDate;
		try
		{
			purchaseDate = dateFormat.parse(creationDate);
		}
		catch (final ParseException e)
		{
			return null;
		}
		return purchaseDate;
	}

	@Override
	public void setDataInErrorState()
	{
		this.purchaseHistoryData = new SearchPageData<>();
		this.purchaseHistoryData.setResults(Collections.<CARPurchaseHistoryCustomerOrders> emptyList());
		this.maxEntriesDisplayedPerPage = 0;
	}

	@Override
	public void encodeHTML()
	{
		final List<CARPurchaseHistoryCustomerOrders> purchaseHistoryDataResults = this.purchaseHistoryData.getResults();

		if (purchaseHistoryDataResults == null)
		{
			return;
		}

		for (int i = 0; i < purchaseHistoryDataResults.size(); i++)
		{
			final CARPurchaseHistoryCustomerOrders carPurchaseHistoryCustomerOrders = purchaseHistoryDataResults.get(i);

			carPurchaseHistoryCustomerOrders.setOrderNumber(this.encodeHTML(carPurchaseHistoryCustomerOrders.getOrderNumber()));
			carPurchaseHistoryCustomerOrders.setPurchaseType(this.encodeHTML(carPurchaseHistoryCustomerOrders.getPurchaseType()));
			final PriceData priceData = carPurchaseHistoryCustomerOrders.getGrossSalesVolumePrice();
			priceData.setFormattedValue(priceData.getFormattedValue());
			priceData.setCurrencyIso(priceData.getCurrencyIso());
			carPurchaseHistoryCustomerOrders.setGrossSalesVolumePrice(priceData);
		}
	}

	@Override
	public String getContainerContextParamName()
	{
		return "CARPurchaseHistoryDataContainerCustomerOrders";
	}

	@Override
	public String getLocalizedContainerName()
	{
		return this.messageSource.getMessage("instorecs.customer360.purchaseHistory", null, this.i18nService.getCurrentLocale());
	}

	/**
	 * Retrieves the localized table line value for Online Order.
	 *
	 * @return String localized Online Order
	 */
	protected String getLocalizedOnlineOrderNamePurchaseHistory()
	{
		return this.messageSource.getMessage("instorecs.customer360.purchaseHistory.onlineOrder", null,
				this.i18nService.getCurrentLocale());
	}

	/**
	 * Retrieves the localized table line value for store purchase.
	 *
	 * @return String localized store purchase
	 */
	protected String getLocalizedStorePurchaseNamePurchaseHistory()
	{
		return this.messageSource.getMessage("instorecs.customer360.purchaseHistory.storePurchase", null,
				this.i18nService.getCurrentLocale());
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
	 * @return the purchaseHistoryData
	 */
	public SearchPageData<CARPurchaseHistoryCustomerOrders> getPurchaseHistoryData()
	{
		return purchaseHistoryData;
	}

	/**
	 * @param purchaseHistoryData
	 *           the purchaseHistoryData to set
	 */
	protected void setPurchaseHistoryData(final SearchPageData<CARPurchaseHistoryCustomerOrders> purchaseHistoryData)
	{
		this.purchaseHistoryData = purchaseHistoryData;
	}

	/**
	 * @return the maxEntriesDisplayedPerPage
	 */
	public int getMaxEntriesDisplayedPerPage()
	{
		return maxEntriesDisplayedPerPage;
	}

	/**
	 * @param maxEntriesDisplayedPerPage
	 *           the maxEntriesDisplayedPerPage to set
	 */
	public void setMaxEntriesDisplayedPerPage(final int maxEntriesDisplayedPerPage)
	{
		this.maxEntriesDisplayedPerPage = maxEntriesDisplayedPerPage;
	}

	/**
	 * @return the pageNumberDisplayed
	 */
	public int getPageNumberDisplayed()
	{
		return pageNumberDisplayed;
	}

	/**
	 * @param pageNumberDisplayed
	 *           the pageNumberDisplayed to set
	 */
	public void setPageNumberDisplayed(final int pageNumberDisplayed)
	{
		this.pageNumberDisplayed = pageNumberDisplayed;
	}

	@Override
	public void traceInformation()
	{
		super.traceInformation(log);
	}
}
