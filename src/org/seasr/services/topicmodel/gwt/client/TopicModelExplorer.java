package org.seasr.services.topicmodel.gwt.client;

import java.util.List;

import org.seasr.services.topicmodel.gwt.client.model.FileRecord;
import org.seasr.services.topicmodel.gwt.client.model.TopicCorrRecord;
import org.seasr.services.topicmodel.gwt.client.model.TopicRecord;
import org.seasr.services.topicmodel.gwt.shared.models.FileMeta;
import org.seasr.services.topicmodel.gwt.shared.models.LocTopicCorrMeta;
import org.seasr.services.topicmodel.gwt.shared.models.TopicMeta;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.HeaderSpan;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
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
        ListGridField nationField = new ListGridField("nation", "Nation");
        ListGridField genderField = new ListGridField("gender", "Gender");
        ListGridField countField = new ListGridField("count", "Count", 40);
        countField.setType(ListGridFieldType.INTEGER);
        filesGrid.setFields(new ListGridField[] { fileIdField, titleField, yearField, firstNameField, lastNameField, nationField, genderField, countField });
        filesGrid.setHeaderSpans(new HeaderSpan("Documents List", new String[] { "file", "title", "year", "first_name", "last_name", "nation", "gender", "count" }));

        final ListGrid topicsGrid = new ListGrid() {
            @Override
            protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {

                String fieldName = this.getFieldName(colNum);

                if (fieldName.equals("actions")) {
                    HLayout recordCanvas = new HLayout(3);
                    recordCanvas.setHeight(22);
                    recordCanvas.setAlign(Alignment.CENTER);
                    recordCanvas.setDefaultLayoutAlign(Alignment.CENTER);
                    ImgButton corrImg = new ImgButton();
                    corrImg.setShowDown(false);
                    corrImg.setShowRollOver(false);
                    corrImg.setLayoutAlign(Alignment.CENTER);
                    corrImg.setSrc("actions/groupby.png");
                    corrImg.setPrompt("Show Topic Location Correlations");
                    corrImg.setHeight(16);
                    corrImg.setWidth(16);
                    corrImg.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            int topicId = record.getAttributeAsInt("topic_id");

                            com.smartgwt.client.widgets.Window window = new com.smartgwt.client.widgets.Window();
                            window.setTitle("Topic Location Correlation for topic " + topicId);
                            window.setWidth(600);
                            window.setHeight(400);
                            window.setCanDragReposition(true);
                            window.setCanDragResize(true);

                            final ListGrid locTopicCorrGrid = new ListGrid() {
                                @Override
                                protected Canvas createRecordComponent(final ListGridRecord record2, Integer colNum2) {

                                    String fieldName = this.getFieldName(colNum2);

                                    if (fieldName.equals("actions")) {
                                        HLayout recordCanvas = new HLayout(3);
                                        recordCanvas.setHeight(22);
                                        recordCanvas.setAlign(Alignment.CENTER);
                                        recordCanvas.setDefaultLayoutAlign(Alignment.CENTER);
                                        ImgButton previewTextImg = new ImgButton();
                                        previewTextImg.setShowDown(false);
                                        previewTextImg.setShowRollOver(false);
                                        previewTextImg.setLayoutAlign(Alignment.CENTER);
                                        previewTextImg.setSrc("actions/view.png");
                                        previewTextImg.setPrompt("View segment text");
                                        previewTextImg.setHeight(16);
                                        previewTextImg.setWidth(16);
                                        previewTextImg.addClickHandler(new ClickHandler() {
                                            @Override
                                            public void onClick(ClickEvent event) {
                                                final int segment = record2.getAttributeAsInt("segment");
                                                final String file = record2.getAttribute("file");

                                                topicModelDataService.getTextForFileSegment(file, segment, new AsyncCallback<String>() {

                                                    @Override
                                                    public void onSuccess(String result) {
                                                        String text = SafeHtmlUtils.htmlEscape(result);

                                                        String kw1 = record.getAttribute("keyword1");
                                                        String kw2 = record.getAttribute("keyword2");
                                                        String kw3 = record.getAttribute("keyword3");
                                                        String kw4 = record.getAttribute("keyword4");
                                                        String kw5 = record.getAttribute("keyword5");

                                                        text = text.replaceAll(" " + kw1 + " ", " <span style='color: #E41A1C; font-weight: bold;'>" + kw1 + "</span> ");
                                                        text = text.replaceAll(" " + kw2 + " ", " <span style='color: #377EB8; font-weight: bold;'>" + kw2 + "</span> ");
                                                        text = text.replaceAll(" " + kw3 + " ", " <span style='color: #4DAF4A; font-weight: bold;'>" + kw3 + "</span> ");
                                                        text = text.replaceAll(" " + kw4 + " ", " <span style='color: #984EA3; font-weight: bold;'>" + kw4 + "</span> ");
                                                        text = text.replaceAll(" " + kw5 + " ", " <span style='color: #FF7F00; font-weight: bold;'>" + kw5 + "</span> ");

                                                        HTMLFlow label = new HTMLFlow("<div style='font-family: Georgia,Serif,Arial; font-size: 11pt;'>" + text + "</div>");
                                                        label.setWidth100();
                                                        label.setHeight100();
                                                        label.setPadding(5);

                                                        com.smartgwt.client.widgets.Window window = new com.smartgwt.client.widgets.Window();
                                                        window.setTitle("File " + file + ".xml, segment " + segment);
                                                        window.setWidth(640);
                                                        window.setHeight(480);
                                                        window.setCanDragReposition(true);
                                                        window.setCanDragResize(true);
                                                        window.addItem(label);
                                                        window.draw();
                                                    }

                                                    @Override
                                                    public void onFailure(Throwable caught) {
                                                        SC.say("Error", caught.getMessage());
                                                    }
                                                });
                                            }
                                        });
                                        recordCanvas.addMember(previewTextImg);
                                        return recordCanvas;
                                    } else
                                        return null;
                                }
                            };
                            locTopicCorrGrid.setHeight100();
                            ListGridField fileIdField = new ListGridField("file", "File", 50);
                            ListGridField titleField = new ListGridField("title", "Title");
                            ListGridField lastNameField = new ListGridField("last_name", "LastName",40);
                            ListGridField genderField = new ListGridField("gender", "Gender"); 
                            ListGridField nationField = new ListGridField("nation", "Nation"); 
                            ListGridField yearField = new ListGridField("year", "Year", 30);
                            yearField.setType(ListGridFieldType.INTEGER);
        //                    ListGridField topicIdField = new ListGridField("topic_id", "Topic Id", 50);
//                          topicIdField.setType(ListGridFieldType.INTEGER);
                            ListGridField segmentIdField = new ListGridField("segment", "Segment", 50);
                            segmentIdField.setType(ListGridFieldType.INTEGER);
                            ListGridField numTypesField = new ListGridField("num_types", "NumTypes", 60);
                            numTypesField.setType(ListGridFieldType.INTEGER);
                            ListGridField numTokensField = new ListGridField("num_tokens", "NumTokens", 60);
                            numTokensField.setType(ListGridFieldType.INTEGER);
                            ListGridField actionsField = new ListGridField("actions", "Actions", 70);
                            locTopicCorrGrid.setFields(new ListGridField[] { fileIdField, titleField, yearField, lastNameField, nationField, genderField, segmentIdField, numTypesField, numTokensField, actionsField });
                            locTopicCorrGrid.setShowRecordComponents(true);
                            locTopicCorrGrid.setShowRecordComponentsByCell(true);
                            locTopicCorrGrid.setCanRemoveRecords(false);
                            locTopicCorrGrid.setEmptyMessage("Fetching data, please wait...");
                            locTopicCorrGrid.setData(new RecordList());

                            window.addItem(locTopicCorrGrid);
                            window.draw();

                            topicModelDataService.getLocationCorrelationForTopic(topicId, new AsyncCallback<List<LocTopicCorrMeta>>() {

                                @Override
                                public void onSuccess(List<LocTopicCorrMeta> result) {
                                    int n = result.size();
                                    TopicCorrRecord[] records = new TopicCorrRecord[n];
                                    for (int i = 0; i < n; i++)
                                        records[i] = new TopicCorrRecord(result.get(i));
                                    locTopicCorrGrid.setEmptyMessage("No items to show");
                                    locTopicCorrGrid.setData(records);
                                }

                                @Override
                                public void onFailure(Throwable caught) {
                                    locTopicCorrGrid.setEmptyMessage("No items to show");
                                    SC.say("Error", caught.getMessage());
                                }
                            });
                        }
                    });

                    ImgButton tagcloudImg = new ImgButton();
                    tagcloudImg.setShowDown(false);
                    tagcloudImg.setShowRollOver(false);
                    tagcloudImg.setAlign(Alignment.CENTER);
                    tagcloudImg.setSrc("actions/color_swatch.png");
                    tagcloudImg.setPrompt("Topic Tag Cloud");
                    tagcloudImg.setHeight(16);
                    tagcloudImg.setWidth(16);
                    tagcloudImg.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent event) {
                            int topic = record.getAttributeAsInt("topic_id");
                            Window.open("tagcloud.html?topic=" + topic, "_topicTagCloud" + topic, "toolbar=0,menubar=0,resizable=0,width=520,height=520");
                        }
                    });

                    recordCanvas.addMember(corrImg);
                    recordCanvas.addMember(tagcloudImg);
                    return recordCanvas;
                } else
                    return null;
            }
        };
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
        ListGridField actionsField = new ListGridField("actions", "Actions");
        actionsField.setWidth(100);
        topicsGrid.setFields(new ListGridField[] { topicIdField, keyword1Field, keyword2Field, keyword3Field, keyword4Field, keyword5Field, actionsField });
        topicsGrid.setHeaderSpans(new HeaderSpan("Topics List", new String[] { "topic_id", "keyword1", "keyword2", "keyword3", "keyword4", "keyword5", "actions" }));
        topicsGrid.setShowRecordComponents(true);
        topicsGrid.setShowRecordComponentsByCell(true);
        topicsGrid.setCanRemoveRecords(false);

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
