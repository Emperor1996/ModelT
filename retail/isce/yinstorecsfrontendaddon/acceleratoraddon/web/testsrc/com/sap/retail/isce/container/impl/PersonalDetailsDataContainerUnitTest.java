/*****************************************************************************
 Class:        PersonalDetailsDataContainerUnitTest
 Copyright (c) 2016, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.container.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sap.retail.isce.container.DataContainerContext;
import com.sap.retail.isce.exception.DataContainerServiceException;


/**
 * Unit Test for Default implementation class PersonalDetailsDataContainer
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations =
{ "ISCETest-spring.xml" })
@UnitTest
public class PersonalDetailsDataContainerUnitTest
{
	protected static final String APPENDIX_DIRECTORY = "..";
	protected static final String APPENDIX_JAVASCRIPT = "javascript:alert();";
	protected static final String MODUS_REGULAR = "";
	protected static final String MODUS_ENCODING_TEST = "encoding";
	protected static final String MODUS_PATH_TRAVERSAL_TEST = "pathtraversal";
	protected static final String UID_VALUE = "johndoetest";
	protected static final String FIRST_NAME = "John";
	protected static final String LAST_NAME = "Doe";
	protected static final String IMAGE_SOURCE = "/folder1/folder2/folder2/Foto.png";
	protected static final String STREET = "Long Acre";
	protected static final String HOUSE_NUMBER = "90";
	protected static final String POSTAL_CODE = "WC2E 9RZ";
	protected static final String TOWN = "London";
	protected static final String COUNTRY = "United Kingdom";
	protected static final String PHONE = "+44 123 5678";
	protected static final String EMAIL = "johndoe@test4wec.com";
	protected static final String TITLE_CODE = "mr";

	@Resource(name = "personalDetailsDataContainerUnderTest")
	private final PersonalDetailsDataContainer classUnderTest = new PersonalDetailsDataContainer();

	private final DataContainerContext dataContainerContext = new DataContainerContextDefaultImpl();

	protected static final Date now = new Date();

	@Before
	public void setUp()
	{
		prepareTestData(MODUS_REGULAR);
	}

	/**
	 * Tests encode HTML.
	 */
	@Test
	public void testEncodeHTML()
	{
		classUnderTest.setDataInErrorState();
		prepareTestData(MODUS_ENCODING_TEST);

		try
		{
			classUnderTest.readData();
		}
		catch (final DataContainerServiceException e)
		{
			assertFalse("Test method execution went bad", true);
		}

		classUnderTest.encodeHTML();
		assertFalse("Javascript should be encoded", classUnderTest.getFirstName().equalsIgnoreCase(APPENDIX_JAVASCRIPT));
	}

	/**
	 * Tests the readData method..
	 */
	@Test
	public void testReadData()
	{
		try
		{
			classUnderTest.readData();

			assertEquals("Error when reading title!", TITLE_CODE, classUnderTest.getTitle());
			assertEquals("Error when reading first name!", FIRST_NAME, classUnderTest.getFirstName());
			assertEquals("Error when reading last name!", LAST_NAME, classUnderTest.getLastName());
			assertEquals("Error when reading country!", COUNTRY, classUnderTest.getCountry());
			assertEquals("Error when reading email!", EMAIL, classUnderTest.getEmail());
			assertEquals("Error when reading street!", STREET, classUnderTest.getStreet());
			assertEquals("Error when reading house number!", HOUSE_NUMBER, classUnderTest.getHouseNumber());
			assertEquals("Error when reading postal code!", POSTAL_CODE, classUnderTest.getPostalCode());
			assertEquals("Error when reading registration date!", now, classUnderTest.getRegisteredSince());
			assertEquals("Error when reading town!", TOWN, classUnderTest.getTown());
			assertEquals("Error when reading phone!", PHONE, classUnderTest.getPhone());
			assertEquals("Error when reading image source!", IMAGE_SOURCE, classUnderTest.getImageSource());
		}
		catch (final DataContainerServiceException e)
		{
			assertFalse("Test method execution went bad", true);
		}
	}

	/**
	 * Prepare appendix for various tests
	 *
	 * @param modus
	 * @return adapted appendix
	 */
	protected String getAppendix(final String modus)
	{
		String appendix = "";

		switch (modus)
		{
			case MODUS_ENCODING_TEST:
				appendix = APPENDIX_JAVASCRIPT;
			case MODUS_PATH_TRAVERSAL_TEST:
				appendix = APPENDIX_DIRECTORY;
		}

		return appendix;
	}

	/**
	 * Prepare data required for tests.
	 */
	protected void prepareTestData(final String modus)
	{
		final String appendix = getAppendix(modus);
		final CustomerData customerData = new CustomerData();
		final AddressModel addressModel = new AddressModel();
		final CountryModel countryModel = new CountryModel();

		customerData.setUid(UID_VALUE.concat(appendix));
		customerData.setTitleCode(TITLE_CODE.concat(appendix));
		customerData.setFirstName(FIRST_NAME.concat(appendix));
		customerData.setLastName(LAST_NAME.concat(appendix));
		countryModel.setName(COUNTRY.concat(appendix), getLocale());
		addressModel.setContactAddress(Boolean.TRUE);
		addressModel.setCountry(countryModel);
		addressModel.setEmail(EMAIL.concat(appendix));
		addressModel.setStreetname(STREET.concat(appendix));
		addressModel.setStreetnumber(HOUSE_NUMBER.concat(appendix));
		addressModel.setPostalcode(POSTAL_CODE.concat(appendix));
		addressModel.setTown(TOWN.concat(appendix));
		addressModel.setPhone1(PHONE.concat(appendix));

		final Collection<AddressModel> addresses = new ArrayList<AddressModel>();
		addresses.add(addressModel);

		final CustomerModel mockCustomerModel = mock(CustomerModel.class);
		when(mockCustomerModel.getAddresses()).thenReturn(addresses);

		final MediaModel mockMediaModel = mock(MediaModel.class);
		when(mockMediaModel.getURL()).thenReturn(IMAGE_SOURCE);

		when(mockCustomerModel.getProfilePicture()).thenReturn(mockMediaModel);
		when(mockCustomerModel.getCreationtime()).thenReturn(now);

		final TitleModel mockTitleModel = mock(TitleModel.class);
		when(mockTitleModel.getName()).thenReturn("titleModelName");

		final UserService mockUserService = mock(UserService.class);
		when(mockUserService.getUserForUID(null)).thenReturn(mockCustomerModel);
		when(mockUserService.getTitleForCode("titleFromCustomerData")).thenReturn(mockTitleModel);

		final Map dataContainerContextMap = dataContainerContext.getContextMap();
		dataContainerContextMap.put(DataContainerContext.CUSTOMER_DATA, customerData);

		classUnderTest.setDataContainerContext(dataContainerContext);
		classUnderTest.setUserService(mockUserService);
	}

	/**
	 * Return default locale.
	 *
	 * @return locale
	 */
	protected Locale getLocale()
	{
		return Locale.getDefault();
	}

	@Test
	public void testGetContainerContextParamName()
	{
		assertEquals("ContainerContextParamName should be personalDetailsDataContainer", "personalDetailsDataContainer",
				classUnderTest.getContainerContextParamName());
	}

	@Test
	public void testGetContainerName()
	{
		assertEquals("ContainerName should be PersonalDetailsDataContainer",
				"com.sap.retail.isce.container.impl.PersonalDetailsDataContainer", classUnderTest.getContainerName());
	}

	/**
	 * Tests getLocalizedContainerName.
	 */
	@Test
	public void testGetLocalizedContainerName()
	{
		assertEquals("Localized Container Name should be instorecs.customer360.details", "instorecs.customer360.details",
				classUnderTest.getLocalizedContainerName());
	}

	@Test
	public void testGetProfilePictureUrlCustomerModelNull()
	{
		try
		{
			assertNull("ProfilePictureUrl should be null due to null customerModel",
					classUnderTest.getProfilePictureUrlFromCustomerModel(null));
		}
		catch (final DataContainerServiceException e)
		{
			assertFalse("Exception was thrown though execution should not have gone wrong", true);
		}
	}

	@Test
	public void testGetProfilePictureUrlCustomerModelProfilePictureNull()
	{
		try
		{
			final CustomerModel customerModel = new CustomerModel();
			assertNull("ProfilePictureUrl should be null due to null customerModel",
					classUnderTest.getProfilePictureUrlFromCustomerModel(customerModel));
		}
		catch (final DataContainerServiceException e)
		{
			assertFalse("Exception was thrown though execution should not have gone wrong", true);
		}
	}

	@Test
	public void testGetProfilePictureUrlCustomerModelProfilePictureUrlNull()
	{
		try
		{
			final CustomerModel customerModel = new CustomerModel();
			final MediaModel mediaModel = new MediaModel();
			customerModel.setProfilePicture(mediaModel);
			assertNull("ProfilePictureUrl should be null due to null customerModel",
					classUnderTest.getProfilePictureUrlFromCustomerModel(customerModel));
		}
		catch (final DataContainerServiceException e)
		{
			assertFalse("Exception was thrown though execution should not have gone wrong", true);
		}
	}

	@Test
	public void testGetAddressDataFromCustomerModelCustomerModelNull()
	{
		assertNull("Address data should be null", classUnderTest.getAddressDataFromCustomerModel(null));
	}

	@Test
	public void testGetAddressDataFromCustomerModelAddressesNull()
	{
		final CustomerModel customerModel = new CustomerModel();

		assertNull("Address data should be null", classUnderTest.getAddressDataFromCustomerModel(customerModel));
	}

	@Test
	public void testGetAddressDataFromCustomerModelAddressModelNull()
	{
		final CustomerModel customerModel = new CustomerModel();
		final Collection<AddressModel> addressCollection = Collections.EMPTY_LIST;
		customerModel.setAddresses(addressCollection);

		assertNull("Address data should be null", classUnderTest.getAddressDataFromCustomerModel(customerModel));
	}

	@Test
	public void testGetCustomerData()
	{

		final DataContainerContext dataContainerContext = new DataContainerContextDefaultImpl();
		classUnderTest.setDataContainerContext(dataContainerContext);

		try
		{
			classUnderTest.getCustomerData();
			assertFalse("Exception was not thrown despite customerData being null", true);
		}

		catch (final DataContainerServiceException e)
		{
			assertNotNull("An exception should have been thrown here", e);
		}

	}

	@Test
	public void testGetUserService()
	{
		assertEquals("UserService not received", classUnderTest.userService, classUnderTest.getUserService());
	}

	@Test
	public void testGetErrorState()
	{
		assertEquals("ErrorState not received", classUnderTest.errorState, classUnderTest.getErrorState());
	}

	@Test
	public void testGetTitleFromCustomerDataCustomerDataNull()
	{
		assertNull("Title should be null", classUnderTest.getTitleFromCustomerData(null));
	}

	@Test
	public void testGetTitleFromCustomerDataTitleCodeNull()
	{
		final CustomerData customerData = new CustomerData();
		assertNull("", classUnderTest.getTitleFromCustomerData(customerData));
	}

	@Test
	public void testGetTitleFromCustomerDataTitleNull()
	{
		final CustomerData customerData = new CustomerData();
		customerData.setTitleCode("TESTtest");
		assertEquals("Title should be TESTtest but was" + classUnderTest.getTitleFromCustomerData(customerData), "TESTtest",
				classUnderTest.getTitleFromCustomerData(customerData));
	}

	@Test
	public void testGetTitleFromCustomerDataTitleNotNull()
	{
		final CustomerData customerData = new CustomerData();
		customerData.setTitleCode("titleFromCustomerData");
		assertEquals("Title should be titleModelName but was" + classUnderTest.getTitleFromCustomerData(customerData),
				"titleModelName", classUnderTest.getTitleFromCustomerData(customerData));
	}

	@Test
	public void testGetCountryOfCustomerFromAddressModel()
	{
		assertNull("Country should be null", classUnderTest.getCountryOfCustomerFromAddressModel(null));
	}

	@Test
	public void testGetOnlineRegistrationDateOfCustomerFromCustomerModel()
	{
		assertNull("Online Registration Date should be null",
				classUnderTest.getOnlineRegistrationDateOfCustomerFromCustomerModel(null));
	}

}
