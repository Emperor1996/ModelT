/*****************************************************************************
 Interface:        ISCEConfigurationService
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap;

import de.hybris.platform.sap.core.configuration.model.SAPHTTPDestinationModel;


/**
 * Interface for the ISCE Configuration service.
 */
public interface ISCEConfigurationService
{
	/**
	 * Returns the HTTP destination model for CAR OData calls
	 *
	 * @return the HTTP destination model to use for calling the CAR OData service
	 */
	public SAPHTTPDestinationModel getCARHttpDestination();

	/**
	 * Returns the HTTP destination model for yMkt OData calls
	 *
	 * @return the HTTP destination model to use for calling the yMkt OData service
	 */
	public SAPHTTPDestinationModel getYMktHttpDestination();

	/**
	 * Returns the SAP Client for CAR OData calls
	 *
	 * @return String the SAP Client to use for CAR OData calls
	 */
	public String getCARSAPClient();

	/**
	 * Returns the Name of the CAR OData service
	 *
	 * @return String the name of the CAR OData service
	 */
	public String getCARServiceName();

	/**
	 * Returns a list of comma separated Oder Channels, that will be interpreted as POS transactions
	 *
	 * @return String the list of comma separated Oder Channels for POS transactions
	 */
	public String getCARPOSOrderChannels();

	/**
	 * Returns a list of comma separated Oder Channels, that will be interpreted as Online transactions
	 *
	 * @return String the list of comma separated Oder Channels for Online transactions
	 */
	public String getCAROnlineOrderChannels();

	/**
	 * Returns the maximum number of transactiopns to be displayed in purchase history.
	 *
	 * @return Integer the maximum number of transactiopns to be displayed in purchase history.
	 */
	public Integer getCARMaxNumberTransactionsPurchaseHistory();
}
