/*****************************************************************************
 Class:        BackendMessageUtilImpl.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.odata.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sap.retail.isce.service.sap.odata.BackendMessage;
import com.sap.retail.isce.service.sap.odata.BackendMessage.Severity;
import com.sap.retail.isce.service.sap.odata.BackendMessageUtil;


public class BackendMessageUtilImpl implements BackendMessageUtil
{

	@Override
	public boolean hasErrorMessage(final List<BackendMessage> messages)
	{
		boolean hasError = false;
		if (messages != null)
		{
			for (final BackendMessage message : messages)
			{
				if (Severity.ERROR.equals(message.getSeverity()))
				{
					hasError = true;
					break;
				}
			}
		}
		return hasError;
	}


	@Override
	public List<BackendMessage> mergeMessageLists(final List<BackendMessage> m1, final List<BackendMessage> m2)
	{
		List<BackendMessage> newList;
		if (m1.isEmpty() && m2.isEmpty())
		{
			newList = Collections.emptyList();
		}
		else if (m1.isEmpty())
		{
			newList = m2;
		}
		else if (m2.isEmpty())
		{
			newList = m1;
		}
		else
		{
			newList = new ArrayList<>(m1.size() + m2.size());
			newList.addAll(m1);
			newList.addAll(m2);
		}
		return newList;
	}
}
