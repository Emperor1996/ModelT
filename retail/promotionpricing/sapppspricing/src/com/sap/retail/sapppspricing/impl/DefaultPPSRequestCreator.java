/*****************************************************************************
Class: DefaultPPSRequestCreator

@Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 
 *****************************************************************************/

package com.sap.retail.sapppspricing.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.Ordered;

import com.sap.ppengine.client.dto.ItemDomainSpecific;
import com.sap.ppengine.client.dto.LineItemDomainSpecific;
import com.sap.ppengine.client.dto.PriceCalculate;
import com.sap.ppengine.client.dto.PriceCalculateBase;
import com.sap.ppengine.client.dto.RetailTransactionItemTypeEnumeration;
import com.sap.ppengine.client.dto.SaleBase;
import com.sap.ppengine.client.dto.TransactionTypeEnumeration;
import com.sap.ppengine.client.util.PPSClientBeanAccessor;
import com.sap.retail.sapppspricing.LineItemPopulator;
import com.sap.retail.sapppspricing.PPSConfigService;
import com.sap.retail.sapppspricing.PPSRequestCreator;
import com.sap.retail.sapppspricing.enums.InterfaceVersion;

/**
 * Helper class for creating price calculation requests for usage from catalog
 * or cart. Supports injection of an arbitrary number of line item populators.
 * Set requested language based on current language provided by
 * {@link CommonI18NService}
 */
public class DefaultPPSRequestCreator implements PPSRequestCreator {
	private static final Logger LOG = LoggerFactory
			.getLogger(DefaultPPSRequestCreator.class);
	private PPSClientBeanAccessor accessor;
	private List<LineItemPopulator<ProductModel>> lineItemPopulators;
	private PPSConfigService configService;
	private CommonI18NService commonI18NService;
	private boolean discountableFlag;

	private final PPSAccessorHelper helper = new PPSAccessorHelper();

	protected PriceCalculate createRequest(final String businessUnitId,
			final String transactionId, final boolean isNet) {
		final PriceCalculate priceCalculate = getAccessor().getHelper()
				.createCalculateRequestSkeleton(businessUnitId,
						new GregorianCalendar());

		final String languageCode = getCommonI18NService().getCurrentLanguage()
				.getIsocode();
		priceCalculate.getARTSHeader().setRequestedLanguage(languageCode);
		final PriceCalculateBase priceCalculateBase = priceCalculate
				.getPriceCalculateBody().get(0);
		priceCalculateBase
				.setTransactionType(TransactionTypeEnumeration.SALE_TRANSACTION
						.value());
		priceCalculateBase.setNetPriceFlag(isNet);
		priceCalculateBase.getTransactionID().setValue(transactionId);

		return priceCalculate;
	}

	@Override
	public PriceCalculate createRequestForCatalog(
			final ProductModel productModel, final boolean isNet) {
		final String businessUnit = getConfigService().getBusinessUnitId(
				productModel);
		final PriceCalculate priceCalculate = createRequest(businessUnit,
				"yproduct_" + productModel.getCode() + "@"
						+ productModel.getCatalogVersion().getVersion(), isNet);
		InterfaceVersion clientInterfaceVersion = getConfigService()
				.getClientInterfaceVersion(productModel);
		setVersionForRequest(clientInterfaceVersion, priceCalculate);

		final List<LineItemDomainSpecific> lineItems = priceCalculate
				.getPriceCalculateBody().get(0).getShoppingBasket()
				.getLineItem();
		final String uomCode = productModel.getUnit().getCode();
		lineItems.add(createLineItem(0, productModel, uomCode,
				BigDecimal.valueOf(1L)));

		return priceCalculate;
	}

	protected void setVersionForRequest(InterfaceVersion version,
			final PriceCalculate priceCalculate) {
		if (version == null) {
			LOG.warn("No client interface version could be determined, using default version 1.0");
			return;
		}
		final String[] splitResult = version.getCode().split("VERSION");
		final int versionNumber = Integer.parseInt(splitResult[1]);

		final BigInteger majorVersion = BigInteger.valueOf(((long)versionNumber) / 10);
		final BigInteger minorVersion = BigInteger.valueOf(versionNumber % 10);

		priceCalculate.setInternalMajorVersion(majorVersion);
		priceCalculate.setInternalMinorVersion(minorVersion);
		LOG.debug("Interface major / minor version {} / {} is used",
				majorVersion, minorVersion);
	}

