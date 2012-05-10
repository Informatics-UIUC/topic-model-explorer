package org.seasr.services.topicmodel.gwt.client.model;

import org.seasr.services.topicmodel.gwt.shared.models.FileMeta;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class FileRecord extends ListGridRecord {

    public FileRecord() { }

    public FileRecord(FileMeta fileMeta) {
        setFile(fileMeta.getFileName());
        setTitle(fileMeta.getTitle());
        setNation(fileMeta.getNation());
        setGender(fileMeta.getGender());
        setYear(fileMeta.getPublicationYear());
        setFirstName(fileMeta.getAuthorFirstName());
        setLastName(fileMeta.getAuthorLastName());
        setCount(fileMeta.getCount());
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

    public void setYear(int year) {
        setAttribute("year", year);
    }

    public void setFirstName(String firstName) {
        setAttribute("first_name", firstName);
    }

    public void setLastName(String lastName) {
        setAttribute("last_name", lastName);
    }

    public void setCount(int count) {
        setAttribute("count", count);
    }
}
