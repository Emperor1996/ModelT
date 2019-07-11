/*****************************************************************************
Class: DefaultPriceCalculateToOrderMapper
 
@Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 
*****************************************************************************/
 
/**
 *
 */
package com.sap.retail.sapppspricing.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.util.DiscountValue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.ppengine.client.dto.DiscountBase;
import com.sap.ppengine.client.dto.ItemDomainSpecific;
import com.sap.ppengine.client.dto.LineItemDomainSpecific;
import com.sap.ppengine.client.dto.PriceCalculateResponse;
import com.sap.ppengine.client.dto.PriceDerivationRuleBase;
import com.sap.ppengine.client.dto.RetailPriceModifierBase.Amount;
import com.sap.ppengine.client.dto.RetailPriceModifierDomainSpecific;
import com.sap.retail.sapppspricing.PriceCalculateToOrderMapper;
import com.sap.retail.sapppspricing.SapPPSPricingRuntimeException;


/**
 * Helper class mapping the result of a PPS call back to the hybris order
 */
public class DefaultPriceCalculateToOrderMapper extends DefaultPPSClientBeanAccessor implements PriceCalculateToOrderMapper
{
	private static final Logger LOG = LoggerFactory.getLogger(DefaultPriceCalculateToOrderMapper.class);
	private PPSAccessorHelper accessorHelper = new PPSAccessorHelper();

	@Override
	public int getOrder()
	{
		return 0;
	}

	@Override
	public void map(final PriceCalculateResponse response, final AbstractOrderModel order)
	{
		final List<LineItemDomainSpecific> lineItems = response.getPriceCalculateBody().get(0).getShoppingBasket().getLineItem();
		mapResponseToCartEntries(lineItems, order);
	}

	private Double basePriceOf(final ItemDomainSpecific sale)
	{
		return Double.valueOf(sale.getRegularSalesUnitPrice().getValue().doubleValue());
	}

	protected void mapResponseToCartEntries(final List<LineItemDomainSpecific> lineItems, final AbstractOrderModel order)
	{
		int saleItemIndex = 0;
		final String expectedCurrency = order.getCurrency().getIsocode();
		for (final AbstractOrderEntryModel entry : order.getEntries())
		{
			final Pair<Integer, ItemDomainSpecific> result = getAccessorHelper().nextNonDiscountItem(lineItems, saleItemIndex);
			if (result == null)
			{
				throw new SapPPSPricingRuntimeException("Missing sale items in calculation response");
			}
			final ItemDomainSpecific item = result.getRight();
			saleItemIndex = result.getLeft().intValue() + 1;
			if (item != null)
			{
				entry.setBasePrice(basePriceOf(item));
				final String currency = item.getRegularSalesUnitPrice().getCurrency();
				if (currency != null && !currency.equals(expectedCurrency))
				{
					LOG.error("Unexpected currency code {} for item {}", currency, item.getItemID().get(0).getValue());
				}
				LOG.debug("Base Price = {}", entry.getBasePrice());
				final List<DiscountValue> discounts = convertToEntryDiscounts(lineItems, item, order);
				entry.setDiscountValues(new ArrayList<DiscountValue>());
				if (!discounts.isEmpty())
				{
					entry.setDiscountValues(discounts);
				}
			}
		}
	}

	protected String codeForDiscount(final List<LineItemDomainSpecific> lineItems,
			final RetailPriceModifierDomainSpecific priceModifier, final String defaultPrefix)
	{
		String result;
		final List<PriceDerivationRuleBase> priceDerivationRule = priceModifier.getPriceDerivationRule();
		if (priceDerivationRule != null && !priceDerivationRule.isEmpty())
		{
			// Only take the first rule - as of now the service does not fill
			// several price rules per price modifier
			result = priceDerivationRule.get(0).getPromotionPriceDerivationRuleTypeCode();
		}
		else
		{
			// Header Discount has no price derivation rule
			result = getDiscountCodeFromDiscountLineItem(lineItems, priceModifier);
		}

		if (result == null || result.length() == 0)
		{
			result = defaultPrefix + "_" + priceModifier.getPromotionID();
		}
		return result;
	}

