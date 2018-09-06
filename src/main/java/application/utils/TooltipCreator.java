package application.utils;

import application.model.ModelDataProxy;
import application.model.modelobjects.KafkaBrokerConfig;
import application.model.modelobjects.KafkaTopicConfig;
import application.model.modelobjects.ToolTipInfoProvider;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Optional;


public class TooltipCreator {

    private static final double OPEN_DELAY_MS = 100.0d;
    private static final double VISIBLE_DURATION_MS = Double.MAX_VALUE;
    private static final double CLOSE_DELAY_MS = 100d;

    // in java 10 - no such method
//    static {
//        updateTooltipBehavior(OPEN_DELAY_MS,
//                              VISIBLE_DURATION_MS,
//                              CLOSE_DELAY_MS,
//                              false);
//    }

    public static Tooltip createFrom(KafkaTopicConfig topicConfig, ModelDataProxy dataProxy) {

        if (null == topicConfig) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(getTopicConfigToolTipText(topicConfig));
        final Optional<KafkaBrokerConfig> brokerConfigByUuid = dataProxy.getBrokerConfigByUuid(topicConfig.getBrokerUuid());
        brokerConfigByUuid.ifPresent(brokerConfig -> {
            builder.append("\n");
            builder.append(getBrokerConfigTooltipText(brokerConfig));
        });

        return createFrom(builder.toString());
    }

    public static Tooltip createFrom(KafkaBrokerConfig brokerConfig) {
        if (null == brokerConfig) {
            return null;
        }
        return createFrom(getBrokerConfigTooltipText(brokerConfig));
    }

    public static Tooltip createFrom(ToolTipInfoProvider provider) {
        if (provider == null) {
            return null;
        }
        final StringProperty stringProperty = provider.toolTipInfoProperty();
        if (stringProperty == null) {
            return null;
        }
        final String toolTipText = stringProperty.get();
        return createFrom(toolTipText);
    }

    public static Tooltip createFrom(String toolTipText) {
        if (StringUtils.isBlank(toolTipText)) {
            return null;
        }
        Tooltip tip = new Tooltip();
        tip.setText(toolTipText);
        tip.setContentDisplay(ContentDisplay.TEXT_ONLY);
        return tip;
    }

    private static String getTopicConfigToolTipText(KafkaTopicConfig topicConfig) {
        return String.format("topic       : '%s'", topicConfig.getTopicName());
    }

    private static String getBrokerConfigTooltipText(KafkaBrokerConfig config) {
        return String.format("broker host : %s", config.getHostInfo().toHostPortString());
    }

    // hack from stack overflow to change display tooltip behaviour
    // https://stackoverflow.com/questions/26854301/how-to-control-the-javafx-tooltips-delay/27739605

    private static void updateTooltipBehavior(
        double openDelayMs,
        double visibleDurationMs,
        double closeDelayMs,
        boolean hideOnExit) {
        try {
            // Get the non public field "BEHAVIOR"
            Field fieldBehavior = Tooltip.class.getDeclaredField("BEHAVIOR");
            // Make the field accessible to be able to get and set its value
            fieldBehavior.setAccessible(true);
            // Get the value of the static field
            Object objBehavior = fieldBehavior.get(null);
            // Get the constructor of the private static inner class TooltipBehavior
            Constructor<?> constructor = objBehavior.getClass().getDeclaredConstructor(
                javafx.util.Duration.class, javafx.util.Duration.class, javafx.util.Duration.class, boolean.class
            );
            // Make the constructor accessible to be able to invoke it
            constructor.setAccessible(true);
            // Create a new instance of the private static inner class TooltipBehavior
            Object tooltipBehavior = constructor.newInstance(
                new javafx.util.Duration(openDelayMs),
                new javafx.util.Duration(visibleDurationMs),
                new javafx.util.Duration(closeDelayMs),
                hideOnExit
            );
            // Set the new instance of TooltipBehavior
            fieldBehavior.set(null, tooltipBehavior);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
