
package application.utils;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.lang3.StringUtils;

public class HostInfo {
    private StringProperty hostname = new SimpleStringProperty("");
    private StringProperty port = new SimpleStringProperty("0");

    public HostInfo(String hostname, int port) {
        setHostname(hostname);
        setPort(String.valueOf(port));
    }

    public String getHostname() {
        return hostname.get();
    }

    public void setHostname(String hostname) {
        if (StringUtils.isBlank(hostname)) {
            throw new IllegalArgumentException("Host is empty");
        }
        this.hostname.set(hostname);
    }

    public StringProperty hostnameProperty() {
        return hostname;
    }

    public String getPort() {
        return port.get();
    }

    public void setPort(String port) {
        if (StringUtils.isBlank(port)) {
            throw new IllegalArgumentException("Port is empty");
        }
        this.port.set(port);
    }

    public StringProperty portProperty() {
        return port;
    }

    public String toHostPortString() {
        return String.format("%s:%s", hostname.get(), port.get());
    }

    @Override
    public String toString() {
        return "HostInfo{" +
            "hostname=" + getHostname() +
            ", port=" + getPort() +
            '}';
    }
}
