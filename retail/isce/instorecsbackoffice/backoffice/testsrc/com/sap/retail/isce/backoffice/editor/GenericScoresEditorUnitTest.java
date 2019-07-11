/*****************************************************************************
 Class:        GenericScoresEditorUnitTest
 Copyright (c) 2019, SAP SE, Germany, All rights reserved.
 *****************************************************************************/
package com.sap.retail.isce.backoffice.editor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cockpit.zk.mock.DummyExecution;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.impl.PageImpl;
import org.zkoss.zk.ui.metainfo.ComponentDefinitionMap;
import org.zkoss.zk.ui.metainfo.LanguageDefinition;
import org.zkoss.zk.ui.metainfo.PageDefinition;
import org.zkoss.zk.ui.metainfo.impl.ComponentDefinitionImpl;
import org.zkoss.zk.ui.sys.ExecutionsCtrl;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;

import com.hybris.cockpitng.editors.EditorListener;
import com.sap.retail.isce.container.impl.AllObjectScoresDataContainer;
import com.sap.retail.isce.service.DataContainerService;
import com.sap.retail.isce.service.sap.result.ISCEObjectScoreResult;
import com.sap.retail.isce.service.util.SpringUtil;


/**
 * Unit Test class for GenericScoresEditor
 */
@UnitTest
public class GenericScoresEditorUnitTest
{

	private class GenericScoresEditorTest extends GenericScoresEditor
	{

		@Override
		protected String getLocalizedString(final String resKey)
		{
			return resKey;
		}
	}

	private class ComponentDefinitionTest extends ComponentDefinitionImpl
	{

		public ComponentDefinitionTest(final LanguageDefinition langdef, final PageDefinition pgdef, final String name,
				final String clsnm)
		{
			super(langdef, pgdef, name, clsnm);
		}

		@Override
		public boolean hasMold(final String name)
		{
			return true;
		}
	}

	private class ISCEDummyExecution extends DummyExecution
	{
		ComponentDefinitionMap compDefMap = new ComponentDefinitionMap(true);
		private final PageImpl page;

		@Override
		public Page getCurrentPage()
		{
			return page;
		}

		public ISCEDummyExecution(final ApplicationContext applicationContext)
		{
			super(applicationContext);
			//page = new PageImpl(new LanguageDefinition("xml", "a", "a", null, "a", "a", true, false, new DummyWebApp(applicationContextMock)), compDefMap, "", "");
			page = new PageImpl(languageDefinitionMock, compDefMap, "", "");
		}
	}

	private class AllObjectScoresDataContainerTest extends AllObjectScoresDataContainer
	{
		/**
		 * @param yMktHttpDestination
		 */
		public AllObjectScoresDataContainerTest(final String yMktHttpDestination)
		{
			super();
		}

		/**
		 * Sets the unencoded list containing the object scores (description and weight).
		 *
		 * @param objectScoresList
		 *           the scores set.
		 */
		public void setUnencodedObjectScoresList(final List<ISCEObjectScoreResult> objectScoresList)
		{
			this.unencodedObjectScoresList = objectScoresList;
		}
	}

	private final AllObjectScoresDataContainerTest allObjectScores = new AllObjectScoresDataContainerTest(
			"ISCEHybrisMarketingHTTPDestination");

	private GenericScoresEditorTest classUnderTest;
	@Mock
	private EditorListener listenerSpy;
	@Mock
	private ApplicationContext applicationContextMock;
	@Mock
	private SessionService sessionServiceMock;
	@Mock
	private DataContainerService dataContainerServiceMock;
	@Mock
	private SpringUtil springUtilMock;
	@Mock
	private LanguageDefinition languageDefinitionMock;
	//	@Mock
	//	private Component parentMock;
	//	@Mock
	//	private EditorContext editorContextMock;


	@Before
	public void setUp()
	{
		classUnderTest = new GenericScoresEditorTest();
		MockitoAnnotations.initMocks(this);
		Mockito.doNothing().when(dataContainerServiceMock).readDataContainers(Arrays.asList(allObjectScores), null);
		Mockito.doReturn(dataContainerServiceMock).when(springUtilMock).getBean("defaultDataContainerService");
		Mockito.doReturn(allObjectScores).when(springUtilMock).getBean("allObjectScoresDataContainer");
		//Mockito.doReturn(springUtilMock).when(applicationContextMock).getBean("springUtil");
		classUnderTest.springUtil = springUtilMock;
		classUnderTest.sessionService = sessionServiceMock;
		//Mockito.doReturn(sessionServiceMock).when(applicationContextMock).getBean("sessionService");
	}

