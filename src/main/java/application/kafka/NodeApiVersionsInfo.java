package application.kafka;

import org.apache.kafka.common.protocol.ApiKeys;
import org.apache.kafka.common.requests.ApiVersionsResponse;

import java.util.ArrayList;
import java.util.List;

public class NodeApiVersionsInfo {
    private final List<ApiVersionsResponse.ApiVersion> apiVersions = new ArrayList<>();

    public NodeApiVersionsInfo(List<ApiVersionsResponse.ApiVersion> apiVersions) {
        this.apiVersions.addAll(apiVersions);
    }

    private boolean isApiSupportedById(short apiKeyId) {
        for (ApiVersionsResponse.ApiVersion version : apiVersions) {
            if (version.apiKey == apiKeyId) {
                return true;
            }
        }
        return false;
    }

    public boolean doesApiSupportDescribeConfig() {
        return isApiSupportedById(ApiKeys.DESCRIBE_CONFIGS.id);
    }
}
