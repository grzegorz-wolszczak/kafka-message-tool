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
    private KafkaClusterProxiesBase kafkaClusterProxiesBase;

    public ClusterStatusChecker(ApplicationBusySwitcher busySwitcher,
                                UserInteractor userInteractor,
                                KafkaClusterProxiesBase kafkaClusterProxiesBase) {
        this.busySwitcher = busySwitcher;
        this.userInteractor = userInteractor;
        this.kafkaClusterProxiesBase = kafkaClusterProxiesBase;
    }

    public void updateStatus(HostInfo hostInfo,
                             boolean shouldShowWarningOnInvalidConfig) {
        new Thread(() -> {
            Platform.runLater(() -> {
                busySwitcher.setAppBusy(true);
            });
            try {
                final KafkaClusterProxy proxy = kafkaClusterProxiesBase.getRefreshed(hostInfo);
                showWarningOnInvalidClusterConfig(proxy, shouldShowWarningOnInvalidConfig);
            } catch (Throwable e) {
                Logger.error(e);
                showGuiErrorMessage("Could not fetch cluster status.", ThrowableUtils.getMessage(e));
            } finally {
                Platform.runLater(() -> {
                    busySwitcher.setAppBusy(false);
                });
            }
        }, "KMT-Thread-ClusterStatusChecker").start();
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