	private String getDiscountCodeFromDiscountLineItem(final List<LineItemDomainSpecific> lineItems,
			final RetailPriceModifierDomainSpecific priceModifier)
	{
		// distributed header discount refers with the ItemLink to the
		// Discount->PriceDerivationRule->SequenceNumber
		if (priceModifier.getItemLink() == null || priceModifier.getItemLink().size() <= 0)
		{
			return null;
		}

		final BigInteger machtingSeqNumber = priceModifier.getItemLink().get(0);

		for (final LineItemDomainSpecific lineItem : lineItems)
		{
			final DiscountBase discount = lineItem.getDiscount();
			if (discount != null && machtingSeqNumber.compareTo(discount.getSequenceNumber()) == 0)
			{
				return discount.getPriceDerivationRule().get(0).getPromotionPriceDerivationRuleTypeCode();
			}
		}
		return null;
	}

	/**
	 *
	 * This method determine the ERP conditionType out of the PriceDerivationRule->PromotionPriceDerivationRuleTypeCode.
	 * It do it for the 'real' item discounts as well as for the distributed header discounts
	 */
	protected String codeForItemDiscount(final List<LineItemDomainSpecific> lineItems,
			final RetailPriceModifierDomainSpecific priceModifier, final AbstractOrderModel order)
	{
		return codeForDiscount(lineItems, priceModifier, "EDISC" + order.getCode());
	}

	protected List<DiscountValue> convertToEntryDiscounts(final List<LineItemDomainSpecific> lineItems,
			final ItemDomainSpecific item, final AbstractOrderModel order)
	{
		final String expectedCurrencyCode = order.getCurrency().getIsocode();
		final List<DiscountValue> discountValues = new ArrayList<>();
		for (final RetailPriceModifierDomainSpecific priceModifier : item.getRetailPriceModifier())
		{
			if (isItemDiscount(priceModifier) || isDistributedItemDiscount(priceModifier))
			{
				final Amount amount = priceModifier.getAmount();
				final double discount = amount.getValue().doubleValue();
				String currencyCode = amount.getCurrency();
				final double itemQty = item.getQuantity().get(0).getValue().doubleValue();
				final double discountAmountPerItem = Math.abs(discount) / itemQty;
				final String code = codeForItemDiscount(lineItems, priceModifier, order);
				LOG.debug("Found discount with code {}, total value {} for qty {}, scaled to {}", code, discount, itemQty,
						discountAmountPerItem);
				if (currencyCode == null)
				{
					currencyCode = expectedCurrencyCode;
				}
				else if (!currencyCode.equals(expectedCurrencyCode))
				{
					LOG.error("Unexpected currency code {} for price modifier of item {}", currencyCode, item.getItemID().get(0)
							.getValue()
							+ ", setting to " + expectedCurrencyCode);
					currencyCode = expectedCurrencyCode;
				}
				discountValues.add(new DiscountValue(code, discountAmountPerItem, true, currencyCode));
			}
		}
		return discountValues;
	}

	protected boolean isItemDiscount(final RetailPriceModifierDomainSpecific priceModifier)
	{
		// Distributed Header discount has an item link, pure item discount not.
		final List<BigInteger> itemLink = priceModifier.getItemLink();
		return (itemLink == null || itemLink.isEmpty()) && amountNotZero(priceModifier.getAmount());
	}

	private boolean amountNotZero(final Amount amount)
	{
		return amount != null && amount.getValue() != null && !amount.getValue().equals(BigDecimal.ZERO);
	}

	protected boolean isDistributedItemDiscount(final RetailPriceModifierDomainSpecific priceModifier)
	{
		// Distributed Header discount has an item link, pure item discount not.
		final List<BigInteger> itemLink = priceModifier.getItemLink();
		return (itemLink != null && !itemLink.isEmpty()) && amountNotZero(priceModifier.getAmount());
	}

	@SuppressWarnings("javadoc")
	public PPSAccessorHelper getAccessorHelper()
	{
		return accessorHelper;
	}

	@SuppressWarnings("javadoc")
	public void setAccessorHelper(final PPSAccessorHelper accessorHelper)
	{
		this.accessorHelper = accessorHelper;
	}

}
