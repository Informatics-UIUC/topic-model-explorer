package org.seasr.services.topicmodel.gwt.client;

import java.util.List;

import org.seasr.services.topicmodel.gwt.shared.models.FileMeta;
import org.seasr.services.topicmodel.gwt.shared.models.LocTopicCorrMeta;
import org.seasr.services.topicmodel.gwt.shared.models.TopicMeta;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("topicModelDataService")
public interface TopicModelDataService extends RemoteService {

    List<FileMeta> getFilesMetaForToken(String token) throws Exception;
    List<TopicMeta> getTopicsForToken(String token) throws Exception;
    List<LocTopicCorrMeta> getLocationCorrelationForTopic(int topicId) throws Exception;
    List<String> getTopWordsToHighlight(int topicId, String file, int segment) throws Exception;
    String getTextForFileSegment(String file, int segment) throws Exception;
}
