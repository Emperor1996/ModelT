package com.sap.retail.oaa.orderexchange.actions;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction.Transition;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.sap.retail.oaa.commerce.services.rest.util.exception.CARBackendDownException;
import com.sap.retail.oaa.commerce.services.sourcing.SourcingService;
import com.sap.retail.oaa.commerce.services.sourcing.exception.SourcingException;


@UnitTest
@SuppressWarnings("javadoc")
public class CheckSAPOaaOrderActionTest
{
	private CheckSAPOaaOrderAction classUnderTest;

	@Before
	public void setup()
	{
		this.classUnderTest = new CheckSAPOaaOrderAction();
	}

	@Test
	/** Test should fail as order is null and hence parent action CheckSAPOrderAction will fail (Transition.NOK). */
	public void testParentActionNOK()
	{
		final OrderProcessModel process = new OrderProcessModel();

		//final DefaultReservationService reservationServiceMock = EasyMock.createNiceMock(DefaultReservationService.class);

		assertEquals(Transition.NOK, classUnderTest.executeAction(process));
	}


	@Test
	/** Test should fail due to error with reservation service. */
	public void testErrorSourcingService()
	{
		final OrderProcessModel process = new OrderProcessModel();

		final OrderModel order = new OrderModel();
		process.setOrder(order);

		final ModelService modelServiceMock = EasyMock.createNiceMock(ModelService.class);
		EasyMock.replay(modelServiceMock);
		classUnderTest.setModelService(modelServiceMock);

		final SourcingService sourcingServiceMock = EasyMock.createNiceMock(SourcingService.class);
		sourcingServiceMock.callRestServiceAndPersistResult(order);
		EasyMock.expectLastCall().andThrow(new SourcingException("Error Sourcing Service"));
		EasyMock.replay(sourcingServiceMock);
		classUnderTest.setSourcingService(sourcingServiceMock);

		final BaseSiteService baseSiteServiceMock = EasyMock.createNiceMock(BaseSiteService.class);
		baseSiteServiceMock.setCurrentBaseSite(EasyMock.anyObject(BaseSiteModel.class), EasyMock.anyBoolean());
		EasyMock.expectLastCall();
		EasyMock.replay(baseSiteServiceMock);
		classUnderTest.setBaseSiteService(baseSiteServiceMock);



		assertEquals(Transition.NOK, classUnderTest.executeAction(process));
	}

	@Test
	/** Test should fail due to CAR back end not reachable. */
	public void testBackendDown()
	{
		final OrderProcessModel process = new OrderProcessModel();

		final OrderModel order = new OrderModel();
		process.setOrder(order);

		final ModelService modelServiceMock = EasyMock.createNiceMock(ModelService.class);
		EasyMock.replay(modelServiceMock);
		classUnderTest.setModelService(modelServiceMock);


		final BaseSiteService baseSiteServiceMock = EasyMock.createNiceMock(BaseSiteService.class);
		baseSiteServiceMock.setCurrentBaseSite(EasyMock.anyObject(BaseSiteModel.class), EasyMock.anyBoolean());
		EasyMock.expectLastCall();
		EasyMock.replay(baseSiteServiceMock);
		classUnderTest.setBaseSiteService(baseSiteServiceMock);

		final SourcingService sourcingServiceMock = EasyMock.createNiceMock(SourcingService.class);
		sourcingServiceMock.callRestServiceAndPersistResult(order);
		EasyMock.expectLastCall().andThrow(new CARBackendDownException());
		EasyMock.replay(sourcingServiceMock);
		classUnderTest.setSourcingService(sourcingServiceMock);



		assertEquals(Transition.NOK, classUnderTest.executeAction(process));
	}

	@Test
	/** Should test successful CheckSAPOaaOrderAction (Transition.OK). */
	public void testSuccess()
	{
		final OrderProcessModel process = new OrderProcessModel();

		final OrderModel order = new OrderModel();
		process.setOrder(order);

		final ModelService modelServiceMock = EasyMock.createNiceMock(ModelService.class);
		EasyMock.replay(modelServiceMock);
		classUnderTest.setModelService(modelServiceMock);

		final SourcingService sourcingServiceMock = EasyMock.createNiceMock(SourcingService.class);
		classUnderTest.setSourcingService(sourcingServiceMock);

		final BaseSiteService baseSiteServiceMock = EasyMock.createNiceMock(BaseSiteService.class);
		classUnderTest.setBaseSiteService(baseSiteServiceMock);

		assertEquals(Transition.OK, classUnderTest.executeAction(process));
	}

}
