/*****************************************************************************
    Class:        Saparticleb2caddonManager.java
    Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 
*****************************************************************************/
package com.sap.retail.commercesuite.saparticleb2caddon.jalo;

import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.extension.ExtensionManager;

import org.apache.log4j.Logger;

import com.sap.retail.commercesuite.saparticleb2caddon.constants.Saparticleb2caddonConstants;

@SuppressWarnings("PMD")
public class Saparticleb2caddonManager extends GeneratedSaparticleb2caddonManager
{
	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger( Saparticleb2caddonManager.class.getName() );

	public static final Saparticleb2caddonManager getInstance()
	{
		final ExtensionManager em = JaloSession.getCurrentSession().getExtensionManager();
		return (Saparticleb2caddonManager) em.getExtension(Saparticleb2caddonConstants.EXTENSIONNAME);
	}

}
