/*****************************************************************************
 Interface:        DataContainer
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;


/**
 * Container for handling data.
 *
 */
public interface DataContainer
{
	/**
	 * @return the container mane
	 */
	public String getContainerName();

	/**
	 * @return the localized container mane
	 */
	public String getLocalizedContainerName();

	/**
	 * Set data container context.
	 *
	 * @param dataContainerContext
	 *           the context to set
	 */
	public void setDataContainerContext(DataContainerContext dataContainerContext);

	/**
	 * Sets the component model.
	 *
	 * @param cmsComponentModel
	 *           the component model to set
	 */
	public void setCMSComponentModel(AbstractCMSComponentModel cmsComponentModel);

	/**
	 * Determines data that depends on the CMS component model
	 */

	public void determineDataForCMSComponent();

	/**
	 * Returns the name of the context parameter used for this DataContainer.
	 *
	 * @return the context parameter attribute name
	 */
	public String getContainerContextParamName();

	/**
	 * Sets the error state of the container.
	 *
	 * @param errorState
	 *           the errorState to set
	 */
	public void setErrorState(Boolean errorState);

	/**
	 * Returns the error state.
	 *
	 * @return the errorState
	 */
	public Boolean getErrorState();

	/**
	 * Set data if data container is in error state.
	 */
	public abstract void setDataInErrorState();

	/**
	 * HTML encodes the input string.
	 */
	public abstract void encodeHTML();

	/**
	 * Traces information about the container, if the trace levels is debug.
	 */
	public abstract void traceInformation();
}
