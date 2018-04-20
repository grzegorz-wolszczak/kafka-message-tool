package application.utils;

import application.constants.ApplicationConstants;
import application.logging.Logger;

import java.io.InputStream;
import java.util.Properties;


public class ApplicationVersionProvider {
    private static final String VERSION_NOT_FOUND = "APP_VERSION_NOT_FOUND";

    public static String get() {
        final String fileName = ApplicationConstants.VERSION_PROPERTIES_FILE_NAME;
        try {
            Properties prop = new Properties();
            try (InputStream stream = ApplicationVersionProvider.class
                .getResourceAsStream(fileName)) {
                prop.load(stream);
            }
            return prop.getProperty("version", VERSION_NOT_FOUND);
        } catch (Throwable e) {
            //e.printStackTrace();
            Logger.error(String.format("Could not find/loadOnAnchorPane file %s",fileName));
            return VERSION_NOT_FOUND;
        }
    }
}
