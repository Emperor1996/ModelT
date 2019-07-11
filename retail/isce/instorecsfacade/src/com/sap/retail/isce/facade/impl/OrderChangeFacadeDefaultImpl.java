/*****************************************************************************
 Class:        OrderChangeFacadeDefaultImpl
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade.impl;

import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.ordercancel.DefaultOrderCancelDenialReason;
import de.hybris.platform.ordercancel.OrderCancelDenialReason;
import de.hybris.platform.ordercancel.OrderCancelDeniedException;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.sap.retail.isce.facade.OrderChangeFacade;
import com.sap.retail.isce.sap.exception.SAPISCERuntimeException;


/**
 * Default implementation of the OrderChangeFacade interface.
 */
public class OrderChangeFacadeDefaultImpl implements OrderChangeFacade
{
	private static final String SALES_ORDER_REFERENCE = "instorecs.orderchange.salesorder.ref";
	private CustomerAccountService customerAccountService;
	private BaseStoreService baseStoreService;
	private CartService cartService;
	private CommerceCartService commerceCartService;
	private ModelService modelService;
	private L10NService l10NService;

	private static final Logger LOG = Logger.getLogger(OrderChangeFacadeDefaultImpl.class);

	@Override
	public boolean copyOrderToCartAndCancel(final String orderCode)
	{
		if (isValidOrderCode(orderCode))
		{
			return true;
		}

		final BaseStoreModel baseStoreModel = baseStoreService.getCurrentBaseStore();
		final OrderModel orderModel = customerAccountService.getOrderForCode(orderCode, baseStoreModel);
		final List<AbstractOrderEntryModel> orderEntries = orderModel.getEntries();

		if (areOrderEntriesValid(orderEntries))
		{
			return true;
		}

		final List<CartEntryModel> newCartEntries = new ArrayList();

		// Add reference of sourcing sales order in an easy parsable way (sales order number is not supposed to be part of the
		// translation to avoid issue in the translation system by usage of {}).
		final String tmpRef = getL10NService().getLocalizedString(SALES_ORDER_REFERENCE, null);
		cartService.getSessionCart().setDescription("[" + tmpRef + " {" + orderCode + "}]");
		if (!copyItemsToCart(orderEntries, cartService.getSessionCart(), newCartEntries)
				|| !finalizeCart(cartService.getSessionCart()) || !cancelOrder(orderModel))
		{
			return resetCartChanges(cartService.getSessionCart(), newCartEntries);
		}

		return true;
	}

	/**
	 * Checks if the orderEntries is not null and not empty.
	 *
	 * @param orderEntries
	 *           the orderEntries to check
	 * @return true if the orderEntries is not null and not empty, else false
	 */
	protected boolean areOrderEntriesValid(final List<AbstractOrderEntryModel> orderEntries)
	{
		return orderEntries == null || orderEntries.isEmpty();
	}

	/**
	 * Checks if the ordeCode is not null and not empty.
	 *
	 * @param ordeCode
	 *           the ordeCode to check
	 * @return true if the ordeCode is not null and not empty, else false
	 */
	protected boolean isValidOrderCode(final String ordeCode)
	{
		return ordeCode == null || ordeCode.isEmpty();
	}

	/**
	 * Tries to reset the changes done to the given cart.
	 *
	 * @param theCart
	 *           the cart to remove the entries from
	 * @param newCartEntries
	 *           the entries to remove from the cart
	 * @return false always
	 * @throws SAPISCERuntimeException
	 *            if the reset fails unrecoverably
	 */
	protected boolean resetCartChanges(final CartModel theCart, final List<CartEntryModel> newCartEntries)
			throws SAPISCERuntimeException
	{
		if (!removeCartEntries(theCart, newCartEntries))
		{
			LOG.error("ISCE Orderchange - copyOrderToCartAndCancel failed unrecoverably");
			throw new SAPISCERuntimeException("ISCE Orderchange - copyOrderToCartAndCancel failed unrecoverably");
		}
		return false;
	}

	/**
	 * Copy the given order Items to the cart.
	 *
	 * @param orderEntries
	 *           the entries to copy
	 * @param theCart
	 *           the cart to copy the entries to
	 * @param newCartEntries
	 *           the newly created cart entries will added to this list
	 *
	 * @return true if everything went well, false else
	 */
	protected boolean copyItemsToCart(final List<AbstractOrderEntryModel> orderEntries, final CartModel theCart,
			final List<CartEntryModel> newCartEntries)
	{
		CartEntryModel cartEntry;

		for (final AbstractOrderEntryModel orderEntry : orderEntries)
		{
			cartEntry = cartService.addNewEntry(theCart, orderEntry.getProduct(), orderEntry.getQuantity().longValue(),
					orderEntry.getUnit(), -1, false);
			if (cartEntry == null)
			{
				LOG.error("ISCE Orderchange - Oder entry " + orderEntry.getProduct()
						+ " could not be copied to basket - cartEntry was null");
				return false;
			}
			newCartEntries.add(cartEntry);
			cartEntry.setExternalConfiguration(orderEntry.getExternalConfiguration());
			cartEntry.setGiveAway(orderEntry.getGiveAway());
			cartEntry.setDeliveryPointOfService(orderEntry.getDeliveryPointOfService());
			try
			{
				modelService.save(cartEntry);
			}
			catch (final ModelSavingException e)
			{
				LOG.error("ISCE Orderchange - Copied cart entry " + orderEntry.getProduct() + " could not be saved", e);
				return false;
			}
		}

		return true;
	}

