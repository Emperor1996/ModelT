/*****************************************************************************
 Class:        OrderChangeFacadeDefaultImplUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.service.data.CommerceCartParameter;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.ordercancel.OrderCancelDenialReason;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.store.services.BaseStoreService;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.retail.isce.UnitTestBase;


/**
 * Unit Test for Default implementation class OrderChangeDefaultImpl
 *
 */
@UnitTest
public class OrderChangeFacadeDefaultImplUnitTest extends UnitTestBase
{
	private OrderChangeFacadeDefaultImpl classUnderTest;

	OrderModel theOrder;
	CartModel theCart;
	AbstractOrderEntryModel entryOne;
	AbstractOrderEntryModel entryTwo;

	UnitModel unitOne = new UnitModel("c1", "t1");

	@Mock
	private CustomerAccountService customerAccountServiceMock;
	@Mock
	private BaseStoreService baseStoreServiceMock;
	@Mock
	private CartService cartServiceMock;
	@Mock
	private CommerceCartService commerceCartServiceMock;
	@Mock
	private ModelService modelServiceMock;
	@Mock
	private L10NService L10NServiceMock;

	class MyOrderCancelDenialReason implements OrderCancelDenialReason
	{
		public MyOrderCancelDenialReason()
		{
			super();
		}
	}


	@Override
	@Before
	public void setUp()
	{
		super.setUp();
		MockitoAnnotations.initMocks(this);
		classUnderTest = new OrderChangeFacadeDefaultImpl();

		// inject this mocked Service into the classUnderTest Facade
		classUnderTest.setCustomerAccountService(customerAccountServiceMock);
		classUnderTest.setBaseStoreService(baseStoreServiceMock);
		classUnderTest.setCartService(cartServiceMock);
		classUnderTest.setCommerceCartService(commerceCartServiceMock);
		classUnderTest.setModelService(modelServiceMock);
		classUnderTest.setL10NService(L10NServiceMock);

		theOrder = new OrderModel();
		theCart = new CartModel();

		Mockito.doReturn(null).when(baseStoreServiceMock).getCurrentBaseStore();
		Mockito.doReturn(theOrder).when(customerAccountServiceMock).getOrderForCode("123456", null);
		Mockito.doReturn(theCart).when(cartServiceMock).getSessionCart();

		Mockito.doReturn(Boolean.TRUE).when(commerceCartServiceMock).calculateCart(Mockito.any(CommerceCartParameter.class));
		Mockito.doNothing().when(modelServiceMock).save(theCart);
		Mockito.doReturn("Created via sales order").when(L10NServiceMock)
				.getLocalizedString("instorecs.orderchange.salesorder.ref", null);

		entryOne = new OrderEntryModel();

		entryOne.setUnit(unitOne);
		entryOne.setQuantity(Long.valueOf(5));
		entryOne.setExternalConfiguration("ExtConfig1");
		entryOne.setEntryNumber(Integer.valueOf(1));

		entryTwo = new OrderEntryModel();

		entryTwo.setUnit(unitOne);
		entryTwo.setQuantity(Long.valueOf(8));
		entryTwo.setGiveAway(Boolean.TRUE);
		entryTwo.setEntryNumber(Integer.valueOf(2));
	}

	/**
	 * Tests copyOrderToCartAndCancel.
	 */
	@Test
	public void copyOrderToCartAndCancelNoOrder()
	{
		//No order specified
		final boolean result = classUnderTest.copyOrderToCartAndCancel(null);

		assertEquals("true should be returned", Boolean.TRUE, Boolean.valueOf(result));

		Mockito.verify(baseStoreServiceMock, Mockito.times(0)).getCurrentBaseStore();
		Mockito.verify(commerceCartServiceMock, Mockito.times(0)).calculateCart(Mockito.any(CommerceCartParameter.class));
		Mockito.verify(modelServiceMock, Mockito.times(0)).save(theCart);
	}