	/**
	 * @param applicationContextMock2
	 */
	private void setApplicationContext(final ApplicationContext applicationContextMock2)
	{
		// YTODO Auto-generated method stub

	}

	protected void prepareZulRuntime()
	{
		final ISCEDummyExecution dummyExec = new ISCEDummyExecution(applicationContextMock);
		dummyExec.getCurrentPage().getComponentDefinitionMap()
				.add(new ComponentDefinitionTest(null, null, "org.zkoss.zul.Listbox", "org.zkoss.zul.Listbox"));
		dummyExec.getCurrentPage().getComponentDefinitionMap()
				.add(new ComponentDefinitionTest(null, null, "org.zkoss.zul.Paging", "org.zkoss.zul.Paging"));
		dummyExec.getCurrentPage().getComponentDefinitionMap()
				.add(new ComponentDefinitionTest(null, null, "org.zkoss.zul.Hbox", "org.zkoss.zul.Hbox"));

		ExecutionsCtrl.setCurrent(dummyExec);
	}

	//	@Test
	//	public void testRender()
	//	{
	//		// SMOKE
	//		allObjectScores.setErrorState(Boolean.TRUE);
	//		classUnderTest.render(parentMock, editorContextMock, listenerSpy);
	//		final boolean b = true;
	//		assertTrue("No exception shoudl have occured", b);
	//	}

	@Test
	public void testReadAllObjectScores()
	{
		final AllObjectScoresDataContainer result = classUnderTest.readAllObjectScores();
		assertEquals("readAllObjectScores - must not be empty ", result, allObjectScores);
		Mockito.verify(dataContainerServiceMock, Mockito.times(1)).readDataContainers(Arrays.asList(allObjectScores), null);
	}

	@Test
	public void testAddValueToText()
	{

		final Textbox text = new Textbox();
		classUnderTest.addValueToText(text, "a", listenerSpy);
		assertEquals("addValueToText - text must be 'a'", "a", text.getText());
		assertEquals("addValueToText - value must be 'a'", "a", text.getText());
		Mockito.verify(listenerSpy, Mockito.times(1)).onValueChanged("a");

		classUnderTest.addValueToText(text, "b", listenerSpy);
		assertEquals("addValueToText - text must be 'a, b'", "a, b", text.getText());
		assertEquals("addValueToText - value must be 'a, b'", "a, b", text.getText());
		Mockito.verify(listenerSpy, Mockito.times(1)).onValueChanged("a, b");

		classUnderTest.addValueToText(text, "", listenerSpy);
		assertEquals("addValueToText - text must be 'a, b'", "a, b", text.getText());
		assertEquals("addValueToText - value must be 'a, b'", "a, b", text.getText());
		Mockito.verify(listenerSpy, Mockito.times(1)).onValueChanged("a, b");

		//		//final Textbox text = new Textbox();
		//		Mockito.when(textBoxMock.getText()).thenReturn("");
		//		classUnderTest.addValueToText(textBoxMock, "a", listenerSpy);
		//		//assertEquals("addValueToText - text must be 'a'", "a", text.getText());
		//		Mockito.verify(textBoxMock, Mockito.times(1)).getText();
		//		Mockito.verify(textBoxMock, Mockito.times(1)).setText("a");
		//		Mockito.verify(listenerSpy, Mockito.times(1)).onValueChanged("a");
		//
		//		Mockito.when(textBoxMock.getText()).thenReturn("a");
		//		classUnderTest.addValueToText(textBoxMock, "b", listenerSpy);
		//		//assertEquals("addValueToText - text must be 'a, b'", "a, b", text.getText());
		//		Mockito.verify(textBoxMock, Mockito.times(1)).setText("a, b");
		//		Mockito.verify(listenerSpy, Mockito.times(1)).onValueChanged("a, b");
		//
		//		classUnderTest.addValueToText(textBoxMock, "", listenerSpy);
		//		// assertEquals("addValueToText - text must be 'a, b'", "a, b", text.getText());
		//		Mockito.verify(textBoxMock, Mockito.times(1)).setText("a, b");
		//		Mockito.verify(listenerSpy, Mockito.times(1)).onValueChanged("a, b");
	}

