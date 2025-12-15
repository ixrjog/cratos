package com.baiyi.cratos.eds.azure.graph.client;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.config.model.EdsAzureConfigModel;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/12 14:52
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphClientBuilder {

    /**
     *    // https://learn.microsoft.com/zh-cn/graph/sdks/sdk-installation?view=graph-rest-1.0
     * @param azure
     * @return
     */
    public static GraphServiceClient create(EdsConfigs.Azure azure) {
        EdsAzureConfigModel.Cred cred = Optional.of(azure)
                .map(EdsConfigs.Azure::getCred)
                .orElseThrow();
        final ClientSecretCredential credential = new ClientSecretCredentialBuilder().clientId(cred.getClientId())
                .tenantId(cred.getTenantId())
                .clientSecret(cred.getSecretValue())
                .build();
        return new GraphServiceClient(credential, cred.getScope());
    }

}
