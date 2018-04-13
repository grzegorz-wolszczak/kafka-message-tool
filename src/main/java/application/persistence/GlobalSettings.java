package application.persistence;

import application.logging.LogLevel;
import application.model.XmlElementNames;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@EqualsAndHashCode
@ToString
@XmlRootElement(name = XmlElementNames.GLOBAL_SETTINGS)
public class GlobalSettings {

    private ObjectProperty<LogLevel> logLevel = new SimpleObjectProperty<>(LogLevel.DEBUG);
    private StringProperty runBeforeFirstMessageSharedScriptContent = new SimpleStringProperty("");

    @XmlElement(name = XmlElementNames.GLOBAL_LOG_LEVEL)
    public LogLevel getLogLevel() {
        return logLevel.get();
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel.set(logLevel);
    }


    public ObjectProperty<LogLevel> logLevelProperty() {
        return logLevel;
    }

    public void fillFrom(GlobalSettings other) {
        if (other == null) {
            return;
        }
        setLogLevel(other.getLogLevel());
        setRunBeforeFirstMessageSharedScriptContent(other.getRunBeforeFirstMessageSharedScriptContent());
    }

    @XmlElement(name = XmlElementNames.BEFORE_FIST_MESSAGE_SHARED_SCRIPT_CONTENT)
    public String getRunBeforeFirstMessageSharedScriptContent() {
        return runBeforeFirstMessageSharedScriptContent.get();
    }

    public StringProperty runBeforeFirstMessageSharedScriptContentProperty() {
        return runBeforeFirstMessageSharedScriptContent;
    }

    public void setRunBeforeFirstMessageSharedScriptContent(String runBeforeFirstMessageSharedScriptContent) {
        this.runBeforeFirstMessageSharedScriptContent.set(runBeforeFirstMessageSharedScriptContent);
    }
}