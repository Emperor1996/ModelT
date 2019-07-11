/*****************************************************************************
 Interface:        DataContainerSmart
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container;

import com.sap.retail.isce.exception.DataContainerServiceException;


/**
 * Smart container for handling data.
 *
 */
public interface DataContainerSmart
{
	/**
	 * Read the data needed for the container.
	 *
	 * @throws DataContainerServiceException
	 *            exception thrown in error case
	 */
	public void readData() throws DataContainerServiceException;
}
