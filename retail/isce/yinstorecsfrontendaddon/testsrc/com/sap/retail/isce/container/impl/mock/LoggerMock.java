/*****************************************************************************
 Class:        LoggerMock
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl.mock;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


/**
 * Unit test mock for Logger
 */
public class LoggerMock extends Logger
{

	private final List<Object> errorList = new ArrayList();
	private final List<Object> warningList = new ArrayList();
	private final List<Object> debugList = new ArrayList();
	private final List<Object> info = new ArrayList();
	private boolean isDebug = false;
	private boolean isInfo = false;

	/**
	 * @param isDebug
	 *           the isDebug to set
	 */
	public void setDebug(final boolean isDebug)
	{
		this.isDebug = isDebug;
	}

	/**
	 * @param isInfo
	 *           the isInfo to set
	 */
	public void setInfo(final boolean isInfo)
	{
		this.isInfo = isInfo;
	}

	public LoggerMock(final String name)
	{
		super(name);
	}

	@Override
	public boolean isInfoEnabled()
	{
		return isInfo;
	}

	@Override
	public boolean isDebugEnabled()
	{
		return isDebug;
	}

	@Override
	public void error(final Object message)
	{
		this.errorList.add(message);
	}

	@Override
	public void warn(final Object message)
	{
		this.warningList.add(message);
	}

	@Override
	public void error(final Object message, final Throwable t)
	{
		this.errorList.add(message);
	}

	@Override
	public void debug(final Object message)
	{
		this.debugList.add(message);
	}

	@Override
	public void info(final Object message)
	{
		this.info.add(message);
	}

	public void clearAll()
	{
		this.errorList.clear();
		this.warningList.clear();
		this.info.clear();
		this.debugList.clear();
		this.isDebug = false;
		this.isInfo = false;
	}

	/**
	 * @return the error
	 */
	public List<Object> getError()
	{
		return errorList;
	}

	/**
	 * @return the warning
	 */
	public List<Object> getWarning()
	{
		return warningList;
	}

	/**
	 * @return the debug
	 */
	public List<Object> getDebug()
	{
		return debugList;
	}

	/**
	 * @return the info
	 */
	public List<Object> getInfo()
	{
		return info;
	}

}
