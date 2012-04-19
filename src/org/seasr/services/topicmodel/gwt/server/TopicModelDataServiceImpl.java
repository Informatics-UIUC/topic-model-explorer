package org.seasr.services.topicmodel.gwt.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.seasr.services.topicmodel.gwt.client.TopicModelDataService;
import org.seasr.services.topicmodel.gwt.server.utils.DBUtils;
import org.seasr.services.topicmodel.gwt.shared.models.FileMeta;
import org.seasr.services.topicmodel.gwt.shared.models.TopicMeta;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class TopicModelDataServiceImpl extends RemoteServiceServlet implements TopicModelDataService {

    @Override
    public List<FileMeta> getFilesMetaForToken(String token) throws Exception {
        Connection connection = null;
        Statement stmt = null;
        try {
            Properties dbConfig = ConfigManager.getConfigProperties();
            connection = DBConnectionPoolManager.getConnectionPool(dbConfig).getConnection();
            stmt = connection.createStatement();

            List<FileMeta> files = new ArrayList<FileMeta>();

            String query = String.format(dbConfig.getProperty("query_files").trim(), token);
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                FileMeta fileMeta = new FileMeta();
                fileMeta.setAuthorFirstName(resultSet.getString("first_name"));
                fileMeta.setAuthorLastName(resultSet.getString("last_name"));
                fileMeta.setFileName(resultSet.getString("file"));
                fileMeta.setPublicationYear(resultSet.getInt("year"));
                fileMeta.setTitle(resultSet.getString("title"));
                fileMeta.setCount(resultSet.getInt("count"));
                files.add(fileMeta);
            }

            return files;
        }
        finally {
            DBUtils.releaseConnection(connection, stmt);
        }
    }

    @Override
    public List<TopicMeta> getTopicsForToken(String token) throws Exception {
        Connection connection = null;
        Statement stmt = null;
        try {
            Properties dbConfig = ConfigManager.getConfigProperties();
            connection = DBConnectionPoolManager.getConnectionPool(dbConfig).getConnection();
            stmt = connection.createStatement();

            List<TopicMeta> topics = new ArrayList<TopicMeta>();

            String query = String.format(dbConfig.getProperty("query_topics").trim(), token);
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                TopicMeta topicMeta = new TopicMeta();
                topicMeta.setTopicId(resultSet.getInt("topic_id"));
                topicMeta.setKeyword1(resultSet.getString("keyword1"));
                topicMeta.setKeyword2(resultSet.getString("keyword2"));
                topicMeta.setKeyword3(resultSet.getString("keyword3"));
                topicMeta.setKeyword4(resultSet.getString("keyword4"));
                topicMeta.setKeyword5(resultSet.getString("keyword5"));

                topics.add(topicMeta);
            }

            return topics;
        }
        finally {
            DBUtils.releaseConnection(connection, stmt);
        }
    }

}
