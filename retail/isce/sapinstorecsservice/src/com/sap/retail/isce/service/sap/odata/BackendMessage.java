/*****************************************************************************
 Interface:        BackendMessage.java
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/

package com.sap.retail.isce.service.sap.odata;

/**
 * Value Object representing a SAP BAPI message.
 */
public interface BackendMessage
{

	/**
	 * Simplified Severities of SAP BAPI Messages:
	 * <ul>
	 * <li>{@link Severity#ERROR}</li>
	 * <li>{@link Severity#WARNING}</li>
	 * <li>{@link Severity#INFO}</li>
	 * </ul>
	 *
	 */
	public enum Severity
	{
		/**
		 * SAP back message of type 'A' (Abort) or 'E' (Error)
		 */
		ERROR,
		/**
		 * SAP back message of type 'W' (Warning)
		 */
		WARNING,
		/**
		 * SAP back message of type 'S' (Success) or 'I' (Info)
		 */
		INFO;

		public static Severity fromBAPI(final char c)
		{
			final Severity severity;
			switch (Character.toUpperCase(c))
			{
				case 'A':
				case 'E':
					severity = Severity.ERROR;
					break;
				case 'W':
					severity = Severity.WARNING;
					break;
				case 'S':
				case 'I':
					severity = Severity.INFO;
					break;
				default:
					throw new IllegalArgumentException("'" + c + "' is not a valid BAPI severity (A|E|W|S|I)");
			}

			return severity;
		}
	}

	/**
	 * @return {@link Severity} of the message
	 */
	public abstract Severity getSeverity();

	/**
	 * @return message text
	 */
	public abstract String getText();

	/**
	 * @return message number
	 */
	public abstract int getNumber();

	/**
	 * @return message class
	 */
	public abstract String getMessageClass();



}
