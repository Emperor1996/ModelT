/*****************************************************************************
 Class:        UnitTestBase
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce;

import java.util.Collections;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Before;


/**
 * Base class for unit testing in the area of ISCE on service layer.
 *
 */
public class UnitTestBase
{
	/**
	 * Use this method to suppress logging entries of methods during test execution. This might help to avoid confusions
	 * in the console log during test execution.<br/>
	 * So logging statements in tested methods like<br/>
	 * <b>LOG.error("Missing containerName in instance of class " ...</b><br/>
	 * will not be shown in console log during test execution.
	 *
	 */
	protected void suppressLogging()
	{
		final List<Logger> loggers = Collections.<Logger> list(LogManager.getCurrentLoggers());
		loggers.add(LogManager.getRootLogger());
		for (final Logger logger : loggers) // NOSONAR
		{
			logger.setLevel(Level.OFF);
		}

	}


	/**
	 * Provides base functionality like
	 * <ul>
	 * <li>suppression of logging (@see suppressLogging())</li>
	 * </ul>
	 */
	@Before
	public void setUp()
	{
		this.suppressLogging();
	}

}
