package org.seasr.services.topicmodel.gwt.client.model;

import org.seasr.services.topicmodel.gwt.shared.models.LocTopicCorrMeta;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class TopicCorrRecord extends ListGridRecord {

    public TopicCorrRecord() { }

    public TopicCorrRecord(LocTopicCorrMeta fileMeta) {
        setFile(fileMeta.getFileName());
        setTitle(fileMeta.getTitle());
        setNation(fileMeta.getNation());
        setGender(fileMeta.getGender());
        setLastName(fileMeta.getLastName());
        setYear(fileMeta.getYear());
        setSegment(fileMeta.getSegmentId());
        setTopicId(fileMeta.getTopicId());
        setNumTypes(fileMeta.getNumTypes());
        setNumTokens(fileMeta.getNumTokens());
    }

	public void setFile(String file) {
        setAttribute("file", file);
    }

    public void setTitle(String title) {
        setAttribute("title", title);
    }
    
    public void setNation(String nation) {
        setAttribute("nation", nation);
    }
    
    private void setGender(String gender) {
    	setAttribute("gender", gender);
	}
    
    private void setLastName(String lastName) {
    	setAttribute("last_name", lastName);
	}

    public void setYear(int year) {
        setAttribute("year", year);
    }
    
    public void setSegment(int segment) {
        setAttribute("segment", segment);
    }

    public void setTopicId(int topicId) {
        setAttribute("topic_id", topicId);
    }

    public void setNumTypes(int numTypes) {
        setAttribute("num_types", numTypes);
    }
    
    public void setNumTokens(int numTokens) {
        setAttribute("num_tokens", numTokens);
    }
}
