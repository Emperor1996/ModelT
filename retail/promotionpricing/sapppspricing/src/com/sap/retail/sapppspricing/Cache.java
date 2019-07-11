/*****************************************************************************
Class: Cache
 
@Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 
*****************************************************************************/
 
/**
 *
 */
package com.sap.retail.sapppspricing;

/**
 * Generic interface for cache wrapper
 *
 * @param <K>
 *           type of the object holding key information for the cached entry
 * @param <C>
 *           type of the cached entry
 */
public interface Cache<C, K>
{
	/**
	 * read from cache
	 *
	 * @param keyHolder
	 * @return cached entry or null
	 */
	C read(K keyHolder);

	/**
	 * write into cache
	 *
	 * @param keyHolder
	 * @param entry
	 *           entry to to be cached
	 */
	void write(K keyHolder, C entry);
}
