package com.baiyi.cratos.eds.aws.repo;

import com.amazonaws.services.transfer.model.*;
import com.baiyi.cratos.eds.aws.service.AwsTransferService;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/13 下午1:34
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class AwsTransferRepo {

    /**
     * https://docs.aws.amazon.com/transfer/latest/userguide/API_ListServers.html
     *
     * @param regionId
     * @param aws
     * @return
     */
    public static List<ListedServer> listServers(String regionId, EdsAwsConfigModel.Aws aws) {
        ListServersRequest request = new ListServersRequest().withMaxResults(1000);
        List<ListedServer> servers = Lists.newArrayList();
        while (true) {
            ListServersResult result = AwsTransferService.buildAwsTransfer(regionId, aws)
                    .listServers(request);
            if (CollectionUtils.isEmpty(result.getServers())) {
                return servers;
            } else {
                servers.addAll(result.getServers());
                if (StringUtils.hasText(result.getNextToken())) {
                    request.setNextToken(result.getNextToken());
                } else {
                    return servers;
                }
            }
        }
    }

    public static List<ListedUser> listUsers(String regionId, EdsAwsConfigModel.Aws aws, String serverId) {
        ListUsersRequest request = new ListUsersRequest().withMaxResults(1000)
                .withServerId(serverId);

        List<ListedUser> users = Lists.newArrayList();
        while (true) {
            ListUsersResult result = AwsTransferService.buildAwsTransfer(regionId, aws)
                    .listUsers(request);
            if (CollectionUtils.isEmpty(result.getUsers())) {
                return users;
            } else {
                users.addAll(result.getUsers());
                if (StringUtils.hasText(result.getNextToken())) {
                    request.setNextToken(result.getNextToken());
                } else {
                    return users;
                }
            }
        }
    }

}
