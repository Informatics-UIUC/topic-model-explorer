package org.seasr.services.topicmodel.gwt.server;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.seasr.services.topicmodel.gwt.server.utils.DBUtils;
import org.seasr.services.topicmodel.gwt.shared.models.WordCount;

/**
 * Servlet implementation class TopicKeywordCountsDataService
 */
public class TopicKeywordCountsDataService extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String QUERY_WORDCOUNTS = "SELECT location, count FROM tei_locations_topic_keywords WHERE topic_id=%s AND count > 0";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopicKeywordCountsDataService() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String topic = request.getParameter("topic");
	    String callback = request.getParameter("callback");

	    if (topic == null) {
	        response.setStatus(HttpStatus.SC_BAD_REQUEST);
	        return;
	    }

        JSONArray jaWordCounts = new JSONArray();

        Connection connection = null;
        Statement stmt = null;
        try {
            connection = DBConnectionPoolManager.getConnectionPool().getConnection();
            stmt = connection.createStatement();

            List<WordCount> wordCounts = new ArrayList<WordCount>();
            String query = String.format(QUERY_WORDCOUNTS, topic);
            ResultSet resultSet = stmt.executeQuery(query);
            while (resultSet.next()) {
                WordCount wordCount = new WordCount();
                wordCount.setWord(resultSet.getString("location"));
                wordCount.setCount(resultSet.getInt("count"));
                wordCounts.add(wordCount);
            }

            for (WordCount wordCount : wordCounts) {
                JSONObject joWordCount = new JSONObject();
                joWordCount.put("word", wordCount.getWord());
                joWordCount.put("count", wordCount.getCount());
                jaWordCounts.put(joWordCount);
            }
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
        finally {
            DBUtils.releaseConnection(connection, stmt);
        }

	    response.setStatus(HttpStatus.SC_OK);
	    response.setContentType("text/javascript");
	    response.getWriter().println(
	            callback != null ?
	                    String.format("%s(%s)", callback, jaWordCounts.toString()) :
	                    jaWordCounts.toString());
	}
}
