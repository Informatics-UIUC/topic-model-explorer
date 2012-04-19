package org.seasr.services.topicmodel.gwt.server;

import java.util.Properties;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class DBConnectionPoolManager {

    private static BoneCP connectionPool;

    public static BoneCP getConnectionPool(Properties dbConfig) throws Exception {
        if (connectionPool != null)
            return connectionPool;

        Class.forName(dbConfig.getProperty("db_driver"));

        // setup the connection pool
        BoneCPConfig config = new BoneCPConfig();
        config.setJdbcUrl(dbConfig.getProperty("db_url"));
        if (dbConfig.containsKey("db_user"))
            config.setUsername(dbConfig.getProperty("db_user"));
        if (dbConfig.containsKey("db_passwd"))
            config.setPassword(dbConfig.getProperty("db_passwd"));
        config.setMinConnectionsPerPartition(Integer.parseInt(dbConfig.getProperty("min_connections_per_partition", "2")));
        config.setMaxConnectionsPerPartition(Integer.parseInt(dbConfig.getProperty("max_connections_per_partition", "10")));
        config.setPartitionCount(Integer.parseInt(dbConfig.getProperty("partition_count", "1")));
        connectionPool = new BoneCP(config);

        return connectionPool;
    }
}
