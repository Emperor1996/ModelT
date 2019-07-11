/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Jan 30, 2019 3:02:53 PM                     ---
 * ----------------------------------------------------------------
 */
package com.sap.retail.oaa.model.constants;

/**
 * @deprecated since ages - use constants in Model classes instead
 */
@Deprecated
@SuppressWarnings({"unused","cast","PMD"})
public class GeneratedSapoaamodelConstants
{
	public static final String EXTENSIONNAME = "sapoaamodel";
	public static class TC
	{
		public static final String SAPOAA_MODE = "sapoaa_mode".intern();
		public static final String SCHEDULELINE = "ScheduleLine".intern();
	}
	public static class Attributes
	{
		public static class AbstractOrder
		{
			public static final String SAPCARRESERVATION = "sapCarReservation".intern();
		}
		public static class AbstractOrderEntry
		{
			public static final String SAPCARRESERVATION = "sapCarReservation".intern();
			public static final String SAPSOURCE = "sapSource".intern();
			public static final String SAPVENDOR = "sapVendor".intern();
			public static final String SCHEDULELINES = "scheduleLines".intern();
		}
		public static class PointOfService
		{
			public static final String SAPOAA_CACSHIPPINGPOINT = "sapoaa_cacShippingPoint".intern();
		}
		public static class SAPConfiguration
		{
			public static final String SAPOAA_CONSUMERID = "sapoaa_consumerId".intern();
			public static final String SAPOAA_MODE = "sapoaa_mode".intern();
			public static final String SAPOAA_OAAPROFILE = "sapoaa_oaaProfile".intern();
			public static final String SAPOAA_SALESCHANNEL = "sapoaa_salesChannel".intern();
			public static final String SAPOAA_VENDORITEMCATEGORY = "sapoaa_vendorItemCategory".intern();
		}
		public static class SAPGlobalConfiguration
		{
			public static final String SAPOAA_CARCLIENT = "sapoaa_carClient".intern();
			public static final String SAPOAA_CARHTTPDESTINATION = "sapoaa_carHttpDestination".intern();
		}
		public static class SAPHTTPDestination
		{
			public static final String SAPOAA_GLOBALCONFIGURATIONS = "sapoaa_globalConfigurations".intern();
		}
		public static class StockLevel
		{
			public static final String SAPOAA_ROUGHSTOCKINDICATOR = "sapoaa_roughStockIndicator".intern();
			public static final String SAPOAA_ROUGHSTOCKINDICATOR_AVAILABILITYDATE = "sapoaa_roughStockIndicator_availabilityDate".intern();
		}
	}
	public static class Enumerations
	{
		public static class Sapoaa_mode
		{
			public static final String OAAPROFILE = "oaaProfile".intern();
			public static final String SALESCHANNEL = "salesChannel".intern();
		}
	}
	public static class Relations
	{
		public static final String SAPCARHTTPDESTINATIONFORGLOBALCONFIGURATION = "SapCarHttpDestinationForGlobalConfiguration".intern();
	}
	
	protected GeneratedSapoaamodelConstants()
	{
		// private constructor
	}
	
	
}
