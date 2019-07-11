/*****************************************************************************
 Class:        DataContainerPropertyLevelDefaultImpl
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import com.sap.retail.isce.container.DataContainerPropertyLevel;


public class DataContainerPropertyLevelDefaultImpl implements DataContainerPropertyLevel
{
	protected String levelFlag;
	protected Comparable lowValue;
	protected Comparable highValue;
	protected Object adaptedUILowValue;
	protected Object adaptedUIHighValue;
	protected String unit;
	protected String description;

	public DataContainerPropertyLevelDefaultImpl(final String levelFlag, final Comparable lowValue, final Comparable highValue,
			final String unit, final String description)
	{
		this.levelFlag = levelFlag;
		this.lowValue = lowValue;
		this.highValue = highValue;
		this.unit = unit;
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.container.DataContainerPropertyLevel#getLevelFlag()
	 */
	@Override
	public String getLevelFlag()
	{
		return this.levelFlag;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.container.DataContainerPropertyLevel#setLevelFlag(java.lang.String)
	 */
	@Override
	public void setLevelFlag(final String levelFlag)
	{
		this.levelFlag = levelFlag;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.container.DataContainerPropertyLevel#getLowValue()
	 */
	@Override
	public Comparable getLowValue()
	{
		return this.lowValue;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerPropertyLevel#setLowValue(java.lang.Comparable)
	 */
	@Override
	public void setLowValue(final Comparable lowValue)
	{
		this.lowValue = lowValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerPropertyLevel#getHighValue()
	 */
	@Override
	public Comparable getHighValue()
	{
		return this.highValue;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.container.DataContainerPropertyLevel#setHighValue(java.lang.Comparable)
	 */
	@Override
	public void setHighValue(final Comparable highValue)
	{
		this.highValue = highValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sap.retail.isce.container.DataContainerPropertyLevel#getDescription()
	 */
	@Override
	public String getDescription()
	{
		return this.description;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.container.DataContainerPropertyLevel#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(final String description)
	{
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.container.DataContainerPropertyLevel#getUIAdaptedLowValue()
	 */
	@Override
	public Object getAdaptedUILowValue()
	{
		return this.adaptedUILowValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.container.DataContainerPropertyLevel#setUIAdaptedLowValue(java.lang.Object)
	 */
	@Override
	public void setAdaptedUILowValue(final Object lowValue)
	{
		this.adaptedUILowValue = lowValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.container.DataContainerPropertyLevel#getUIAdaptedHighValue()
	 */
	@Override
	public Object getAdaptedUIHighValue()
	{
		return this.adaptedUIHighValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.container.DataContainerPropertyLevel#setUIAdaptedHighValue(java.lang.Object)
	 */
	@Override
	public void setAdaptedUIHighValue(final Object highValue)
	{
		this.adaptedUIHighValue = highValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.container.DataContainerPropertyLevel#getUnit()
	 */
	@Override
	public String getUnit()
	{
		return this.unit;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.sap.retail.isce.container.DataContainerPropertyLevel#setUnit(java.lang.String)
	 */
	@Override
	public void setUnit(final String unit)
	{
		this.unit = unit;
	}
}