	@Override
	public PriceCalculate createRequestForCart(final AbstractOrderModel order) {
		final PriceCalculate priceCalculate = createRequest(getConfigService()
				.getBusinessUnitId(order), "yorder_" + order.getCode(),
				order.getNet());
		fillRequestBodyForCart(order, priceCalculate);
		InterfaceVersion clientInterfaceVersion = getConfigService()
				.getClientInterfaceVersion(order);
		setVersionForRequest(clientInterfaceVersion, priceCalculate);

		return priceCalculate;
	}

	protected void fillRequestBodyForCart(final AbstractOrderModel order,
			final PriceCalculate priceCalculate) {
		int saleItemIndex = 0;
		int entryIndex = 0;
		final List<LineItemDomainSpecific> lineItems = priceCalculate
				.getPriceCalculateBody().get(0).getShoppingBasket()
				.getLineItem();
		// Skip as many order entries as there are "real" line items
		for (int i = 0; i < order.getEntries().size(); i++) {
			final Pair<Integer, ItemDomainSpecific> next = helper
					.nextNonDiscountItem(lineItems, saleItemIndex);
			if (next == null) {
				break;
			}
			saleItemIndex = next.getLeft().intValue() + 1;
			entryIndex++;
		}
		// We assume that sequence numbers do not have gaps
		for (int i = entryIndex; i < order.getEntries().size(); i++) {
			final AbstractOrderEntryModel entry = order.getEntries().get(i);
			final String uomCode = entry.getUnit().getCode();
			final int sequenceNumber = saleItemIndex++;
			lineItems.add(createLineItem(sequenceNumber, entry.getProduct(),
					uomCode,
					BigDecimal.valueOf(entry.getQuantity().longValue())));
		}
	}

	protected LineItemDomainSpecific createLineItem(final int sequenceNumber,
			final ProductModel product, final String uom, final BigDecimal qty) {
		final LineItemDomainSpecific lineItem = getAccessor()
				.getHelper()
				.createSaleLineItem(sequenceNumber, product.getCode(), uom, qty);

		SaleBase saleBase = lineItem.getSale();
		saleBase.setItemType(RetailTransactionItemTypeEnumeration.STOCK.value());

		// consider discount flag per line item
		if (isDiscountableFlag()
				&& Boolean.FALSE.equals(product.getDiscountable())) {
			saleBase.setNonDiscountableFlag(Boolean.TRUE);
		}

		for (final LineItemPopulator<ProductModel> populator : getLineItemPopulators()) {
			populator.populate(lineItem, product);
		}
		return lineItem;
	}

	@SuppressWarnings("javadoc")
	public PPSClientBeanAccessor getAccessor() {
		return accessor;
	}

	@SuppressWarnings("javadoc")
	public void setAccessor(final PPSClientBeanAccessor accessor) {
		this.accessor = accessor;
	}

	@SuppressWarnings("javadoc")
	public List<LineItemPopulator<ProductModel>> getLineItemPopulators() {
		return lineItemPopulators;
	}

	@SuppressWarnings("javadoc")
	@Required
	public void setLineItemPopulators(
			final List<LineItemPopulator<ProductModel>> lineItemPopulators) {
		this.lineItemPopulators = lineItemPopulators;
		Collections.sort(this.lineItemPopulators, new OrderedComparator());
		if (LOG.isDebugEnabled()) {
			LOG.debug("List of line item populators after sorting: "
					+ this.lineItemPopulators);
		}
	}

	@SuppressWarnings("javadoc")
	public CommonI18NService getCommonI18NService() {
		return commonI18NService;
	}

	@SuppressWarnings("javadoc")
	public void setCommonI18NService(final CommonI18NService commonI18NService) {
		this.commonI18NService = commonI18NService;
	}

	@SuppressWarnings("javadoc")
	public PPSConfigService getConfigService() {
		return configService;
	}

	@SuppressWarnings("javadoc")
	public void setConfigService(final PPSConfigService configService) {
		this.configService = configService;
	}

	private static class OrderedComparator implements Comparator<Ordered> {
		@Override
		public int compare(final Ordered arg0, final Ordered arg1) {
			return Integer.valueOf(arg0.getOrder()).compareTo(
					Integer.valueOf(arg1.getOrder()));
		}
	}

	@SuppressWarnings("javadoc")
	public boolean isDiscountableFlag() {
		return discountableFlag;
	}

	@SuppressWarnings("javadoc")
	public void setDiscountableFlag(boolean discountableFlag) {
		this.discountableFlag = discountableFlag;
	}
}
