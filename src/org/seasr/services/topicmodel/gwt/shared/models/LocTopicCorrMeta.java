package org.seasr.services.topicmodel.gwt.shared.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LocTopicCorrMeta implements IsSerializable {
    private String title;
    private int topicId;
    private String fileName;
    private int segmentId;
    private int freq;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(int segmentId) {
        this.segmentId = segmentId;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }
}
