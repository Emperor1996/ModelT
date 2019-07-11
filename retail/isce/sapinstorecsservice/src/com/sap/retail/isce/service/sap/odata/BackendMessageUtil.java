/*****************************************************************************
 Interface:        BackendMessageUtil.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/

package com.sap.retail.isce.service.sap.odata;

import java.util.List;

import com.sap.retail.isce.service.sap.odata.BackendMessage;


/**
 * Utility class for SAP BAPI messages.
 */
public interface BackendMessageUtil
{
	/**
	 * Checks whether the list of messages contains at least one error message
	 *
	 * @param messages
	 *           list of messages to check
	 * @return <code>true</code>, only if the provided list of messages contains at lest one message with severity
	 *         {@link Severity#ERROR}.
	 */
	public boolean hasErrorMessage(final List<BackendMessage> messages);

	/**
	 * merges two list of messages into one list
	 *
	 * @param m1
	 *           message list to merge
	 * @param m2
	 *           message list to merge
	 * @return new list, containing all messages from list m1 and list m2
	 */
	public List<BackendMessage> mergeMessageLists(final List<BackendMessage> m1, final List<BackendMessage> m2);

}
