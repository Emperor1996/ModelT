/**
 *
 */
package com.sap.retail.oaa.orderexchange.jobs;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.sap.orderexchange.outbound.OrderExchangeRepair;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import com.sap.retail.oaa.orderexchange.constants.ActionIds;
import com.sap.retail.oaa.orderexchange.constants.SapoaaorderexchangeConstants;




/**
 * @author SAP
 *
 */
public class OaaOrderExchangeRepairJobTest
{


	private final OaaOrderExchangeRepairJob classUnderTest = new OaaOrderExchangeRepairJob();

	@SuppressWarnings("PMD.MethodNamingConventions")
	@Test
	public void testPerform_noJobsToRepair()
	{

		final CronJobModel job = null;

		final OrderExchangeRepair orderExchangeRepairMock = EasyMock.createMock(OrderExchangeRepair.class);
		final List<OrderProcessModel> emptyOrderProcessModel = new ArrayList<OrderProcessModel>();
		EasyMock.expect(orderExchangeRepairMock.findAllProcessModelsToRepair(SapoaaorderexchangeConstants.ORDER_PROCESS_NAME,
				OaaOrderExchangeRepairJob.STATUS_CODE_ORDER_CHECK_FAILED)).andReturn(emptyOrderProcessModel);
		EasyMock.replay(orderExchangeRepairMock);
		classUnderTest.setOrderExchangeRepair(orderExchangeRepairMock);


		Assert.assertEquals(CronJobResult.SUCCESS, classUnderTest.perform(job).getResult());



	}

	@SuppressWarnings("PMD.MethodNamingConventions")
	@Test
	public void testPerform_jobsToRepair()
	{

		final CronJobModel job = null;

		final OrderExchangeRepair orderExchangeRepairMock = EasyMock.createMock(OrderExchangeRepair.class);
		final List<OrderProcessModel> emptyOrderProcessModel = new ArrayList<OrderProcessModel>();
		final OrderProcessModel firstProcess = new OrderProcessModel();
		emptyOrderProcessModel.add(firstProcess);

		EasyMock.expect(orderExchangeRepairMock.findAllProcessModelsToRepair(SapoaaorderexchangeConstants.ORDER_PROCESS_NAME,
				OaaOrderExchangeRepairJob.STATUS_CODE_ORDER_CHECK_FAILED)).andReturn(emptyOrderProcessModel);
		EasyMock.replay(orderExchangeRepairMock);
		classUnderTest.setOrderExchangeRepair(orderExchangeRepairMock);

		final BusinessProcessService businessProcessServiceMock = EasyMock.createMock(BusinessProcessService.class);
		businessProcessServiceMock.restartProcess(firstProcess, ActionIds.CHECK_SAP_ORDER);
		EasyMock.expectLastCall();
		EasyMock.replay(businessProcessServiceMock);
		classUnderTest.setBusinessProcessService(businessProcessServiceMock);


		Assert.assertEquals(CronJobResult.SUCCESS, classUnderTest.perform(job).getResult());



	}

}
