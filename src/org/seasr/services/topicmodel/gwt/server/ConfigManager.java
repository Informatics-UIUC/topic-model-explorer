package org.seasr.services.topicmodel.gwt.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class ConfigManager {

    private static Properties configProps;

    public static Properties getConfigProperties() throws InvalidPropertiesFormatException, IOException {
        if (configProps != null)
            return configProps;

        InputStream configData = Thread.currentThread().getContextClassLoader().getResourceAsStream("/db_config.xml");
        if (configData == null)
            throw new FileNotFoundException("db_config.xml");

        configProps = new Properties();
        configProps.loadFromXML(configData);

        boolean configValid = true;
        configValid &= configProps.containsKey("db_driver");
        configValid &= configProps.containsKey("db_url");
        configValid &= configProps.containsKey("query_wordcounts");
        configValid &= configProps.containsKey("query_files");
        configValid &= configProps.containsKey("query_topics");
        configValid &= configProps.containsKey("query_loc_topic_correlation");
        configValid &= configProps.containsKey("text_zip_location");

        if (!configValid)
            throw new IOException("Required configuration properties missing from db_config.xml!");

        return configProps;
    }
}
