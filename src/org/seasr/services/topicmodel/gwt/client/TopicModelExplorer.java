package org.seasr.services.topicmodel.gwt.client;

import java.util.List;

import org.seasr.services.topicmodel.gwt.client.model.FileRecord;
import org.seasr.services.topicmodel.gwt.client.model.TopicRecord;
import org.seasr.services.topicmodel.gwt.shared.models.FileMeta;
import org.seasr.services.topicmodel.gwt.shared.models.TopicMeta;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.HeaderSpan;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public class TopicModelExplorer implements EntryPoint {

    private final TopicModelDataServiceAsync topicModelDataService = GWT.create(TopicModelDataService.class);

    @Override
    public void onModuleLoad() {

        VLayout layout = new VLayout(10);
        layout.setWidth100();
        layout.setHeight100();
        layout.setLayoutMargin(10);
        layout.setMembersMargin(10);
        layout.setAlign(Alignment.CENTER);

        HLayout inputLayout = new HLayout();
        inputLayout.setMembersMargin(10);
        inputLayout.setLayoutMargin(10);
        inputLayout.setAlign(Alignment.CENTER);
        inputLayout.setDefaultLayoutAlign(Alignment.CENTER);

        HLayout resultLayout = new HLayout();
        resultLayout.setHeight100();
        resultLayout.setMembersMargin(10);
        resultLayout.setLayoutMargin(10);

        final ListGrid filesGrid = new ListGrid();
        filesGrid.setHeight100();
        filesGrid.setHeaderHeight(40);
        ListGridField firstNameField = new ListGridField("first_name", "First Name", 100);
        ListGridField lastNameField = new ListGridField("last_name", "Last Name", 100);
        ListGridField fileIdField = new ListGridField("file", "File", 50);
        ListGridField titleField = new ListGridField("title", "Title");
        ListGridField yearField = new ListGridField("year", "Year", 40);
        yearField.setType(ListGridFieldType.INTEGER);
        ListGridField countField = new ListGridField("count", "Count", 40);
        countField.setType(ListGridFieldType.INTEGER);
        filesGrid.setFields(new ListGridField[] { fileIdField, titleField, yearField, firstNameField, lastNameField, countField });
        filesGrid.setHeaderSpans(new HeaderSpan("Documents List", new String[] { "file", "title", "year", "first_name", "last_name", "count" }));

        final ListGrid topicsGrid = new ListGrid();
        topicsGrid.setHeight100();
        topicsGrid.setHeaderHeight(40);
        ListGridField topicIdField = new ListGridField("topic_id", "Topic Id");
        topicIdField.setType(ListGridFieldType.INTEGER);
        topicIdField.setAlign(Alignment.CENTER);
        ListGridField keyword1Field = new ListGridField("keyword1", "Keyword 1");
        ListGridField keyword2Field = new ListGridField("keyword2", "Keyword 2");
        ListGridField keyword3Field = new ListGridField("keyword3", "Keyword 3");
        ListGridField keyword4Field = new ListGridField("keyword4", "Keyword 4");
        ListGridField keyword5Field = new ListGridField("keyword5", "Keyword 5");
        topicsGrid.setFields(new ListGridField[] { topicIdField, keyword1Field, keyword2Field, keyword3Field, keyword4Field, keyword5Field });
        topicsGrid.setHeaderSpans(new HeaderSpan("Topics List", new String[] { "topic_id", "keyword1", "keyword2", "keyword3", "keyword4", "keyword5" }));

        resultLayout.addMember(filesGrid);
        resultLayout.addMember(topicsGrid);

        final DynamicForm form = new DynamicForm();
        form.setWidth(250);
        form.setHeight(24);

        final TextItem tokenItem = new TextItem();
        tokenItem.setTitle("Token");
        tokenItem.setHint("Enter a token to search for");
        tokenItem.setShowHintInField(true);
        tokenItem.setRequired(true);


        form.setFields(new FormItem[] { tokenItem });

        topicsGrid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {

            @Override
            public void onRecordDoubleClick(RecordDoubleClickEvent event) {
                int topic = event.getRecord().getAttributeAsInt("topic_id");
                Window.open("tagcloud.html?topic=" + topic, "_topicTagCloud" + topic, "toolbar=0,menubar=0,resizable=0,width=520,height=520");
            }
        });


        IButton submitBtn = new IButton("Submit");
        submitBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (!form.validate()) return;

                final String token = tokenItem.getValueAsString();

                filesGrid.setEmptyMessage("Fetching data, please wait...");
                filesGrid.setData(new RecordList());
                topicsGrid.setEmptyMessage("Fetching data, please wait...");
                topicsGrid.setData(new RecordList());

                topicModelDataService.getFilesMetaForToken(token, new AsyncCallback<List<FileMeta>>() {
                    @Override
                    public void onSuccess(List<FileMeta> result) {
                        int n = result.size();
                        FileRecord[] records = new FileRecord[n];
                        for (int i = 0; i < n; i++)
                            records[i] = new FileRecord(result.get(i));
                        filesGrid.setEmptyMessage("No items to show");
                        filesGrid.setData(records);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        filesGrid.setEmptyMessage("No items to show");
                        SC.say("Error", caught.getMessage());
                    }
                });

                topicModelDataService.getTopicsForToken(token, new AsyncCallback<List<TopicMeta>>() {
                    @Override
                    public void onSuccess(List<TopicMeta> result) {
                        int n = result.size();
                        TopicRecord[] records = new TopicRecord[n];
                        for (int i = 0; i < n; i++)
                            records[i] = new TopicRecord(result.get(i));
                        topicsGrid.setEmptyMessage("No items to show");
                        topicsGrid.setData(records);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        topicsGrid.setEmptyMessage("No items to show");
                        SC.say("Error", caught.getMessage());
                    }
                });
            }
        });

        inputLayout.addMember(form);
        inputLayout.addMember(submitBtn);

        layout.addMember(inputLayout);
        layout.addMember(resultLayout);

        layout.draw();
    }

}
