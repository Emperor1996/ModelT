/*****************************************************************************
 Interface:        DataContainerPropertyLevel
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container;

public interface DataContainerPropertyLevel
{
	/**
	 * @return the levelFlag
	 */
	public String getLevelFlag();

	/**
	 * @param levelFlag
	 *           the levelFlag to set
	 */
	public void setLevelFlag(String levelFlag);

	/**
	 * @return the lowValue
	 */
	public Comparable getLowValue();

	/**
	 * @param lowValue
	 *           the lowValue to set
	 */
	public void setLowValue(final Comparable lowValue);

	/**
	 * @return the highValue
	 */
	public Comparable getHighValue();

	/**
	 * @param highValue
	 *           the highValue to set
	 */
	public void setHighValue(Comparable highValue);

	/**
	 * @return the UI adapted low value
	 */
	public Object getAdaptedUILowValue();

	/**
	 * @param lowValue
	 *           the UI adapted low value to set
	 */
	public void setAdaptedUILowValue(final Object lowValue);

	/**
	 * @return the high value adapted for UI
	 */
	public Object getAdaptedUIHighValue();

	/**
	 * @param highValue
	 *           the UI adapted high value to set
	 */
	public void setAdaptedUIHighValue(Object highValue);

	/**
	 * @return the unit
	 */
	public String getUnit();

	/**
	 * @param unit
	 *           the unit to set
	 */
	public void setUnit(String unit);

	/**
	 * @return the description
	 */
	public String getDescription();

	/**
	 * @param description
	 *           the description to set
	 */
	public void setDescription(String description);

}
