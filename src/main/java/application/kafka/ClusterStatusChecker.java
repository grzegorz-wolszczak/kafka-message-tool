package application.kafka;

import application.logging.Logger;
import application.root.ApplicationBusySwitcher;
import application.globals.KafkaClusterProxies;
import application.utils.HostInfo;
import application.utils.ThrowableUtils;
import application.utils.UserInteractor;
import javafx.application.Platform;

public class ClusterStatusChecker {
    private final ApplicationBusySwitcher busySwitcher;
    private final UserInteractor userInteractor;

    public ClusterStatusChecker(ApplicationBusySwitcher busySwitcher,
                                UserInteractor userInteractor) {
        this.busySwitcher = busySwitcher;
        this.userInteractor = userInteractor;
    }

    public void updateStatus(HostInfo hostInfo,
                             boolean shouldShowWarningOnInvalidConfig) {
        new Thread(() -> {
            Platform.runLater(() -> {
                busySwitcher.setAppBusy(true);
            });
            try {
                final KafkaClusterProxy proxy = KafkaClusterProxies.getFreshFor(hostInfo);
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
                userInteractor.showWarning("Invalid cluster configuration", msg);
                Logger.warn("Invalid cluster config\n" + msg);
            });
        }
    }
}
