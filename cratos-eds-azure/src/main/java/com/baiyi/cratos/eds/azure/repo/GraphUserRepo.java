package com.baiyi.cratos.eds.azure.repo;

import com.baiyi.cratos.eds.azure.graph.client.GraphClientBuilder;
import com.baiyi.cratos.eds.azure.graph.model.GraphUserModel;
import com.baiyi.cratos.eds.core.config.model.EdsAzureConfigModel;
import com.microsoft.graph.directoryobjects.item.getmemberobjects.GetMemberObjectsPostResponse;
import com.microsoft.graph.models.User;
import com.microsoft.graph.models.UserCollectionResponse;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import com.microsoft.kiota.store.InMemoryBackingStore;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/12 16:24
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GraphUserRepo {

    /**
     * https://learn.microsoft.com/zh-cn/graph/api/user-list?view=graph-rest-1.0&tabs=java
     *
     * @param azure
     * @return
     */
    public static List<GraphUserModel.User> listUsers(EdsAzureConfigModel.Azure azure) {
        final GraphServiceClient graphClient = GraphClientBuilder.create(azure);
        UserCollectionResponse result = graphClient.users()
                .get();
        List<User> users = Optional.ofNullable(result)
                .map(UserCollectionResponse::getValue)
                .orElseGet(List::of);
        if (users.isEmpty()) {
            return List.of();
        }
        return users.stream()
                .map(user -> {
                    Object backing = user.getBackingStore();
                    if (!(backing instanceof InMemoryBackingStore)) return null;
                    return GraphUserModel.User.of(((InMemoryBackingStore) backing).enumerate());
                })
                .filter(Objects::nonNull)
                .toList();
    }

    public static GraphUserModel.User getUserById(EdsAzureConfigModel.Azure azure, String userId) {
        final GraphServiceClient graphClient = GraphClientBuilder.create(azure);
        User result = graphClient.users()
                .byUserId(userId)
                .get(requestConfiguration -> requestConfiguration.queryParameters.select = new String[]{"displayName", "givenName", "postalCode", "identities", "accountEnabled"});
        return GraphUserModel.User.builder()
                .id(userId)
                .accountEnabled(Optional.ofNullable(result)
                                        .map(User::getAccountEnabled)
                                        .orElse(false))
                .build();
    }

    public static void blockUserById(EdsAzureConfigModel.Azure azure, String userId) {
        final GraphServiceClient graphClient = GraphClientBuilder.create(azure);
        User user = new User();
        user.setAccountEnabled(false);
        User result = graphClient.users()
                .byUserId(userId)
                .patch(user);
    }

    public static List<String> getUserDirectoryRoleIds(EdsAzureConfigModel.Azure azure, String userId) {
        final GraphServiceClient graphClient = GraphClientBuilder.create(azure);
        com.microsoft.graph.directoryobjects.item.getmemberobjects.GetMemberObjectsPostRequestBody getMemberObjectsPostRequestBody = new com.microsoft.graph.directoryobjects.item.getmemberobjects.GetMemberObjectsPostRequestBody();
        getMemberObjectsPostRequestBody.setSecurityEnabledOnly(true);
        var result = graphClient.directoryObjects()
                .byDirectoryObjectId(userId)
                .getMemberObjects()
                .post(getMemberObjectsPostRequestBody);
        return Optional.ofNullable(result)
                .map(GetMemberObjectsPostResponse::getValue)
                .orElse(List.of());
    }

}
