package application.utils;

import java.util.Enumeration;
import java.util.Properties;

public class PropertiesUtils{

    public static String prettyProperties(Properties p) {
        StringBuilder b = new StringBuilder();
        Enumeration keys = p.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String value = (String) p.get(key);
            b.append(String.format("\t%s = %s%n", key, value));
        }
        return b.toString();
    }
}
