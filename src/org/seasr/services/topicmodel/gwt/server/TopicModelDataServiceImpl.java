package org.seasr.services.topicmodel.gwt.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.seasr.services.topicmodel.gwt.client.TopicModelDataService;
import org.seasr.services.topicmodel.gwt.server.utils.DBUtils;
import org.seasr.services.topicmodel.gwt.shared.models.FileMeta;
import org.seasr.services.topicmodel.gwt.shared.models.LocTopicCorrMeta;
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
                fileMeta.setNation(resultSet.getString("nation"));
                fileMeta.setGender(resultSet.getString("gender"));
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

    @Override
    public List<LocTopicCorrMeta> getLocationCorrelationForTopic(int topicId) throws Exception {
        Connection connection = null;
        Statement stmt = null;
        try {
            Properties dbConfig = ConfigManager.getConfigProperties();
            connection = DBConnectionPoolManager.getConnectionPool(dbConfig).getConnection();
            stmt = connection.createStatement();

            List<LocTopicCorrMeta> locTopicCorrelations = new ArrayList<LocTopicCorrMeta>();

            String query = String.format(dbConfig.getProperty("query_loc_topic_correlation").trim(), topicId);
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                LocTopicCorrMeta locTopicCorrMeta = new LocTopicCorrMeta();
                locTopicCorrMeta.setFileName(resultSet.getString("file"));
                locTopicCorrMeta.setTitle(resultSet.getString("title"));
                locTopicCorrMeta.setNation(resultSet.getString("nation"));
                locTopicCorrMeta.setGender(resultSet.getString("gender"));
                locTopicCorrMeta.setLastName(resultSet.getString("last_name"));
                locTopicCorrMeta.setYear(resultSet.getInt("year"));
                locTopicCorrMeta.setSegmentId(resultSet.getInt("segment"));
                locTopicCorrMeta.setNumTypes(resultSet.getInt("num_types"));
                locTopicCorrMeta.setNumTokens(resultSet.getInt("num_tokens"));
                locTopicCorrMeta.setTopicId(resultSet.getInt("topic_id"));
                locTopicCorrelations.add(locTopicCorrMeta);
            }

            return locTopicCorrelations;
        }
        finally {
            DBUtils.releaseConnection(connection, stmt);
        }
    }

    @Override
    public String getTextForFileSegment(String file, int segment) throws Exception {
        Properties config = ConfigManager.getConfigProperties();
        String location = config.getProperty("text_zip_location");

        File zip = new File(location, String.format("%s.xml.zip", file));
        if (!zip.exists()) throw new FileNotFoundException(zip.toString());

        FileInputStream fis = new FileInputStream(zip);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry entry;
        StringBuilder contents = new StringBuilder();
        boolean found = false;

        try {
            while ((entry = zis.getNextEntry()) != null) {
                try {
                    if (!entry.getName().equalsIgnoreCase("segment_" + segment + ".txt")) continue;

                    found = true;

                    BufferedReader reader = new BufferedReader(new InputStreamReader(zis));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        contents.append(line);
                        contents.append(System.getProperty("line.separator"));
                    }

                    break;
                }
                finally {
                    zis.closeEntry();
                }
            }
        }
        finally {
            zis.close();
        }

        if (!found) throw new Exception("Segment " + segment + " was not found in file " + file);

        return contents.toString();
    }

    @Override
	public List<String> getTopWordsToHighlight(int topicId, String file, int segment) throws Exception {
        Connection connection = null;
        Statement stmt = null;
        try {
            Properties dbConfig = ConfigManager.getConfigProperties();
            connection = DBConnectionPoolManager.getConnectionPool(dbConfig).getConnection();
            stmt = connection.createStatement();

            List<String> topWordsToHighlight = new ArrayList<String>();

            String query = String.format(dbConfig.getProperty("query_highlight_top_words").trim(), topicId, file, segment);
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                topWordsToHighlight.add(resultSet.getString("location").trim());
            }

            return topWordsToHighlight;
        }
        finally {
            DBUtils.releaseConnection(connection, stmt);
        }
    }
}
