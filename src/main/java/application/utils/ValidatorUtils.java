package application.utils;

import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.function.Consumer;
import java.util.function.Function;

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

    public static Boolean isValidGraterThanZeroInteger(String value) {
        if (!isStringIdentifierValid(value)) {
            return false;
        }
        try {
            return Integer.parseUnsignedInt(value) > 0;
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

        final TextFormatter<Object> decimalTextFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().isEmpty()) {
                return change;
            }
            ParsePosition parsePosition = new ParsePosition(0);
            Object object = format.parse(change.getControlNewText(), parsePosition);

            if (object == null || parsePosition.getIndex() < change.getControlNewText().length()) {
                return null;
            } else {
                return change;
            }
        });
        textField.setTextFormatter(decimalTextFormatter);
    }

    public static boolean isNumberLessEqualThan(String e, int upperBound) {
        return StringUtils.isNumeric(e) && Integer.parseUnsignedInt(e) <= upperBound;
    }

    public static void configureSpinner(Spinner<Integer> spinner, IntegerProperty referenceProperty, int minValue, int maxValue) {
        spinner.setEditable(true);
        spinner.setTooltip(TooltipCreator.createFrom("Max value: " + maxValue));
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(minValue,
                                                                                   Integer.MAX_VALUE,
                                                                                   referenceProperty.get()));
        GuiUtils.configureTextFieldToAcceptOnlyValidData(spinner.getEditor(),
                                                         stringConsumer(referenceProperty),
                                                         validationFunc(maxValue));
        configureTextFieldToAcceptOnlyDecimalValues(spinner.getEditor());
    }

    private static Function<String, Boolean> validationFunc(int maxValue) {
        return (v) -> isNumberLessEqualThan(v, maxValue);
    }

    private static Consumer<String> stringConsumer(IntegerProperty referenceProperty) {
        return (v) -> referenceProperty.set(Integer.valueOf(v));
    }
}
