package org.seasr.services.topicmodel.gwt.server;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class DBConnectionPoolManager {

    private static BoneCP connectionPool;

    public static BoneCP getConnectionPool() throws Exception {
        if (connectionPool != null)
            return connectionPool;

        Class.forName("com.mysql.jdbc.Driver");

        // setup the connection pool
        BoneCPConfig config = new BoneCPConfig();
        config.setJdbcUrl("jdbc:mysql://leovip021.ncsa.uiuc.edu/matt");
        config.setUsername("seasr");
        config.setPassword("m3andr3");
        config.setMinConnectionsPerPartition(2);
        config.setMaxConnectionsPerPartition(10);
        config.setPartitionCount(1);
        connectionPool = new BoneCP(config);

        return connectionPool;
    }
}
