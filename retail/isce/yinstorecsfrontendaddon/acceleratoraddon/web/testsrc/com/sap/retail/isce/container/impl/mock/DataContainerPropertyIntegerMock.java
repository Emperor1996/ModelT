/*****************************************************************************
 Class:        DataContainerPropertyIntegerMock
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl.mock;

import java.util.List;

import com.sap.retail.isce.container.DataContainerPropertyLevel;
import com.sap.retail.isce.container.impl.DataContainerPropertyIntegerDefaultImpl;
import com.sap.retail.isce.exception.DataContainerPropertyLevelException;


/**
 * @author Administrator
 *
 */
public class DataContainerPropertyIntegerMock extends DataContainerPropertyIntegerDefaultImpl
{
	private static final String TEST_STRING_4_ENCODING = "NiceText! §$%&/()=+*#'<>|/*-+;:_,.-µ^°1234567890ßß´`~+*";

	public boolean throwDataContainerPropertyLevelException = false; //NOSONAR

	public DataContainerPropertyIntegerMock(final Integer value, final String unitSingular, final String unitPlural)
	{
		super(value, unitSingular, unitPlural);
	}

	@Override
	public List<DataContainerPropertyLevel> createLevels(final List<Comparable> levelBoundaryPairs)
			throws DataContainerPropertyLevelException
	{
		if (throwDataContainerPropertyLevelException)
		{
			throw new DataContainerPropertyLevelException();
		}
		final List<DataContainerPropertyLevel> levels = super.createLevels(levelBoundaryPairs);
		if (!levels.isEmpty())
		{
			levels.get(0).setUnit(TEST_STRING_4_ENCODING);
		}

		return levels;
	}
}