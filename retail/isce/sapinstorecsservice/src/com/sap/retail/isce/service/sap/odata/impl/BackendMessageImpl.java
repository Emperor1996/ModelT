/*****************************************************************************
 Class:        BackendMessageImpl.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.service.sap.odata.impl;

import com.sap.retail.isce.service.sap.odata.BackendMessage;


public class BackendMessageImpl implements BackendMessage
{


	private Severity severity;
	private String messageClass;
	private int number;
	private String text;


	public BackendMessageImpl()
	{
		super();
	}

	/**
	 * @param severity
	 * @param messageClass
	 * @param number
	 * @param text
	 */
	public BackendMessageImpl(final Severity severity, final String messageClass, final int number, final String text)
	{
		super();
		this.severity = severity;
		this.messageClass = messageClass;
		this.number = number;
		this.text = text;
	}


	@Override
	public String toString()
	{
		final StringBuilder builder = new StringBuilder();
		builder.append(severity);
		builder.append(" (");
		builder.append(messageClass);
		builder.append("/");
		builder.append(number);
		builder.append(") ");
		builder.append(text);
		return builder.toString();
	}

	/**
	 * @return the messageClass
	 */
	@Override
	public String getMessageClass()
	{
		return messageClass;
	}

	/**
	 * @return the number
	 */
	@Override
	public int getNumber()
	{
		return number;
	}

	/**
	 * @return the text
	 */
	@Override
	public String getText()
	{
		return text;
	}

	/**
	 * @return the severity
	 */
	@Override
	public Severity getSeverity()
	{
		return severity;
	}

	/**
	 * @param severity
	 *           the severity to set
	 */
	public void setSeverity(final Severity severity)
	{
		this.severity = severity;
	}

	/**
	 * @param messageClass
	 *           the messageClass to set
	 */
	public void setMessageClass(final String messageClass)
	{
		this.messageClass = messageClass;
	}

	/**
	 * @param number
	 *           the number to set
	 */
	public void setNumber(final int number)
	{
		this.number = number;
	}

	/**
	 * @param text
	 *           the text to set
	 */
	public void setText(final String text)
	{
		this.text = text;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((messageClass == null) ? 0 : messageClass.hashCode());
		result = prime * result + number;
		result = prime * result + ((severity == null) ? 0 : severity.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final BackendMessageImpl other = (BackendMessageImpl) obj;
		if (messageClass == null)
		{
			if (other.messageClass != null)
			{
				return false;
			}
		}
		else if (!messageClass.equals(other.messageClass))
		{
			return false;
		}
		if (number != other.number)
		{
			return false;
		}
		if (severity != other.severity)
		{
			return false;
		}
		return true;
	}


}
