
package application.utils;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.ParsePosition;

public class ValidatorUtils {

    private static final int MAX_PORT_VALUE = 65535;
    private static final int HOUR_IN_MS = 1000 * 3600;
    private static final int MIN_MS = 1;

    public static boolean isStringIdentifierValid(String name) {
        return StringUtils.isNotBlank(name);
    }

    public static Boolean isPortValid(String port) {
        if (!isStringIdentifierValid(port)) {
            return false;
        }
        try {
            Integer value = Integer.valueOf(port);
            return value >= 0 && value <= MAX_PORT_VALUE;
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean isTimeoutInMsValid(String value) {
        if (!isStringIdentifierValid(value)) {
            return false;
        }
        Integer asIntValue;
        try {
            asIntValue = Integer.valueOf(value);
        } catch (Exception e) {
            return false;
        }
        return asIntValue >= MIN_MS && asIntValue <= HOUR_IN_MS;
    }

    public static void configureTextFieldToAcceptOnlyDecimalValues(TextField textField) {

        DecimalFormat format = new DecimalFormat("#");

        final TextFormatter<Object> decimalTextFormatter = new TextFormatter<>(c -> {
            if (c.getControlNewText().isEmpty()) {
                return c;
            }
            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(c.getControlNewText(), parsePosition);

            if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
                return null;
            } else {
                return c;
            }
        });
        textField.setTextFormatter(decimalTextFormatter);
    }

}
