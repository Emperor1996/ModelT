/*
 *
 *  [y] hybris Platform
 *
 *  Copyright (c) 2018 SAP SE or an SAP affiliate company. All rights reserved.
 *
 *  This software is the confidential and proprietary information of SAP
 *  ("Confidential Information"). You shall not disclose such Confidential
 *  Information and shall use it only in accordance with the terms of the
 *  license agreement you entered into with SAP.
 * /
 */
package com.sap.hybris.crm.sapcrmcomplaintexchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.Utilities;

/**
 * Demonstration of how to write a standalone application that can be run
 * directly from within eclipse or from the commandline.<br>
 * To run this from commandline, just use the following command:<br>
 * <code>
 * java -jar bootstrap/bin/ybootstrap.jar "new com.sap.hybris.crm.sapcrmcomplaintexchange.SapcrmcomplaintexchangeStandalone().run();"
 * </code> From eclipse, just run as Java Application. Note that you maybe need
 * to add all other projects like ext-commerce, ext-pim to the Launch
 * configuration classpath.
 */
public class SapcrmcomplaintexchangeStandalone {
    private static final Logger LOG = LoggerFactory.getLogger(SapcrmcomplaintexchangeStandalone.class);

    /**
     * Main class to be able to run it directly as a java program.
     *
     * @param args
     *            the arguments from commandline
     */
    public static void main(final String[] args) {
        new SapcrmcomplaintexchangeStandalone().run();
    }

    public void run() {
        Registry.activateStandaloneMode();
        Registry.activateMasterTenant();

        final JaloSession jaloSession = JaloSession.getCurrentSession();
        LOG.info("Session ID: " + jaloSession.getSessionID()); // NOPMD
        LOG.info("User: " + jaloSession.getUser()); // NOPMD
        Utilities.printAppInfo();

        RedeployUtilities.shutdown();
    }
}