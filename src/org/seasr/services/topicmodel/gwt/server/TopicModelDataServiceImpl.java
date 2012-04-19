package org.seasr.services.topicmodel.gwt.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.seasr.services.topicmodel.gwt.client.TopicModelDataService;
import org.seasr.services.topicmodel.gwt.server.utils.DBUtils;
import org.seasr.services.topicmodel.gwt.shared.models.FileMeta;
import org.seasr.services.topicmodel.gwt.shared.models.TopicMeta;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class TopicModelDataServiceImpl extends RemoteServiceServlet implements TopicModelDataService {

    private static final String QUERY_FILES =
            "SELECT file, title, last_name, first_name, year, count " +
            "FROM (" +
            "   SELECT DISTINCT (file)" +
            "   FROM tei_locations_topics" +
            "   WHERE location = '%s'" +
            ") t1 " +
            "INNER JOIN tei_title_author_meta USING (file) " +
            "INNER JOIN (" +
            "   SELECT file, SUM(count) AS count" +
            "   FROM tei_locations_opennlp" +
            "   WHERE location = '%1$s'" +
            "   GROUP BY file" +
            ") t2 USING (file) " +
            "ORDER BY count DESC, year, last_name, first_name";

    private static final String QUERY_TOPICS =
            "SELECT topic_id," +
            "        MAX(CASE WHEN rownum = 1 THEN location ELSE NULL END) as keyword1," +
            "        MAX(CASE WHEN rownum = 2 THEN location ELSE NULL END) as keyword2," +
            "        MAX(CASE WHEN rownum = 3 THEN location ELSE NULL END) as keyword3," +
            "        MAX(CASE WHEN rownum = 4 THEN location ELSE NULL END) as keyword4," +
            "        MAX(CASE WHEN rownum = 5 THEN location ELSE NULL END) as keyword5 " +
            "FROM (" +
            "   SELECT topic_id, location, count, rownum" +
            "   FROM (" +
            "      SELECT topic_id, location, count," +
            "           IF(@PREV <> topic_id, @ROWNUM := 1, @ROWNUM := @ROWNUM+1) AS rownum," +
            "           @PREV := topic_id" +
            "      FROM (" +
            "         SELECT topic_id, location, count" +
            "         FROM tei_locations_topic_keywords" +
            "         JOIN (" +
            "            SELECT DISTINCT(topic_id)" +
            "            FROM tei_locations_topics" +
            "            WHERE location = '%s'" +
            "         ) t1 " +
            "         USING (topic_id)" +
            "         ORDER BY topic_id, count DESC" +
            "      ) u, (SELECT @ROWNUM := 0, @PREV := -1) r" +
            "   ) t2" +
            "   WHERE rownum <= 5" +
            ") t3 " +
            "GROUP BY topic_id";


    @Override
    public List<FileMeta> getFilesMetaForToken(String token) throws Exception {
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = DBConnectionPoolManager.getConnectionPool().getConnection();
            stmt = connection.createStatement();

            List<FileMeta> files = new ArrayList<FileMeta>();

            String query = String.format(QUERY_FILES, token);
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
            connection = DBConnectionPoolManager.getConnectionPool().getConnection();
            stmt = connection.createStatement();

            List<TopicMeta> topics = new ArrayList<TopicMeta>();

            String query = String.format(QUERY_TOPICS, token);
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
