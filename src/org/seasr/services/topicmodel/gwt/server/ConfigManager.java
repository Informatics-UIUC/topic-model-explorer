package org.seasr.services.topicmodel.gwt.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class ConfigManager {

    private static Properties configProps;

    public static Properties getConfigProperties() throws InvalidPropertiesFormatException, IOException {
        if (configProps != null)
            return configProps;

        File configFile = new File("WEB-INF" + File.separator + "db_config.xml");
        if (!configFile.exists())
            throw new FileNotFoundException(configFile.toString());

        configProps = new Properties();
        configProps.loadFromXML(new FileInputStream(configFile));

        boolean configValid = true;
        configValid &= configProps.containsKey("db_driver");
        configValid &= configProps.containsKey("db_url");
        configValid &= configProps.containsKey("query_wordcounts");
        configValid &= configProps.containsKey("query_files");
        configValid &= configProps.containsKey("query_topics");

        if (!configValid)
            throw new IOException("Required configuration properties missing from db_config.xml!");

        return configProps;
    }
}