	@Test
	public void testGetModelContentObjectScores()
	{
		List<String[]> model = classUnderTest.getModelContentObjectScores(null);
		assertTrue("getModelContentObjectScores - modell content must be empty ", model.isEmpty());

		final AllObjectScoresDataContainerTest allObjectScores = new AllObjectScoresDataContainerTest(
				"ISCEHybrisMarketingHTTPDestination");

		model = classUnderTest.getModelContentObjectScores(allObjectScores);
		assertTrue("getModelContentObjectScores - modell content must be empty ", model.isEmpty());

		final List objectScoresList = new ArrayList<ISCEObjectScoreResult>();
		objectScoresList.add(new ISCEObjectScoreResult("id", "name", null));

		allObjectScores.setUnencodedObjectScoresList(objectScoresList);

		model = classUnderTest.getModelContentObjectScores(allObjectScores);
		assertEquals("getModelContentObjectScores - modell should contain 1 entry ", 1, model.size());

		final String[] content = model.get(0);
		assertEquals("getModelContentObjectScores - entry first part should be 'name'", "name", content[0]);
		assertEquals("getModelContentObjectScores - entry second part should be 'id'", "id", content[1]);
	}

	@Test
	public void testCreateListBox()
	{
		prepareZulRuntime();

		final List<String[]> modelContent = new ArrayList();

		final String[] val1 =
		{ "wert1", "a" };
		modelContent.add(val1);
		final String[] val2 =
		{ "wert2", "b" };
		modelContent.add(val2);

		final Listbox listbox = classUnderTest.createListBox(listenerSpy, new Listhead(), modelContent);

		assertEquals("createListBox - listbox model size must be identical to model content", modelContent.size(),
				listbox.getModel().getSize());
		assertEquals("createListBox - listbox must be peagable", "paging", listbox.getMold());
		assertEquals("createListBox - listbox must have 10 entries per page", 10, listbox.getPageSize());
		assertEquals("createListBox - listbox must have head", "org.zkoss.zul.Listhead",
				listbox.getChildren().get(0).getClass().getName());
		assertNotNull("createListBox - listbox must have an item Renderer", listbox.getItemRenderer());
	}

	@Test
	public void testDefineListItem()
	{
		final String[] data =
		{ "wert1", "a" };

		final Listitem listitem = new Listitem();

		classUnderTest.defineListItem(listitem, data, listenerSpy);

		assertEquals("defineListItem - item label should be 'wert1'", "wert1", listitem.getLabel());
		assertEquals("defineListItem - item value should be 'a'", "a", listitem.getValue());
		assertEquals("defineListItem - item must have Listcell", "org.zkoss.zul.Listcell",
				listitem.getChildren().get(0).getClass().getName());
		final Iterator onClickHandler = listitem.getListenerIterator("onClick");
		assertNotNull(onClickHandler.next());
	}

	@Test
	public void testCreateAddScoreButton()
	{
		final Popup addScorePopup = new Popup();

		final Button button = classUnderTest.createAddScoreButton(new Div(), addScorePopup);
		assertEquals("CreateAddScoreButton - label must be '+'", "+", button.getLabel());

		final Iterator onClickHandler = button.getListenerIterator("onClick");
		assertNotNull(onClickHandler.next());
	}

	@Test
	public void testCreateTextbox()
	{
		Textbox text = classUnderTest.createTextBox("", listenerSpy);
		assertEquals("createTextbox - text must be ''", "", text.getText());
		text = classUnderTest.createTextBox("a, b, c", listenerSpy);
		assertEquals("createTextbox - text must be 'a, b, c'", "a, b, c", text.getText());

		final Iterator onChangeHandler = text.getListenerIterator("onChange");
		assertNotNull(onChangeHandler.next());
	}

	@Test
	public void testCreateListHead()
	{
		final Listhead listHead = classUnderTest.createListHead();
		assertNotNull(listHead);
		assertEquals("createListHead - must conatin to header entries", 2, listHead.getChildren().size());

		assertEquals("createListHead - child 1 must have label 'type.CMSGenericScoreEditor.scoreName'",
				"type.CMSGenericScoreEditor.scoreName", ((Listheader) listHead.getChildren().get(0)).getLabel());
	}

}