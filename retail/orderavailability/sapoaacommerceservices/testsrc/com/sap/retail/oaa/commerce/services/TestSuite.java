/**
 *
 */
package com.sap.retail.oaa.commerce.services;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.sap.retail.oaa.commerce.services.atp.impl.DefaultATPResultHandlerTest;
import com.sap.retail.oaa.commerce.services.atp.impl.DefaultATPServiceTest;
import com.sap.retail.oaa.commerce.services.atp.strategy.impl.DefaultATPStrategyTest;
import com.sap.retail.oaa.commerce.services.common.util.ServiceUtilsTest;
import com.sap.retail.oaa.commerce.services.order.impl.DefaultSapOaaCartAdjustmentStrategyTest;
import com.sap.retail.oaa.commerce.services.order.impl.DefaultSapOaaCommerceAddToCartStrategyTest;
import com.sap.retail.oaa.commerce.services.order.impl.DefaultSapOaaCommerceUpdateCartEntryStrategyTest;
import com.sap.retail.oaa.commerce.services.reservation.impl.DefaultReservationRequestMapperTest;
import com.sap.retail.oaa.commerce.services.reservation.impl.DefaultReservationResultHandlerTest;
import com.sap.retail.oaa.commerce.services.reservation.impl.DefaultReservationServiceTest;
import com.sap.retail.oaa.commerce.services.reservation.strategy.impl.DefaultReservationStrategyTest;
import com.sap.retail.oaa.commerce.services.rest.impl.DefaultRestServiceConfigurationTest;
import com.sap.retail.oaa.commerce.services.rest.util.AuthenticationServiceTest;
import com.sap.retail.oaa.commerce.services.rest.util.DefaultHttpEntityBuilderTest;
import com.sap.retail.oaa.commerce.services.rest.util.HttpHeaderProviderTest;
import com.sap.retail.oaa.commerce.services.rest.util.URLProviderTest;
import com.sap.retail.oaa.commerce.services.sourcing.impl.DefaultSourcingRequestMapperTest;
import com.sap.retail.oaa.commerce.services.sourcing.impl.DefaultSourcingServiceTest;
import com.sap.retail.oaa.commerce.services.sourcing.impl.SourcingResultHandlerTest;
import com.sap.retail.oaa.commerce.services.sourcing.strategy.impl.DefaultSourcingStrategyTest;
import com.sap.retail.oaa.commerce.services.stock.impl.DefaultSapOaaCommerceStockServiceTest;
import com.sap.retail.oaa.commerce.services.stock.impl.DefaultSapOaaStockLevelStatusStrategyTest;
import com.sap.retail.oaa.commerce.services.strategies.impl.DefaultSapOaaCartValidationStrategyTest;
import com.sap.retail.oaa.commerce.services.strategies.impl.DefaultSapOaaPickupAvailabilityStrategyTest;


/**
 * Test suite class for all SAP OAA commerce services extensions
 */
@RunWith(Suite.class)
@SuiteClasses(
{
		// atp.impl.*
		DefaultATPResultHandlerTest.class, DefaultATPServiceTest.class,
		// strategy.impl.*
		DefaultATPStrategyTest.class,
		// common.util.*
		ServiceUtilsTest.class,
		// order.impl.*
		DefaultSapOaaCommerceAddToCartStrategyTest.class, DefaultSapOaaCommerceUpdateCartEntryStrategyTest.class,
		DefaultSapOaaCartAdjustmentStrategyTest.class,
		// reservation.impl.*
		DefaultReservationRequestMapperTest.class, DefaultReservationServiceTest.class, DefaultReservationResultHandlerTest.class,
		// reservation.strategy.impl.*
		DefaultReservationStrategyTest.class,
		// rest.util.*
		AuthenticationServiceTest.class, HttpHeaderProviderTest.class, URLProviderTest.class, DefaultHttpEntityBuilderTest.class,
		// rest.impl.*
		DefaultRestServiceConfigurationTest.class,
		// sourcing.impl.*
		DefaultSourcingRequestMapperTest.class, DefaultSourcingServiceTest.class, SourcingResultHandlerTest.class,
		// sourcing.strategy.*
		DefaultSourcingStrategyTest.class,
		// stock.impl.*
		DefaultSapOaaCommerceStockServiceTest.class, DefaultSapOaaStockLevelStatusStrategyTest.class,
		// strategies.impl
		DefaultSapOaaCartValidationStrategyTest.class, DefaultSapOaaPickupAvailabilityStrategyTest.class

})
public class TestSuite
{
	// Add all required JUnit Test classes
}
