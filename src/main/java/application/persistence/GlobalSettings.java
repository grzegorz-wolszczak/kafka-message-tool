package application.persistence;

import application.notifications.LogLevel;
import application.model.XmlElementNames;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@EqualsAndHashCode
@ToString
@XmlRootElement(name = XmlElementNames.GLOBAL_SETTINGS)
public class GlobalSettings {

    private ObjectProperty<LogLevel> logLevel = new SimpleObjectProperty<>(LogLevel.DEBUG);

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
    }
}