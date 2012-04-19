package org.seasr.services.topicmodel.gwt.shared.models;

import com.google.gwt.user.client.rpc.IsSerializable;

public class WordCount implements IsSerializable {

    private String word;
    private int count;

    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "(" + word + ": " + count + ")";
    }
}
