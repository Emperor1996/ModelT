/*****************************************************************************
    Class:        SaparticlesearchJUnitTestSuite.java
    Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 
*****************************************************************************/
package com.sap.retail.commercesuite.saparticlesearch.suite;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.sap.retail.commercesuite.saparticlesearch.provider.StructuredArticleComponentKeywordsValueResolverTest;
import com.sap.retail.commercesuite.saparticlesearch.provider.StructuredArticleComponentNamesValueResolverTest;


/**
 * JUnit Testsuite for saparticlesearch extension.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
{ StructuredArticleComponentNamesValueResolverTest.class, StructuredArticleComponentKeywordsValueResolverTest.class })
public class SaparticlesearchJUnitTestSuite
{
	// no coding needed
}
