/*****************************************************************************
 Class:        DataContainerPropertyDefaultImpl
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.sap.retail.isce.container.DataContainerProperty;
import com.sap.retail.isce.container.DataContainerPropertyLevel;
import com.sap.retail.isce.exception.DataContainerPropertyLevelException;
import com.sap.retail.isce.exception.DataContainerRuntimeException;
import com.sap.security.core.server.csi.XSSEncoder;


public abstract class DataContainerPropertyDefaultImpl implements DataContainerProperty
{
	private static final Logger LOG = Logger.getLogger(DataContainerPropertyDefaultImpl.class.getName());

	protected Object value;
	protected String unitSingular;
	protected String unitPlural;
	protected List<DataContainerPropertyLevel> levels = this.getEmptyLevelList();
	protected DataContainerPropertyLevel correspondingLevel = null;

	protected static final String MSG_CAST_EX = "Object value could not be correctly casted.";
	protected static final String MSG_PROP_LEVEL_EX_THROWN = " DataContainerPropertyLevelException thrown.";


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerProperty#getValue()
	 */
	@Override
	public Object getValue()
	{
		return this.value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerProperty#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(final Object value)
	{
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerProperty#getLevels()
	 */
	@Override
	public List<DataContainerPropertyLevel> getLevels()
	{
		return this.levels;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerProperty#createLevels(java.util.List)
	 */
	@Override
	public List<DataContainerPropertyLevel> createLevels(final List<Comparable> levelBoundaryPairs)
			throws DataContainerPropertyLevelException
	{
		DataContainerPropertyLevel level;
		String levelUnit;
		final List<DataContainerPropertyLevel> propertyLevels = new ArrayList();

		this.levels = propertyLevels;

		if (levelBoundaryPairs != null && !levelBoundaryPairs.isEmpty())
		{
			final int numBorders = levelBoundaryPairs.size();
			for (int i = 0, j = 1; i < numBorders; i = i + 2, j++)
			{
				if (i + 1 == numBorders)
				{
					throw new DataContainerPropertyLevelException("Missing high level value.");
				}
				levelUnit = this.determineUnit(levelBoundaryPairs.get(i + 1));

				level = new DataContainerPropertyLevelDefaultImpl(Integer.toString(j), levelBoundaryPairs.get(i),
						levelBoundaryPairs.get(i + 1), levelUnit, "");
				propertyLevels.add(level);
			}
		}
		return this.levels;
	}

	/**
	 *
	 * @param objectValue
	 *           the value for which the unit should be determined
	 * @return the singular or the plural unit depending of the objectValue
	 * @throws DataContainerPropertyLevelException
	 */
	public abstract String determineUnit(final Object objectValue) throws DataContainerPropertyLevelException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerProperty#encodeHTML()
	 */
	@Override
	public void encodeHTML()
	{
		this.unitPlural = encodeHTML(this.unitPlural);
		this.unitSingular = encodeHTML(this.unitSingular);

		for (final DataContainerPropertyLevel level : this.levels)
		{
			level.setDescription(encodeHTML(level.getDescription()));
			level.setUnit(encodeHTML(level.getUnit()));
			level.setLevelFlag(encodeHTML(level.getLevelFlag()));
			if (level.getHighValue() instanceof String)
			{
				level.setHighValue(encodeHTML((String) level.getHighValue()));
			}
			if (level.getLowValue() instanceof String)
			{
				level.setLowValue(encodeHTML((String) level.getLowValue()));
			}
			if (level.getAdaptedUIHighValue() instanceof String)
			{
				level.setAdaptedUIHighValue(encodeHTML((String) level.getAdaptedUIHighValue()));
			}
			if (level.getAdaptedUILowValue() instanceof String)
			{
				level.setAdaptedUILowValue(encodeHTML((String) level.getAdaptedUILowValue()));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerProperty#getCorrespondingLevel()
	 */
	@Override
	public DataContainerPropertyLevel getCorrespondingLevel()
	{
		return this.correspondingLevel;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerProperty#determineCorrespondingLevel()
	 */
	@Override
	public void determineCorrespondingLevel() throws DataContainerPropertyLevelException
	{
		this.correspondingLevel = this.determineCorrespondingLevel((Comparable) this.value);
	}

	/**
	 * Determines the level that corresponds to the value
	 *
	 * @param value
	 *           the value that is compared with the level borders
	 * @return the level that contains the value
	 * @throws DataContainerPropertyLevelException
	 */
	public DataContainerPropertyLevel determineCorrespondingLevel(final Comparable value)
			throws DataContainerPropertyLevelException
	{
		boolean correspondingLevelFound;

		if (this.levels == null || this.levels.isEmpty() || value == null)
		{
			return null;
		}

		for (final DataContainerPropertyLevel level : this.levels)
		{
			correspondingLevelFound = this.isCorrespondingLevel(level, value);
			if (correspondingLevelFound)
			{
				return level;
			}
		}
		return null;
	}

	/**
	 * Checks whether the value matches to the given level. If the low/high value is missing then the low/high value
	 * matches.
	 *
	 * @param level
	 *           the level to compare with the value
	 * @param value
	 *           the value to compare with the high and low value of the level
	 * @return true if the value matches otherwise false
	 * @throws DataContainerPropertyLevelException
	 */
	protected boolean isCorrespondingLevel(final DataContainerPropertyLevel level, final Comparable value)
			throws DataContainerPropertyLevelException
	{
		if (value == null || level == null)
		{
			return false;
		}
		if (level.getLowValue() == null && level.getHighValue() == null)
		{
			return true;
		}
		checkDataTypes(level, value, value.getClass());
		return isValueInLevelRange(level, value);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.sap.retail.isce.container.impl.DataContainerPropertyDefaultImpl#isValueInLevelRange(com.sap.retail.isce.container
	 * .DataContainerPropertyLevel, java.lang.Object)
	 */
	protected boolean isValueInLevelRange(final DataContainerPropertyLevel level, final Comparable value)
	{
		if (level.getLowValue() == null && level.getHighValue().compareTo(value) >= 0)
		{
			return true;
		}
		if (level.getHighValue() == null && level.getLowValue().compareTo(value) <= 0)
		{
			return true;
		}
		if (isValueInClosedLevelRange(level, value))
		{
			return true;
		}
		return false;
	}

	/**
	 * Checks whether the value is inside a closed level. Level and value must not be null.
	 *
	 * @param level
	 *           the level to be checked
	 * @param value
	 *           the value to compare with the level borders
	 * @return true if value is inside level range, false otherwise
	 */
	protected boolean isValueInClosedLevelRange(final DataContainerPropertyLevel level, final Object value)
	{
		return level.getLowValue() != null && level.getHighValue() != null && level.getLowValue().compareTo(value) <= 0
				&& level.getHighValue().compareTo(value) >= 0;
	}

	/**
	 * Provides a new empty list of levels.
	 *
	 * @return the empty level list.
	 */
	protected List<DataContainerPropertyLevel> getEmptyLevelList()
	{
		return new ArrayList<>(0);
	}

	/**
	 * Security encoding of a given input.
	 *
	 * @param input
	 *           the input to be encoded
	 * @return the encoded input
	 */
	protected String encodeHTML(final String input)
	{
		try
		{
			return encodeHTMLInner(input);
		}
		catch (final UnsupportedEncodingException e)
		{
			LOG.error("SECURITY: UnsupportedEncodingException occured during HTML Encoding");
			throw new DataContainerRuntimeException("HTML encoding exception occured", e);
		}
	}

	/**
	 * /** Security encoding of a given input.
	 *
	 * @param input
	 *           the input to be encoded
	 * @return the encoded input
	 * @throws UnsupportedEncodingException
	 */
	protected String encodeHTMLInner(final String input) throws UnsupportedEncodingException
	{
		return XSSEncoder.encodeHTML(input);
	}

	/**
	 * Checks whether the level and value variables that will be compared are instances are of the correct type. The
	 * level must not be null.
	 *
	 * @param level
	 * @param value
	 * @throws DataContainerPropertyLevelException
	 *            if one of the variables is not of the given type
	 */
	protected void checkDataTypes(final DataContainerPropertyLevel level, final Object value, final Class clazz)
			throws DataContainerPropertyLevelException
	{
		if (level == null)
		{
			LOG.error("Level is null. So full type check cayn't be done. DataContainerPropertyLevelException thrown.");
			throw new DataContainerPropertyLevelException("Level is null. So full type check can't be done. ");
		}

		if ((level.getLowValue() != null && !(clazz.isInstance(level.getLowValue())))
				|| (level.getHighValue() != null && !(clazz.isInstance(level.getHighValue()))))
		{
			LOG.error("Level high and/or low value is not of suitable data type. DataContainerPropertyLevelException thrown.");
			throw new DataContainerPropertyLevelException("Level high and/or low value is not of suitable data type");
		}
		if (!(clazz.isInstance(value)))
		{
			LOG.error("Property value is not of suitable data type. DataContainerPropertyLevelException thrown.");
			throw new DataContainerPropertyLevelException("Property value is not of suitable data type");
		}
	}

	/**
	 * Logs the ClassCastException, wraps it in a new DataContainerPropertyLevelException and throws this new exception.
	 *
	 * @param log
	 *           the logger to be used for logging
	 * @param e
	 *           the original exception
	 * @throws DataContainerPropertyLevelException
	 */
	protected void logAndThrowExForCastEx(final Logger log, final ClassCastException e) throws DataContainerPropertyLevelException
	{
		log.error(MSG_CAST_EX + MSG_PROP_LEVEL_EX_THROWN);
		throw new DataContainerPropertyLevelException(MSG_CAST_EX, e);
	}
}
