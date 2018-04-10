
package application.utils;

import application.logging.AppLogger;
import org.apache.commons.validator.routines.InetAddressValidator;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HostnameUtils {

    private static final int RESOLVE_HOSTNAME_TIMEOUT_MS = 2000;
    private static final HostnameUtils INSTANCE = new HostnameUtils();
    private final Set<String> localhostIpAddresses = new HashSet<>();


    public static HostnameUtils getInstance() {
        return INSTANCE;
    }

    public static void fetchLocalhostIpAddresses() {
        HostnameUtils.getInstance().assignLocalhostIpV4Addresses();
    }

    public static boolean isHostnameReachable(String hostname, int timeoutMs) {

        AppLogger.debug(String.format("Checking if hostname '%s' is available (timeout:%d ms)",
                                      hostname,
                                      timeoutMs));
        FutureTask<Boolean> booleanFutureTask = createCheckReachabilityTask(hostname, timeoutMs);
        boolean result = false;
        try {

            result = booleanFutureTask.get(timeoutMs, TimeUnit.MILLISECONDS);

        } catch (InterruptedException e) {
            AppLogger.debug(String.format("Hostname %s is not available (InterruptedException)", hostname));
        } catch (ExecutionException e) {
            AppLogger.debug(String.format("Hostname %s is not reachable (ExecutionException)", hostname));
        } catch (TimeoutException e) {
            AppLogger.debug(String.format("Hostname %s is not reachable (Timeout)", hostname));
        } catch (Throwable e) {
            AppLogger.error("Unknown exception", e);
        }
        booleanFutureTask.cancel(true);

        return result;
    }

    private static FutureTask<Boolean> createCheckReachabilityTask(String hostname, int timeoutMs) {

        FutureTask<Boolean> checkValidityTask = new FutureTask<>(() -> {
            try {
                return InetAddress.getByName(hostname).isReachable(timeoutMs);
            } catch (Throwable e) {
                return false;
            }
        });

        new Thread(checkValidityTask, "CheckAvailabilityTaskThread").start();
        return checkValidityTask;
    }

    public static String resolveHostName(String hostname) throws UnknownHostException {
        return InetAddress.getByName(hostname).getHostAddress();
    }

    public Set<String> getLocalhostIpAddresses() {
        if (localhostIpAddresses.isEmpty()) {
            HostnameUtils.fetchLocalhostIpAddresses();
        }
        return localhostIpAddresses;
    }

    private void assignLocalhostIpV4Addresses() {

        final InetAddressValidator validator = InetAddressValidator.getInstance();
        try {
            Enumeration enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) enumeration.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();

                    final String hostAddress = i.getHostAddress();
                    if (!validator.isValidInet4Address(hostAddress)) {
                        continue;
                    }
                    localhostIpAddresses.add(hostAddress);
                }
            }
        } catch (SocketException exception) {
            AppLogger.error("Could not fetch localhost ip addresses", exception);
        }
    }


    private static Set<String> resolveIpsForHostname(String hostname) throws UnknownHostException {

        hostname = hostname.toLowerCase();
        AppLogger.trace(String.format("Resolving ip(s) for '%s'", hostname));
        if (InetAddressValidator.getInstance().isValidInet4Address(hostname)) {
            AppLogger.trace(String.format("Returning %s", hostname));
            return Collections.singleton(hostname);
        }
        if (hostname.equalsIgnoreCase("localhost")) {
            final Set<String> localhostIpAddresses = HostnameUtils.getInstance().getLocalhostIpAddresses();
            AppLogger.trace(String.format("Returning %s", localhostIpAddresses));
            return localhostIpAddresses;
        }
        final Set<String> hostIps = Collections.singleton(HostnameUtils.resolveHostName(hostname));
        AppLogger.trace(String.format("Returning '%s'", hostIps));
        return hostIps;
    }
}
