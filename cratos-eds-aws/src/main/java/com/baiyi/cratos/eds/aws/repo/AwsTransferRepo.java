package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.transfer.model.*;
import com.baiyi.cratos.eds.aws.service.AwsTransferService;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/13 下午1:34
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AwsTransferRepo {

    public static final String HOME_DIRECTORY_TYPE_LOGICAL = "LOGICAL";

    /**
     * https://docs.aws.amazon.com/transfer/latest/userguide/API_ListServers.html
     *
     * @param regionId
     * @param aws
     * @return
     */
    public static List<ListedServer> listServers(String regionId, EdsConfigs.Aws aws) {
        ListServersRequest request = new ListServersRequest().withMaxResults(1000);
        List<ListedServer> servers = Lists.newArrayList();
        String nextToken = null;
        do {
            if (nextToken != null) {
                request.setNextToken(nextToken);
            }
            ListServersResult result = AwsTransferService.buildAwsTransfer(regionId, aws)
                    .listServers(request);
            if (!CollectionUtils.isEmpty(result.getServers())) {
                servers.addAll(result.getServers());
            }
            nextToken = result.getNextToken();
        } while (StringUtils.hasText(nextToken));
        return servers;
    }


    public static List<ListedUser> listUsers(String regionId, EdsConfigs.Aws aws, String serverId) {
        ListUsersRequest request = new ListUsersRequest().withMaxResults(1000)
                .withServerId(serverId);
        List<ListedUser> users = Lists.newArrayList();
        String nextToken = null;
        do {
            if (nextToken != null) {
                request.setNextToken(nextToken);
            }
            ListUsersResult result = AwsTransferService.buildAwsTransfer(regionId, aws)
                    .listUsers(request);
            if (!CollectionUtils.isEmpty(result.getUsers())) {
                users.addAll(result.getUsers());
            }
            nextToken = result.getNextToken();
        } while (StringUtils.hasText(nextToken));
        return users;
    }

    public static CreateUserResult createUser(String regionId, EdsConfigs.Aws aws,
                                              Collection<HomeDirectoryMapEntry> homeDirectoryMappings, String userName,
                                              String role, String serverId, String sshPublicKey) {
        return createUser(regionId, aws, HOME_DIRECTORY_TYPE_LOGICAL, homeDirectoryMappings, userName, role, serverId,
                sshPublicKey);
    }

    public static CreateUserResult createUser(String regionId, EdsConfigs.Aws aws, String homeDirectoryType,
                                              Collection<HomeDirectoryMapEntry> homeDirectoryMappings, String userName,
                                              String role, String serverId, String sshPublicKey) {
        CreateUserRequest request = new CreateUserRequest();
        request.setHomeDirectoryType(homeDirectoryType);
        request.setHomeDirectoryMappings(homeDirectoryMappings);
        request.setUserName(userName);
        request.setRole(role);
        request.setServerId(serverId);
        request.setSshPublicKeyBody(sshPublicKey);
        return AwsTransferService.buildAwsTransfer(regionId, aws)
                .createUser(request);
    }

    public static Collection<HomeDirectoryMapEntry> generateHomeDirectoryMappings(String target) {
        return generateHomeDirectoryMappings("/", target);
    }

    public static Collection<HomeDirectoryMapEntry> generateHomeDirectoryMappings(String entry, String target) {
        HomeDirectoryMapEntry homeDirectoryMapEntry = new HomeDirectoryMapEntry().withEntry(entry)
                .withTarget(target);
        return Lists.newArrayList(homeDirectoryMapEntry);
    }

}
