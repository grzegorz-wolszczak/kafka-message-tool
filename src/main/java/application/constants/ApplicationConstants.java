
package application.constants;

import groovy.transform.CompileStatic;

@CompileStatic
public class ApplicationConstants {
    public static final String APPLICATION_NAME = "Kafka Message Tool";
    public static final String CONFIG_FILE_NAME = "KafkaMessageToolConfig.xml";
    public static final String AUTHOR = "Grzegorz Wolszczak";
    public static final String GITHUB_WEBSITE = "https://github.com/grzegorz-wolszczak/kafka-message-tool";
    public static final String INVALID_TEXT_FIELD_INPUT_PSEUDO_CLASS_NAME = "error";
    public static final String GLOBAL_CSS_FILE_NAME = "/fx_global.css";
    public static final String VERSION_PROPERTIES_FILE_NAME = "/version.properties";
    public static final String DEFAULT_FETCH_TIMEOUT = "5000";
    public static final String DEFAULT_CONSUMER_GROUP_ID = "DefaultConsumerGroup";
    public static final String DEFAULT_MESSAGE_KEY = "DefaultMessageKey";
    public static final int HOSTNAME_REACHABLE_TIMEOUT_MS = 2000; // warning, less than 2000 seconds causes timeouts



    public static final long FUTURE_GET_TIMEOUT_MS = 5000L;
    public static final long DESCRIBE_CONSUMER_METEADATA_TIMEOUT_MS = 2000L;
    public static final long CLOSE_CONNECTION_TIMEOUT_MS = 2000L;
    public static final long DELETE_TOPIC_FUTURE_GET_TIMEOUT_MS = 2000L;
    public static final String DEFAULT_NEW_TOPIC_NAME = "test";
    public static final String DEFAULT_NEW_TOPIC_CONFIG_NAME = "<empty name>";
    public static final String GROOVY_KEYWORDS_STYLES_CSS = "/groovy_keywords_styles.css";
    public static final String JSON_STYLES_CSS = "/json_styles.css";

    public static final String DEFAULT_BROKER_CONFIG_NAME = "<new broker config>";
    public static final String DEFAULT_PORT_AS_STRING = "9092";
    public static final String DEFAULT_HOSTNAME = "localhost";
    public static final String DEFAULT_SENDER_CONFIG_NAME = "<new message sender config>";
    public static final String DEFAULT_LISTENER_CONFIG_NAME = "<new message listener config>";
    public static final String DEFAULT_TOPIC_CONFIG_NAME = "<new topic config>";
}