	/**
	 * Re-calculate and save the cart.
	 *
	 * @param theCart
	 *           the cart to Re-calculate and save
	 *
	 * @return true if everything went well, false else
	 */
	protected boolean finalizeCart(final CartModel theCart)
	{
		theCart.setCalculated(Boolean.FALSE);
		final CommerceCartParameter cartParameter = new CommerceCartParameter();
		cartParameter.setEnableHooks(true);
		cartParameter.setCart(theCart);
		commerceCartService.calculateCart(cartParameter);
		try
		{
			modelService.save(theCart);
		}
		catch (final ModelSavingException e)
		{
			LOG.error("ISCE Orderchange - Finalizing the cart failed", e);
			return false;
		}
		return true;
	}

	/**
	 * Removes the given entries from the cart.
	 *
	 * @param theCart
	 *           the cart to remove the entries from
	 *
	 * @param cartEntries
	 *           the cart entries that must be removed from the cart
	 *
	 * @return true if everything went well, false else
	 */
	protected boolean removeCartEntries(final CartModel theCart, final List<CartEntryModel> cartEntries)
	{
		try
		{
			modelService.removeAll(cartEntries);
		}
		catch (final ModelRemovalException e)
		{
			LOG.error("ISCE Orderchange - Removing cart entries failed", e);
			return false;
		}
		modelService.refresh(theCart);
		return this.finalizeCart(theCart);
	}

	/**
	 * Cancel the given order.
	 *
	 * @param orderModel
	 *           the order to cancel
	 *
	 * @return true if everything went well, false else
	 */
	protected boolean cancelOrder(final OrderModel orderModel)
	{
		orderModel.setStatus(OrderStatus.CANCELLED);
		try
		{
			modelService.save(orderModel);
		}
		catch (final ModelSavingException e)
		{
			LOG.error("ISCE Orderchange - Order could not be canceled", e);
			return false;
		}

		return true;
	}

	/**
	 * Log the denial reasons of the given exception as error.
	 *
	 * @param e
	 *           OrderCancelDeniedException the exception to log the denial reasons for.
	 */
	protected void logDenialReasons(final OrderCancelDeniedException e)
	{
		final List<OrderCancelDenialReason> denialReasons = e.getCancelDecision().getDenialReasons();

		for (final OrderCancelDenialReason denialReason : denialReasons)
		{
			if (denialReason instanceof DefaultOrderCancelDenialReason)
			{
				final DefaultOrderCancelDenialReason defaultDenialReason = (DefaultOrderCancelDenialReason) denialReason;
				LOG.error("Denial reason :" + defaultDenialReason.getCode() + " - " + defaultDenialReason.getDescription());
				continue;
			}

			LOG.error("Denial reason :" + denialReason.toString());
		}
	}

	/**
	 * Get the customerAccountService.
	 *
	 * @return the customerAccountService
	 */
	public CustomerAccountService getCustomerAccountService()
	{
		return customerAccountService;
	}

	/**
	 * Set the customerAccountService.
	 *
	 * @param customerAccountService
	 *           the customerAccountService to set
	 */
	@Required
	public void setCustomerAccountService(final CustomerAccountService customerAccountService)
	{
		this.customerAccountService = customerAccountService;
	}

	/**
	 * Get the baseStoreService.
	 *
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	/**
	 * Set the baseStoreService.
	 *
	 * @param baseStoreService
	 *           the baseStoreService to set
	 */
	@Required
	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	/**
	 * Get the cartService.
	 *
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * Set the cartService.
	 *
	 * @param cartService
	 *           the cartService to set
	 */
	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	/**
	 * Get the commerceCartService.
	 *
	 * @return the commerceCartService
	 */
	public CommerceCartService getCommerceCartService()
	{
		return commerceCartService;
	}

	/**
	 * Set the commerceCartService.
	 *
	 * @param commerceCartService
	 *           the commerceCartService to set
	 */
	@Required
	public void setCommerceCartService(final CommerceCartService commerceCartService)
	{
		this.commerceCartService = commerceCartService;
	}

	/**
	 * Get the modelService.
	 *
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * Sets the modelService.
	 *
	 * @param modelService
	 *           the modelService to set
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	@Required
	public void setL10NService(final L10NService l10NService)
	{
		this.l10NService = l10NService;
	}

	protected L10NService getL10NService()
	{
		return l10NService;
	}


}
