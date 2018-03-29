package application.utils;

import java.util.Objects;

public class HostPortValue {
    String host;
    int port;

    public HostPortValue(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static HostPortValue from(HostInfo hostInfo) {
        return new HostPortValue(hostInfo.getHostname(),
                                 Integer.parseInt(hostInfo.getPort()));
    }

    public String toHostString() {
        return String.format("%s:%d", host, port);
    }

    @Override
    public String toString() {
        return "HostPortValue{" +
            "host='" + host + '\'' +
            ", port=" + port +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HostPortValue)) {
            return false;
        }
        HostPortValue that = (HostPortValue) o;
        return port == that.port &&
            Objects.equals(host, that.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}