	@Test
	public void copyOrderToCartAndCancelNullEntries()
	{
		//Null order entry list
		final boolean result = classUnderTest.copyOrderToCartAndCancel("123456");

		assertEquals("true should be returned", Boolean.TRUE, Boolean.valueOf(result));
		Mockito.verify(baseStoreServiceMock, Mockito.times(1)).getCurrentBaseStore();
		Mockito.verify(commerceCartServiceMock, Mockito.times(0)).calculateCart(Mockito.any(CommerceCartParameter.class));
		Mockito.verify(modelServiceMock, Mockito.times(0)).save(theCart);

	}

	@Test
	public void copyOrderToCartAndCancelEmptyEntries()
	{
		//Empty order entry list
		theOrder.setEntries(new ArrayList());

		final boolean result = classUnderTest.copyOrderToCartAndCancel("123456");

		assertEquals("true should be returned", Boolean.TRUE, Boolean.valueOf(result));

		Mockito.verify(baseStoreServiceMock, Mockito.times(1)).getCurrentBaseStore();
		Mockito.verify(commerceCartServiceMock, Mockito.times(0)).calculateCart(Mockito.any(CommerceCartParameter.class));
		Mockito.verify(modelServiceMock, Mockito.times(0)).save(theCart);
	}

	@Test
	public void copyOrderToCartAndCancelOneEntryEmptyBasket()
	{
		final List entries = new ArrayList();
		entries.add(entryOne);
		theOrder.setEntries(entries);

		final CartEntryModel cartEntry = new CartEntryModel();
		Mockito.doReturn(cartEntry).when(cartServiceMock).addNewEntry(theCart, null, 5, unitOne, -1, false);

		final boolean result = classUnderTest.copyOrderToCartAndCancel("123456");

		assertEquals("true should be returned", Boolean.TRUE, Boolean.valueOf(result));

		Mockito.verify(baseStoreServiceMock, Mockito.times(1)).getCurrentBaseStore();
		Mockito.verify(commerceCartServiceMock, Mockito.times(1)).calculateCart(Mockito.any(CommerceCartParameter.class));
		Mockito.verify(modelServiceMock, Mockito.times(1)).save(theCart);
		Mockito.verify(cartServiceMock, Mockito.times(1)).addNewEntry(theCart, null, 5, unitOne, -1, false);

		assertEquals("Cart entry 1 - External Config should 'ExtConfig1'", "ExtConfig1", cartEntry.getExternalConfiguration());
		assertEquals("Check on right reference text", "[Created via sales order {123456}]", theCart.getDescription());
	}

	@Test
	public void copyOrderToCartAndCanceltwoEntriesEmptyBasket()
	{
		final List entries = new ArrayList();
		entries.add(entryOne);
		entries.add(entryTwo);
		theOrder.setEntries(entries);

		final CartEntryModel cartEntryOne = new CartEntryModel();
		final CartEntryModel cartEntryTwo = new CartEntryModel();
		Mockito.doReturn(cartEntryOne).when(cartServiceMock).addNewEntry(theCart, null, 5, unitOne, -1, false);
		Mockito.doReturn(cartEntryTwo).when(cartServiceMock).addNewEntry(theCart, null, 8, unitOne, -1, false);

		final boolean result = classUnderTest.copyOrderToCartAndCancel("123456");

		assertEquals("true should be returned", Boolean.TRUE, Boolean.valueOf(result));

		Mockito.verify(baseStoreServiceMock, Mockito.times(1)).getCurrentBaseStore();
		Mockito.verify(commerceCartServiceMock, Mockito.times(1)).calculateCart(Mockito.any(CommerceCartParameter.class));
		Mockito.verify(modelServiceMock, Mockito.times(1)).save(theCart);
		Mockito.verify(cartServiceMock, Mockito.times(1)).addNewEntry(theCart, null, 5, unitOne, -1, false);
		Mockito.verify(cartServiceMock, Mockito.times(1)).addNewEntry(theCart, null, 8, unitOne, -1, false);

		assertEquals("Cart entry 1 - External Config should 'ExtConfig1'", "ExtConfig1", cartEntryOne.getExternalConfiguration());
		assertEquals("Cart entry 2 - GiveAway should be true", Boolean.TRUE, cartEntryTwo.getGiveAway());
	}

