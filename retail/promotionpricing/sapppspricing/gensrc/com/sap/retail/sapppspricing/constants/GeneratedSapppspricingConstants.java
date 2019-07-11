/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Jan 31, 2019 8:15:31 AM                     ---
 * ----------------------------------------------------------------
 */
package com.sap.retail.sapppspricing.constants;

/**
 * @deprecated since ages - use constants in Model classes instead
 */
@Deprecated
@SuppressWarnings({"unused","cast","PMD"})
public class GeneratedSapppspricingConstants
{
	public static final String EXTENSIONNAME = "sapppspricing";
	public static class TC
	{
		public static final String DELETEPRICESCRONJOB = "DeletePricesCronJob".intern();
		public static final String DELETEPROMOTIONSCRONJOB = "DeletePromotionsCronJob".intern();
		public static final String INTERFACEVERSION = "interfaceVersion".intern();
		public static final String REMLOCALPPS = "remLocalPPS".intern();
	}
	public static class Attributes
	{
		public static class SAPConfiguration
		{
			public static final String SAPPPS_ACTIVE = "sappps_active".intern();
			public static final String SAPPPS_BUSINESSUNITID = "sappps_businessUnitId".intern();
			public static final String SAPPPS_CACHECATALOGPRICES = "sappps_cacheCatalogPrices".intern();
			public static final String SAPPPS_HTTPDESTINATION = "sappps_HttpDestination".intern();
			public static final String SAPPPS_INTERFACEVERSION = "sappps_interfaceVersion".intern();
			public static final String SAPPPS_LOCALREMOTE = "sappps_localRemote".intern();
		}
		public static class SAPHTTPDestination
		{
			public static final String SAPPPS_CONFIGURATIONS = "sappps_Configurations".intern();
		}
	}
	public static class Enumerations
	{
		public static class InterfaceVersion
		{
			public static final String VERSION10 = "VERSION10".intern();
			public static final String VERSION20 = "VERSION20".intern();
		}
		public static class RemLocalPPS
		{
			public static final String LOCALPPS = "LocalPPS".intern();
			public static final String REMOTEPPS = "RemotePPS".intern();
		}
	}
	public static class Relations
	{
		public static final String SAPHTTPDESTINATIONFORPPS = "SapHttpDestinationForPPS".intern();
	}
	
	protected GeneratedSapppspricingConstants()
	{
		// private constructor
	}
	
	
}
