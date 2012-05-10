package org.seasr.services.topicmodel.gwt.shared.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LocTopicCorrMeta implements IsSerializable {
    private String title;
    private String nation;
    private String gender;
    private String lastName;
    private int year;
    private int topicId;
    private String fileName;
    private int segmentId;
    private int numTypes;
    private int numTokens;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }
    
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }
    
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public int getNumTypes() {
        return numTypes;
    }

    public void setNumTypes(int numTypes) {
        this.numTypes = numTypes;
    }
    
    public int getNumTokens() {
        return numTokens;
    }

    public void setNumTokens(int numTokens) {
        this.numTokens = numTokens;
    }
}
