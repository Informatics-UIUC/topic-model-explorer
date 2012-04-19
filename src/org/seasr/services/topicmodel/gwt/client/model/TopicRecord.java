package org.seasr.services.topicmodel.gwt.client.model;

import org.seasr.services.topicmodel.gwt.shared.models.TopicMeta;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class TopicRecord extends ListGridRecord {

    public TopicRecord() { }

    public TopicRecord(TopicMeta topicMeta) {
        setTopicId(topicMeta.getTopicId());
        setKeyword1(topicMeta.getKeyword1());
        setKeyword2(topicMeta.getKeyword2());
        setKeyword3(topicMeta.getKeyword3());
        setKeyword4(topicMeta.getKeyword4());
        setKeyword5(topicMeta.getKeyword5());
    }

    public void setTopicId(int topicId) {
        setAttribute("topic_id", topicId);
    }

    public void setKeyword1(String keyword1) {
        setAttribute("keyword1", keyword1);
    }

    public void setKeyword2(String keyword2) {
        setAttribute("keyword2", keyword2);
    }

    public void setKeyword3(String keyword3) {
        setAttribute("keyword3", keyword3);
    }

    public void setKeyword4(String keyword4) {
        setAttribute("keyword4", keyword4);
    }

    public void setKeyword5(String keyword5) {
        setAttribute("keyword5", keyword5);
    }
}
