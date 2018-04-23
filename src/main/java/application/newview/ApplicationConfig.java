package application.newview;

import application.logging.LogLevel;
import application.model.FromPojoConverter;
import application.model.ModelDataProxy;
import application.persistence.GlobalSettings;
import application.persistence.GuiSettings;
import application.persistence.XmlFileConfig;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;

public class ApplicationConfig {
    private static XmlFileConfig xmlFileConfig;
    private static GuiSettings guiSettings;
    private static GlobalSettings globalSettings;

    public static void load(ModelDataProxy modelDataProxy) {
        guiSettings = new GuiSettings();
        globalSettings = new GlobalSettings();
        xmlFileConfig = new XmlFileConfig(modelDataProxy,
                                          new FromPojoConverter(modelDataProxy),
                                          guiSettings,
                                          globalSettings);
        xmlFileConfig.load();
    }

    public static LogLevel getLogLevel() {
        return globalSettings.getLogLevel();
    }

    public static DoubleProperty mainWindowWidthProperty() {
        return guiSettings.mainWindowWidthProperty();
    }

    public static DoubleProperty mainWindowHeightProperty() {
        return guiSettings.mainWindowHeightProperty();
    }

    public static DoubleProperty masterDetailDividerPositionProperty(){
        return guiSettings.mainWindowSplitPaneDividerPositionProperty();
    }

    public static BooleanProperty messagesViewButtonSelectedProperty(){
        return guiSettings.messagesViewButtonSelectedProperty();
    }

    public static BooleanProperty problemsViewButtonSelectedProperty(){
        return guiSettings.problemsViewButtonSelectedProperty();
    }

    public static void save() {
        xmlFileConfig.save();
    }
}
