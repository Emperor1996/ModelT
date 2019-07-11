/*****************************************************************************
 Class:        TakeHomeNowComponentController
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.frontend.controllers.cms;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.acceleratorstorefrontcommons.forms.AddToCartForm;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceAgentNotLoggedInException;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.yacceleratorstorefront.controllers.ControllerConstants;

import java.util.Arrays;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sap.retail.isce.facade.exception.StoreContextNotDefinedException;
import com.sap.retail.isce.facade.impl.DefaultInstorecsAssistedServiceFacade;


/**
 * Controller to process the take home now action
 *
 */
@Controller
public class TakeHomeNowComponentController extends AbstractController
{
	private static final Logger LOG = Logger.getLogger(TakeHomeNowComponentController.class);

	private static final String TYPE_MISMATCH_ERROR_CODE = "typeMismatch";
	private static final String ERROR_MSG_TYPE = "errorMsg";
	private static final String QUANTITY_INVALID_BINDING_MESSAGE_KEY = "basket.error.quantity.invalid.binding";
	private static final String QUANTITY = "quantity";

	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	@Resource(name = "assistedServiceFacade")
	private DefaultInstorecsAssistedServiceFacade assistedServiceFacade;

	@RequestMapping(value = "/takehomenowaction/act", method = RequestMethod.POST, produces = "application/json")
	public String addToCart(@RequestParam("productCodePost") final String code, final Model model,
			@Valid final AddToCartForm form, final BindingResult bindingErrors)
	{
		if (bindingErrors.hasErrors())
		{
			return getViewWithBindingErrorMessages(model, bindingErrors);
		}

		final long qty = form.getQty();

		if (qty <= 0)
		{
			model.addAttribute(ERROR_MSG_TYPE, "basket.error.quantity.invalid");
			model.addAttribute(QUANTITY, Long.valueOf(0L));
		}
		else
		{
			try
			{
				final PointOfServiceData pointOfService = assistedServiceFacade.getEmployeeStore();
				final CartModificationData cartModification = cartFacade.addToCart(code, qty, pointOfService.getDisplayName());
				model.addAttribute(QUANTITY, Long.valueOf(cartModification.getQuantityAdded()));
				model.addAttribute("entry", cartModification.getEntry());
				model.addAttribute("cartCode", cartModification.getCartCode());

				if (cartModification.getQuantityAdded() == 0L)
				{
					model.addAttribute(ERROR_MSG_TYPE, "basket.information.quantity.noItemsAdded." + cartModification.getStatusCode());
				}
				else if (cartModification.getQuantityAdded() < qty)
				{
					model.addAttribute(ERROR_MSG_TYPE,
							"basket.information.quantity.reducedNumberOfItemsAdded." + cartModification.getStatusCode());
				}
			}
			catch (final AssistedServiceAgentNotLoggedInException ex)
			{
				LOG.error("SECURITY issue: Take home now action called although assisted service agent is not logged in", ex);
				model.addAttribute(ERROR_MSG_TYPE, "basket.error.occurred");
				model.addAttribute(QUANTITY, Long.valueOf(0L));
			}
			catch (final CommerceCartModificationException | StoreContextNotDefinedException ex)
			{
				LOG.error(
						"Exception occurred: either could not get store of employee (store context undefined) or could not modify cart",
						ex);
				model.addAttribute(ERROR_MSG_TYPE, "basket.error.occurred");
				model.addAttribute(QUANTITY, Long.valueOf(0L));
			}
		}

		model.addAttribute("product", productFacade.getProductForCodeAndOptions(code, Arrays.asList(ProductOption.BASIC)));

		return ControllerConstants.Views.Fragments.Cart.AddToCartPopup;
	}

	protected String getViewWithBindingErrorMessages(final Model model, final BindingResult bindingErrors)
	{
		for (final ObjectError error : bindingErrors.getAllErrors())
		{
			if (isTypeMismatchError(error))
			{
				model.addAttribute(ERROR_MSG_TYPE, QUANTITY_INVALID_BINDING_MESSAGE_KEY);
			}
			else
			{
				model.addAttribute(ERROR_MSG_TYPE, error.getDefaultMessage());
			}
		}
		return ControllerConstants.Views.Fragments.Cart.AddToCartPopup;
	}

	protected boolean isTypeMismatchError(final ObjectError error)
	{
		return error.getCode().equals(TYPE_MISMATCH_ERROR_CODE);
	}

	/**
	 * @return the cartFacade
	 */
	public CartFacade getCartFacade()
	{
		return cartFacade;
	}

	/**
	 * @param cartFacade
	 *           the cartFacade to set
	 */
	public void setCartFacade(final CartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
	}

	/**
	 * @return the productFacade
	 */
	public ProductFacade getProductFacade()
	{
		return productFacade;
	}

	/**
	 * @param productFacade
	 *           the productFacade to set
	 */
	public void setProductFacade(final ProductFacade productFacade)
	{
		this.productFacade = productFacade;
	}

	/**
	 * @return the assistedServiceFacade
	 */
	public DefaultInstorecsAssistedServiceFacade getAssistedServiceFacade()
	{
		return assistedServiceFacade;
	}

	/**
	 * @param assistedServiceFacade
	 *           the assistedServiceFacade to set
	 */
	public void setAssistedServiceFacade(final DefaultInstorecsAssistedServiceFacade assistedServiceFacade)
	{
		this.assistedServiceFacade = assistedServiceFacade;
	}
}
