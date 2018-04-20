package application.kafka.cluster;

import application.logging.Logger;
import application.root.ApplicationBusySwitcher;
import application.utils.HostInfo;
import application.utils.ThrowableUtils;
import application.utils.UserInteractor;
import javafx.application.Platform;

public class ClusterStatusChecker {
    private final ApplicationBusySwitcher busySwitcher;
    private final UserInteractor userInteractor;
    private KafkaClusterProxies kafkaClusterProxies;

    public ClusterStatusChecker(ApplicationBusySwitcher busySwitcher,
                                UserInteractor userInteractor,
                                KafkaClusterProxies kafkaClusterProxies) {
        this.busySwitcher = busySwitcher;
        this.userInteractor = userInteractor;
        this.kafkaClusterProxies = kafkaClusterProxies;
    }

    public void updateStatus(HostInfo hostInfo,
                             boolean shouldShowWarningOnInvalidConfig) {
        new Thread(() -> {
            Platform.runLater(() -> {
                busySwitcher.setAppBusy(true);
            });
            try {
                final KafkaClusterProxy proxy = kafkaClusterProxies.getFreshFor(hostInfo);
                showWarningOnInvalidClusterConfig(proxy, shouldShowWarningOnInvalidConfig);
            } catch (Throwable e) {
                Logger.error(e);
                showGuiErrorMessage("Could not fetch cluster status.", ThrowableUtils.getMessage(e));
            } finally {
                Platform.runLater(() -> {
                    busySwitcher.setAppBusy(false);
                });
            }
        }).start();
    }

    private void showGuiErrorMessage(String header, String content) {
        Platform.runLater(() -> userInteractor.showError(header, content));
    }

    private void showWarningOnInvalidClusterConfig(KafkaClusterProxy proxy,
                                                   boolean shouldShowWarningOnInvalidConfig) {
        if (shouldShowWarningOnInvalidConfig) {
            proxy.reportInvalidClusterConfigurationTo((msg) -> {
                Logger.warn("Invalid cluster config\n" + msg);
            });
        }
    }
}
