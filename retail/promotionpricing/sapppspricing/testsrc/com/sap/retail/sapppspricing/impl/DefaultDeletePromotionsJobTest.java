/*****************************************************************************
Class: DefaultDeletePromotionsJobTest
 
@Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 
*****************************************************************************/
 
package com.sap.retail.sapppspricing.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.ppengine.dataaccess.common.util.DBServiceAccessor;
import com.sap.ppengine.dataaccess.promotion.common.service.PromotionServiceInternal;
import com.sap.retail.sapppspricing.model.DeletePromotionsCronJobModel;


@SuppressWarnings("javadoc")
@UnitTest
public class DefaultDeletePromotionsJobTest
{
	private DefaultDeletePromotionsJob cut;
	@Mock
	private DBServiceAccessor dbServiceAccessor;
	@Mock
	private PromotionServiceInternal promoService;

	@Before
	public void setUp()
	{
		cut = new DefaultDeletePromotionsJob();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSetGetDbServiceAccessor() throws Exception
	{
		assertNull(cut.getDbServiceAccessor());
		cut.setDbServiceAccessor(dbServiceAccessor);
		assertSame(dbServiceAccessor, cut.getDbServiceAccessor());
	}

	@Test
	public void testDaysBeforeNow0() throws Exception
	{
		final LocalDateTime now = LocalDateTime.now();
		final Timestamp then = cut.daysBeforeNow(now, 0);
		assertEquals(then, Timestamp.valueOf(now));
	}

	@Test
	public void testDaysBeforeNow1() throws Exception
	{
		final LocalDateTime now = LocalDateTime.now();
		final Timestamp then = cut.daysBeforeNow(now, 1);
		assertTrue(then.before(Timestamp.valueOf(now)));
	}

	@Test
	public void testDaysBeforeNowNegative() throws Exception
	{
		final LocalDateTime now = LocalDateTime.now();
		final Timestamp then = cut.daysBeforeNow(now, -1);
		assertTrue(Timestamp.valueOf(now).before(then));
	}

	@Test
	public void testPerformFine() throws Exception
	{
		final DeletePromotionsCronJobModel cronJobModel = new DeletePromotionsCronJobModel();
		cronJobModel.setDaysSinceExpiry(100);
		cronJobModel.setDeleteActivePromotions(Boolean.TRUE);
		Mockito.when(dbServiceAccessor.getPromotionService()).thenReturn(promoService);
		Mockito.when(promoService.deletePromotions(Mockito.any(), Mockito.eq(Boolean.TRUE))).thenReturn(27);
		cut.setDbServiceAccessor(dbServiceAccessor);
		final PerformResult perform = cut.perform(cronJobModel);
		assertEquals(CronJobResult.SUCCESS, perform.getResult());
		assertEquals(CronJobStatus.FINISHED, perform.getStatus());
	}

	@Test
	public void testPerformError() throws Exception
	{
		final DeletePromotionsCronJobModel cronJobModel = new DeletePromotionsCronJobModel();
		cronJobModel.setDaysSinceExpiry(100);
		cronJobModel.setDeleteActivePromotions(Boolean.TRUE);
		Mockito.when(dbServiceAccessor.getPromotionService()).thenReturn(promoService);
		Mockito.when(promoService.deletePromotions(Mockito.any(), Mockito.eq(Boolean.TRUE))).thenThrow(new RuntimeException());
		cut.setDbServiceAccessor(dbServiceAccessor);
		final PerformResult perform = cut.perform(cronJobModel);
		assertEquals(CronJobResult.ERROR, perform.getResult());
		assertEquals(CronJobStatus.ABORTED, perform.getStatus());
	}
	
	@Test
	public void testPerformError2() throws Exception
	{
		final DeletePromotionsCronJobModel cronJobModel = new DeletePromotionsCronJobModel();
		cronJobModel.setDaysSinceExpiry(-1);
		cronJobModel.setDeleteActivePromotions(Boolean.TRUE);
		Mockito.when(dbServiceAccessor.getPromotionService()).thenReturn(promoService);
		Mockito.when(promoService.deletePromotions(Mockito.any(), Mockito.eq(Boolean.TRUE))).thenThrow(new RuntimeException());
		cut.setDbServiceAccessor(dbServiceAccessor);
		final PerformResult perform = cut.perform(cronJobModel);
		assertEquals(CronJobResult.ERROR, perform.getResult());
		assertEquals(CronJobStatus.ABORTED, perform.getStatus());
	}
	
}
