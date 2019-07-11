/**
 *
 */
package com.sap.retail.isce.backoffice.editor;

import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Popup;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;

import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.sap.retail.isce.container.impl.AllObjectScoresDataContainer;
import com.sap.retail.isce.service.DataContainerService;
import com.sap.retail.isce.service.sap.result.ISCEObjectScoreResult;
import com.sap.retail.isce.service.util.DataContainerServiceUtil;
import com.sap.retail.isce.service.util.SpringUtil;



/**
 * @author Administrator
 *
 */
public class GenericScoresEditor implements CockpitEditorRenderer<String>
{

	private static final String DEFAULT_DATA_CONTAINER_SERVICE = "defaultDataContainerService";
	private static final String SPRING_UTIL = "springUtil";
	private static final String SESSION_SERVICE = "sessionService";
	private static final String ALL_OBJECT_SCORES_DATA_CONTAINER = "allObjectScoresDataContainer";

	@Resource
	public SessionService sessionService;
	@Resource
	public SpringUtil springUtil;

	private Textbox textbox;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.hybris.cockpitng.editors.CockpitEditorRenderer#render(org.zkoss.zk.ui.Component,
	 * com.hybris.cockpitng.editors.EditorContext, com.hybris.cockpitng.editors.EditorListener)
	 */
	@Override
	public void render(final Component parent, final EditorContext<String> context, final EditorListener<String> listener)
	{
		if (context.isEditable())
		{
			final AllObjectScoresDataContainer allObjectScores = readAllObjectScores();
			final List<String[]> modelContent = getModelContentObjectScores(allObjectScores);

			final Div editorContainer = new Div();
			textbox = createTextBox(context.getInitialValue(), listener);
			textbox.setWidth("85%");
			editorContainer.appendChild(textbox);
			final Popup addScorePopup = new Popup();
			addScorePopup.setWidth("30%");

			final Button addScoreBtn = createAddScoreButton(editorContainer, addScorePopup);
			addScoreBtn.setStyle("margin-left: 2px;");
			editorContainer.appendChild(addScoreBtn);
			editorContainer.appendChild(addScorePopup);

			if (modelContent != null && !modelContent.isEmpty())
			{
				final Listbox listBox = createListBox(listener, createListHead(), modelContent);
				addScorePopup.appendChild(listBox);
			}
			else
			{
				addScorePopup.appendChild(createMessageLabel(allObjectScores));
			}

			editorContainer.setParent(parent);
		}
		else
		{
			final Label label = new Label(context.getInitialValue());
			label.setParent(parent);
		}


	}


	/**
	 * Creates a label with a message depending on the modelContent state.
	 *
	 * @param allObjectScores
	 *           used to determine the message. Create destinationError message if the containers errorState is true,
	 *           else a noScores message.
	 * @return the label with the matching message.
	 */
	private Label createMessageLabel(final AllObjectScoresDataContainer allObjectScores)
	{
		if (allObjectScores.getErrorState().equals(Boolean.TRUE))
		{
			return new Label(getLocalizedString("type.CMSGenericScoreEditor.destinationError"));
		}
		return new Label(getLocalizedString("type.CMSGenericScoreEditor.noScores"));
	}

	/**
	 * Creates an instance of the AllObjectScoresDataContainer and triggers the ODATA call, to read the related data.
	 *
	 * @return AllObjectScoresDataContainer an instance of the container, were the data was read.
	 */
	protected AllObjectScoresDataContainer readAllObjectScores()
	{
		//final SessionService sessionService = (SessionService) getApplicationContext().getBean(SESSION_SERVICE);
		final DataContainerServiceUtil dataContainerServiceUtil = (DataContainerServiceUtil) sessionService
				.getAttribute(DataContainerService.ISCE_360_DATA_CONTAINER_SESSION_ATTRIBUTE);

		if (dataContainerServiceUtil != null
				&& dataContainerServiceUtil.getDataContainerForName(ALL_OBJECT_SCORES_DATA_CONTAINER) != null)
		{
			return (AllObjectScoresDataContainer) dataContainerServiceUtil.getDataContainerForName(ALL_OBJECT_SCORES_DATA_CONTAINER);
		}

		//final SpringUtil springUtil = (SpringUtil) getApplicationContext().getBean(SPRING_UTIL);
		final DataContainerService dataContainerService = (DataContainerService) springUtil.getBean(DEFAULT_DATA_CONTAINER_SERVICE);
		final AllObjectScoresDataContainer allObjectScores = (AllObjectScoresDataContainer) springUtil
				.getBean(ALL_OBJECT_SCORES_DATA_CONTAINER);

		dataContainerService.readDataContainers(Arrays.asList(allObjectScores), null);

		return allObjectScores;
	}

	/**
	 * Determines the model content for the listbox from the results of the allObjectScores container.
	 *
	 * @param allObjectScores
	 *           the AllObjectScoresDataContainer to determine the content from.
	 * @return the modelContent for the listBox.
	 */
	protected List<String[]> getModelContentObjectScores(final AllObjectScoresDataContainer allObjectScores)
	{
		final List<String[]> modelContent = new ArrayList();

		if (allObjectScores == null || allObjectScores.getUnencodedObjectScoresList() == null)
		{
			return modelContent;
		}

		final List<ISCEObjectScoreResult> objectScores = allObjectScores.getUnencodedObjectScoresList();

		for (final ISCEObjectScoreResult objectScoreResult : objectScores)
		{
			final String[] val =
			{ objectScoreResult.getScoreDescription(), objectScoreResult.getScoreId() };
			modelContent.add(val);
		}

		return modelContent;
	}

	/**
	 * Creates a pageable listbox with the given head and items for the given modelContent. When one of the items is
	 * clicked upon, the addValueToText method is called.
	 *
	 * @param listener
	 *           used for the addValueToText call.
	 * @param head
	 *           the head to use for the listbox.
	 * @return Listbox the listbox for the given modelContent.
	 */
	protected Listbox createListBox(final EditorListener listener, final Listhead head, final List<String[]> modelContent)
	{
		final Listbox listBox = new Listbox();

		final SimpleListModel model = new SimpleListModel(modelContent);
		listBox.setModel(model);
		listBox.setMold("paging");
		listBox.setPageSize(10);

		listBox.appendChild(head);

		listBox.setItemRenderer(new ListitemRenderer()
		{
			@Override
			public void render(final Listitem item, final Object data, final int arg2) throws Exception
			{
				defineListItem(item, data, listener);
			}
		});

		return listBox;
	}

	/**
	 * Sets up the given item with the given data and adds a ON_CLICK listener to the item, that calls the addValueToText
	 * method.
	 *
	 * @param item
	 *           the item to set up.
	 * @param data
	 *           the data to show in the item
	 * @param listener
	 *           the listener to use in the ON_CLICK event.
	 */
	protected void defineListItem(final Listitem item, final Object data, final EditorListener listener)
	{
		final String[] values = (String[]) data;
		item.setLabel(values[0]);
		item.setValue(values[1]);

		final Listcell listCellScoreId = new Listcell();
		listCellScoreId.appendChild(new Label(values[1]));
		item.appendChild(listCellScoreId);

		item.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception // NOPMD
			{
				addValueToText(textbox, (String) item.getValue(), listener);
			}
		});
	}

	/**
	 * Creates a header for the object score list.
	 *
	 * @return listHead the header to use for the object score list.
	 */
	protected Listhead createListHead()
	{
		final Listheader nameHeader = new Listheader(getLocalizedString("type.CMSGenericScoreEditor.scoreName"));
		final Listheader idHeader = new Listheader(getLocalizedString("type.CMSGenericScoreEditor.scoreId"));

		final Listhead listHead = new Listhead();
		listHead.appendChild(nameHeader);
		listHead.appendChild(idHeader);

		return listHead;
	}

	/**
	 * Determines the localized String for the given key
	 *
	 * @param resKey
	 *           the resource key to determine the localized string for
	 * @return the localized string.
	 */
	protected String getLocalizedString(final String resKey)
	{
		return de.hybris.platform.util.localization.Localization.getLocalizedString(resKey);
	}

	/**
	 * Creates a Button, that opens the given popup if clicked.
	 *
	 * @param container
	 *           the container to open the popup in.
	 * @param addScorePopup
	 *           the popup to open.
	 *
	 * @return the button.
	 */
	protected Button createAddScoreButton(final Div container, final Popup addScorePopup)
	{
		final Button addScoreBtn = new Button();

		addScoreBtn.setLabel("+");

		addScoreBtn.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				addScorePopup.open(container, "after_start");
			}
		});

		return addScoreBtn;
	}

	/**
	 * Creates a textbox, with the given initial value. If the textbox is changed a valueChanged event is fired to the
	 * listener.
	 *
	 * @param initialValue
	 *           the initial value to set for the textbox.
	 * @param listener
	 *           the listener to fire a valueChanged event, if the textbox was changed.
	 */
	protected Textbox createTextBox(final Object initialValue, final EditorListener<String> listener)
	{
		textbox = new Textbox((String) initialValue);

		textbox.addEventListener(Events.ON_CHANGE, new EventListener<Event>()
		{
			@Override
			public void onEvent(final Event event) throws Exception
			{
				//GenericScoresEditor.this.setValue(textbox.getText());
				listener.onValueChanged(textbox.getText());
			}
		});

		return textbox;
	}


	/**
	 * Adds the given value to the textbox, separated by a comma and fires a valueChanged event for the listener.
	 *
	 * @param textbox
	 *           the textbox, the value should be added to.
	 * @param value
	 *           the value add to the textbox.
	 * @param listener
	 *           the listener to fire the event to.
	 */
	protected void addValueToText(final Textbox textbox, final String value, final EditorListener listener)
	{
		if (value != null && !value.isEmpty())
		{
			String text = textbox.getText();

			if (text != null && !text.isEmpty())
			{
				text = text + ", " + value;
			}
			else
			{
				text = value;
			}
			textbox.setText(text);
			//GenericScoresEditor.this.setValue(textbox.getText());
			listener.onValueChanged(textbox.getText());
		}
	}

}
