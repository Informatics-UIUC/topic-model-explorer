package org.seasr.services.topicmodel.gwt.client;

import java.util.List;

import org.seasr.services.topicmodel.gwt.shared.models.FileMeta;
import org.seasr.services.topicmodel.gwt.shared.models.TopicMeta;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TopicModelDataServiceAsync {

    void getFilesMetaForToken(String token, AsyncCallback<List<FileMeta>> callback);

    void getTopicsForToken(String token, AsyncCallback<List<TopicMeta>> callback);

}
