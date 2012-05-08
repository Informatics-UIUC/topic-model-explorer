package org.seasr.services.topicmodel.gwt.client.model;

import org.seasr.services.topicmodel.gwt.shared.models.LocTopicCorrMeta;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class TopicCorrRecord extends ListGridRecord {

    public TopicCorrRecord() { }

    public TopicCorrRecord(LocTopicCorrMeta fileMeta) {
        setFile(fileMeta.getFileName());
        setTitle(fileMeta.getTitle());
        setSegment(fileMeta.getSegmentId());
        setTopicId(fileMeta.getTopicId());
        setFreq(fileMeta.getFreq());
    }

    public void setFile(String file) {
        setAttribute("file", file);
    }

    public void setTitle(String title) {
        setAttribute("title", title);
    }

    public void setSegment(int segment) {
        setAttribute("segment", segment);
    }

    public void setTopicId(int topicId) {
        setAttribute("topic_id", topicId);
    }

    public void setFreq(int freq) {
        setAttribute("freq", freq);
    }
}
