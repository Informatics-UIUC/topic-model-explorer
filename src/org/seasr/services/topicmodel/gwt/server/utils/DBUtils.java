package org.seasr.services.topicmodel.gwt.server.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DBUtils {

    /**
     * Rolls back the last DB transaction for a connection
     *
     * @param connection The connection
     * @return True if success / False otherwise
     */
    public static boolean rollbackTransaction(Connection connection) {
        if (connection == null) return false;

        try {
            connection.rollback();
            return true;
        }
        catch (SQLException e) {
            return false;
        }
    }

    /**
     * Returns a connection back to the connection pool
     *
     * @param connection The connection
     * @param statements  (Optional) Any ResultSet(s) that need to be closed before the connection is released
     */
    public static void releaseConnection(Connection connection, Statement... statements) {
        if (statements != null)
            for (Statement stmt : statements)
                closeStatement(stmt);

        if (connection != null) {
            try {
                connection.close();
            }
            catch (Exception e) { }
        }
    }

    /**
     * Closes a Statement
     *
     * @param stmt The Statement
     */
    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            }
            catch (SQLException e) { }
        }
    }
}

