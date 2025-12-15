package com.baiyi.cratos.eds.azure.repo;

import com.baiyi.cratos.eds.azure.graph.client.GraphClientBuilder;
import com.baiyi.cratos.eds.azure.graph.model.GraphDirectoryModel;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.microsoft.graph.models.DirectoryRole;
import com.microsoft.graph.models.DirectoryRoleCollectionResponse;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import com.microsoft.kiota.store.InMemoryBackingStore;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/14 10:36
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphDirectoryRepo {

    public static List<GraphDirectoryModel.Role> listRoles(EdsConfigs.Azure azure) {
        final GraphServiceClient graphClient = GraphClientBuilder.create(azure);
        DirectoryRoleCollectionResponse result = graphClient.directoryRoles()
                .get();
        List<DirectoryRole> directoryRoles = Optional.ofNullable(result)
                .map(DirectoryRoleCollectionResponse::getValue)
                .orElseGet(List::of);
        if (directoryRoles.isEmpty()) {
            return List.of();
        }
        return directoryRoles.stream()
                .map(role -> {
                    Object backing = role.getBackingStore();
                    if (!(backing instanceof InMemoryBackingStore)) return null;
                    return GraphDirectoryModel.Role.of(((InMemoryBackingStore) backing).enumerate());
                })
                .filter(Objects::nonNull)
                .toList();
    }

}
