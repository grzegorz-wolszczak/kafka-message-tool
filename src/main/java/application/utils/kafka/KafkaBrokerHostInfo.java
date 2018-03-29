
package application.utils.kafka;

import application.utils.HostInfo;

public final class KafkaBrokerHostInfo extends HostInfo {
    public KafkaBrokerHostInfo(String hostname, int port) {
        super(hostname, port);
    }
}