	@Test
	public void copyOrderToCartAndCancelFailure()
	{
		final List entries = new ArrayList();
		entries.add(entryOne);
		theOrder.setEntries(entries);

		Mockito.doReturn(null).when(cartServiceMock).addNewEntry(theCart, null, 5, unitOne, -1, false);

		final boolean result = classUnderTest.copyOrderToCartAndCancel("123456");

		assertEquals("False should be returned", Boolean.FALSE, Boolean.valueOf(result));

		Mockito.verify(baseStoreServiceMock, Mockito.times(1)).getCurrentBaseStore();
		Mockito.verify(commerceCartServiceMock, Mockito.times(1)).calculateCart(Mockito.any(CommerceCartParameter.class));

		Mockito.verify(modelServiceMock, Mockito.times(1)).save(theCart);
		Mockito.verify(modelServiceMock, Mockito.times(1)).removeAll(Mockito.any(List.class));
		Mockito.verify(modelServiceMock, Mockito.times(1)).refresh(theCart);
	}

	@Test
	public void exceptionFinalizeCart()
	{
		Mockito.doThrow(new ModelSavingException("")).when(modelServiceMock).save(theCart);

		final boolean result = classUnderTest.finalizeCart(theCart);

		assertEquals("False should be returned", Boolean.FALSE, Boolean.valueOf(result));
	}

	@Test
	public void exceptionCopytemsToCart()
	{
		final List entries = new ArrayList();
		entries.add(entryOne);

		Mockito.doReturn(new CartEntryModel()).when(cartServiceMock).addNewEntry(theCart, null, 5, unitOne, -1, false);
		Mockito.doThrow(new ModelSavingException("")).when(modelServiceMock).save(Mockito.any(CartEntryModel.class));

		final boolean result = classUnderTest.copyItemsToCart(entries, theCart, new ArrayList());

		assertEquals("False should be returned", Boolean.FALSE, Boolean.valueOf(result));
	}

	@Test
	public void exceptionRemoveCartEntries()
	{
		Mockito.doThrow(new ModelRemovalException("", null)).when(modelServiceMock).removeAll(Mockito.any(List.class));

		final boolean result = classUnderTest.removeCartEntries(theCart, new ArrayList());

		assertEquals("False should be returned", Boolean.FALSE, Boolean.valueOf(result));
	}

	@Test
	public void resetChangesError()
	{
		Mockito.doThrow(new ModelRemovalException("", null)).when(modelServiceMock).removeAll(Mockito.any(List.class));

		try
		{
			classUnderTest.resetCartChanges(theCart, new ArrayList());
			assertTrue("Exception should have been thrown", false);
		}
		catch (final Exception e)
		{
			assertTrue("An Exception was thrown", true);
		}
	}

	@Test
	public void orderCancelExceptionCancelOrder()
	{
		final List entries = new ArrayList();
		entries.add(entryOne);
		theOrder.setEntries(entries);

		Mockito.doThrow(new ModelSavingException("", null)).when(modelServiceMock).save(Mockito.any(OrderModel.class));

		final boolean result = classUnderTest.cancelOrder(theOrder);

		assertEquals("False should be returned", Boolean.FALSE, Boolean.valueOf(result));
	}

	@Test
	public void orderCancel()
	{
		final List entries = new ArrayList();
		entries.add(entryOne);
		theOrder.setEntries(entries);

		final boolean result = classUnderTest.cancelOrder(theOrder);

		assertEquals("True should be returned", Boolean.TRUE, Boolean.valueOf(result));
	}

	@Test
	public void testGetterSetter()
	{
		assertSame("getCustomerAccountService not identical", customerAccountServiceMock,
				classUnderTest.getCustomerAccountService());
		assertSame("getBaseStoreService not identical", baseStoreServiceMock, classUnderTest.getBaseStoreService());
		assertSame("getCartService not identical", cartServiceMock, classUnderTest.getCartService());
		assertSame("getCommerceCartService not identical", commerceCartServiceMock, classUnderTest.getCommerceCartService());
		assertSame("getModelService not identical", modelServiceMock, classUnderTest.getModelService());
	}
}
