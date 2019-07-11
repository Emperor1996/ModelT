/*****************************************************************************
 Class:        InstorecsfacadeConstants
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.facade.constants;

import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;


/**
 * Global class for all Instorecsfacade constants. You can add global constants for your extension into this class.
 */
public final class InstorecsfacadeConstants extends GeneratedInstorecsfacadeConstants
{
	public static final String EXTENSIONNAME = "instorecsfacade";

	public static final String AGENTUID = "agentUID";
	public static final String AGENTSTORE = "agentPointOfServiceData";
	public static final String AGENTSTORE_MODEL = InstorecsfacadeConstants.class.getName() + "agentPointOfServiceModel";
	public static final String AS_AGENT_ID_SESSION_ATTRIBUTE = AssistedServiceFacade.class.getName() + "_as_agent_id";

	private InstorecsfacadeConstants()
	{
		//empty to avoid instantiating this constant class
	}

	// implement here constants used by this extension
}
