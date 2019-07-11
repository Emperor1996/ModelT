/*****************************************************************************
 Class:        TakeHomeNowComponentControllerTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.frontend.controllers.cms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.acceleratorstorefrontcommons.forms.AddToCartForm;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceAgentNotLoggedInException;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import com.sap.retail.isce.facade.exception.StoreContextNotDefinedException;
import com.sap.retail.isce.facade.impl.DefaultInstorecsAssistedServiceFacade;



/**
 * Test for TakeHomeNowComponentController
 *
 */
@UnitTest
public class TakeHomeNowComponentControllerTest
{
	private static final String ERROR_MSG_TYPE = "errorMsg";
	private static final String CART_PAGE = "fragments/cart/addToCartPopup";

	private final TakeHomeNowComponentController controller = new TakeHomeNowComponentController();
	@Mock
	private DefaultInstorecsAssistedServiceFacade assistedServiceFacade;
	@Mock
	private CartFacade cartFacade;
	@Mock
	private ProductFacade productFacade;
	@Mock
	private AddToCartForm form;
	@Mock
	private BindingResult bindingErrors;

	private final Model model = new ExtendedModelMap();

	private final PointOfServiceData pointOfService = new PointOfServiceData();
	@Mock
	private CartModificationData cartModification;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);

		controller.setAssistedServiceFacade(assistedServiceFacade);
		controller.setCartFacade(cartFacade);
		controller.setProductFacade(productFacade);
	}

	@Test
	public void testProductInStore() throws AssistedServiceAgentNotLoggedInException, StoreContextNotDefinedException,
			CommerceCartModificationException
	{
		final String code = "ProductCode";
		pointOfService.setDisplayName("Walldorf");
		final long quantity = 2;
		Mockito.doReturn(Boolean.FALSE).when(bindingErrors).hasErrors();
		Mockito.doReturn(Long.valueOf(quantity)).when(form).getQty();
		Mockito.doReturn(pointOfService).when(assistedServiceFacade).getEmployeeStore();
		given(cartFacade.addToCart(code, quantity, pointOfService.getDisplayName())).willReturn(cartModification);
		final OrderEntryData cartEntry = new OrderEntryData();
		cartEntry.setDeliveryPointOfService(pointOfService);
		Mockito.doReturn(cartEntry).when(cartModification).getEntry();
		Mockito.doReturn(Long.valueOf(1L)).when(cartModification).getQuantityAdded();

		final String page = controller.addToCart(code, model, form, bindingErrors);

		verify(cartFacade, times(1)).addToCart(eq(code), eq(quantity), eq(pointOfService.getDisplayName()));

		assertTrue(model.containsAttribute("quantity"));
		assertTrue(model.containsAttribute("errorMsg"));
		assertEquals(Long.valueOf(1L), model.asMap().get("quantity"));
		assertEquals("basket.information.quantity.reducedNumberOfItemsAdded.null", model.asMap().get("errorMsg"));
		assertTrue(model.containsAttribute("entry"));
		assertEquals("Cart page should be shown:", page, CART_PAGE);
	}

	@Test
	public void testProductInStore0Quantity() throws AssistedServiceAgentNotLoggedInException, StoreContextNotDefinedException,
			CommerceCartModificationException
	{
		final String code = "ProductCode";
		pointOfService.setDisplayName("Walldorf");
		final long quantity = 1;
		Mockito.doReturn(Boolean.FALSE).when(bindingErrors).hasErrors();
		Mockito.doReturn(Long.valueOf(quantity)).when(form).getQty();
		Mockito.doReturn(pointOfService).when(assistedServiceFacade).getEmployeeStore();
		given(cartFacade.addToCart(code, quantity, pointOfService.getDisplayName())).willReturn(cartModification);
		final OrderEntryData cartEntry = new OrderEntryData();
		cartEntry.setDeliveryPointOfService(pointOfService);
		Mockito.doReturn(cartEntry).when(cartModification).getEntry();
		Mockito.doReturn(Long.valueOf(0L)).when(cartModification).getQuantityAdded();

		final String page = controller.addToCart(code, model, form, bindingErrors);

		verify(cartFacade, times(1)).addToCart(eq(code), eq(quantity), eq(pointOfService.getDisplayName()));

		assertTrue(model.containsAttribute("quantity"));
		assertTrue(model.containsAttribute("errorMsg"));
		assertEquals(Long.valueOf(0L), model.asMap().get("quantity"));
		assertEquals("basket.information.quantity.noItemsAdded.null", model.asMap().get("errorMsg"));
		assertTrue(model.containsAttribute("entry"));
		assertEquals("Cart page should be shown:", page, CART_PAGE);
	}

	@Test
	public void testGetViewWithBindingErrorMessages()
	{
		final String[] code = new String[1];
		code[0] = "typeMismatch";
		org.springframework.validation.ObjectError err = new org.springframework.validation.ObjectError("objectName", code, null,
				"defaultMessage");

		Map<?, ?> target = new HashMap<>();
		BindingResult bindingResult = new MapBindingResult(target, "objectName");
		bindingResult.addError(err);
		String viewWithBindingErrorMessages = controller.getViewWithBindingErrorMessages(model, bindingResult);

		assertEquals(viewWithBindingErrorMessages, "fragments/cart/addToCartPopup");
		assertTrue(model.containsAttribute("errorMsg"));
		assertEquals("basket.error.quantity.invalid.binding", model.asMap().get("errorMsg"));

		// Other code
		code[0] = "otherTypeMismatch";
		err = new org.springframework.validation.ObjectError("objectName", code, null, "defaultMessage");

		target = new HashMap<>();
		bindingResult = new MapBindingResult(target, "objectName");
		bindingResult.addError(err);
		viewWithBindingErrorMessages = controller.getViewWithBindingErrorMessages(model, bindingResult);

		assertEquals(viewWithBindingErrorMessages, "fragments/cart/addToCartPopup");
		assertTrue(model.containsAttribute("errorMsg"));
		assertEquals("defaultMessage", model.asMap().get("errorMsg"));

	}

	@Test
	public void testNoStoreIsFound() throws Exception
	{
		final String code = "ProductCode";
		reset(assistedServiceFacade);
		when(assistedServiceFacade.getEmployeeStore()).thenThrow(AssistedServiceAgentNotLoggedInException.class);

		final long quantity = 1;
		Mockito.doReturn(Boolean.FALSE).when(bindingErrors).hasErrors();
		Mockito.doReturn(Long.valueOf(quantity)).when(form).getQty();

		final String page = controller.addToCart(code, model, form, bindingErrors);

		assertTrue(model.containsAttribute(ERROR_MSG_TYPE));
		assertEquals("Cart page should be shown:", page, CART_PAGE);

	}

	@Test
	public void testAddToCartStoreContextNotDefinedException() throws Exception
	{
		final String code = "ProductCode";
		reset(assistedServiceFacade);
		when(assistedServiceFacade.getEmployeeStore()).thenThrow(StoreContextNotDefinedException.class);

		final long quantity = 1;
		Mockito.doReturn(Boolean.FALSE).when(bindingErrors).hasErrors();
		Mockito.doReturn(Long.valueOf(quantity)).when(form).getQty();

		final String page = controller.addToCart(code, model, form, bindingErrors);

		assertTrue(model.containsAttribute(ERROR_MSG_TYPE));
		assertEquals("Cart page should be shown:", page, CART_PAGE);

	}

	@Test
	public void testBindingError() throws Exception
	{
		final String code = "ProductCode";

		Mockito.doReturn(Boolean.TRUE).when(bindingErrors).hasErrors();

		final String page = controller.addToCart(code, model, form, bindingErrors);

		verify(bindingErrors, times(1)).getAllErrors();

		assertFalse(model.containsAttribute("product"));
		assertFalse(model.containsAttribute("quantity"));
		assertEquals("Cart page should be shown:", page, CART_PAGE);



	}

	@Test
	public void testQuantityZero() throws Exception
	{
		final String code = "ProductCode";
		reset(assistedServiceFacade);
		when(assistedServiceFacade.getEmployeeStore()).thenThrow(AssistedServiceAgentNotLoggedInException.class);

		final long quantity = 0;
		Mockito.doReturn(Boolean.FALSE).when(bindingErrors).hasErrors();
		Mockito.doReturn(Long.valueOf(quantity)).when(form).getQty();
		final ProductData productData = new ProductData();
		productData.setCode(code);
		Mockito.doReturn(productData).when(productFacade)
				.getProductForCodeAndOptions(eq(code), eq(Arrays.asList(ProductOption.BASIC)));

		final String page = controller.addToCart(code, model, form, bindingErrors);

		assertTrue(model.containsAttribute(ERROR_MSG_TYPE));
		assertEquals(model.asMap().get(ERROR_MSG_TYPE), "basket.error.quantity.invalid");
		assertEquals(model.asMap().get("quantity"), Long.valueOf(0L));
		assertEquals(productData, model.asMap().get("product"));
		assertEquals("Cart page should be shown:", page, CART_PAGE);
	}

	@Test
	public void testQuantityNegative() throws Exception
	{
		final String code = "ProductCode";
		reset(assistedServiceFacade);
		when(assistedServiceFacade.getEmployeeStore()).thenThrow(AssistedServiceAgentNotLoggedInException.class);

		final long quantity = -1;
		Mockito.doReturn(Boolean.FALSE).when(bindingErrors).hasErrors();
		Mockito.doReturn(Long.valueOf(quantity)).when(form).getQty();


		final String page = controller.addToCart(code, model, form, bindingErrors);

		assertTrue(model.containsAttribute(ERROR_MSG_TYPE));
		assertEquals(model.asMap().get(ERROR_MSG_TYPE), "basket.error.quantity.invalid");
		assertEquals(model.asMap().get("quantity"), Long.valueOf(0L));
		assertEquals("Cart page should be shown:", page, CART_PAGE);
	}

	@Test
	public void testGetterSetter() throws Exception
	{
		assertSame("getAssistedServiceFacade not identical", controller.getAssistedServiceFacade(), assistedServiceFacade);
		assertSame("getAssistedServiceFacade not identical", controller.getProductFacade(), productFacade);
		assertSame("getCartFacade not identical", controller.getCartFacade(), cartFacade);
	}

}
