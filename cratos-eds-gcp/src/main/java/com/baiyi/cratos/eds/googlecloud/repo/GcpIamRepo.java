package com.baiyi.cratos.eds.googlecloud.repo;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.googlecloud.builder.GcpIAMSettingsBuilder;
import com.google.api.client.util.Lists;
import com.google.cloud.iam.admin.v1.IAMClient;
import com.google.cloud.iam.admin.v1.IAMSettings;
import com.google.iam.admin.v1.ListRolesRequest;
import com.google.iam.admin.v1.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/29 下午1:42
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class GcpIamRepo {

    private final GcpIAMSettingsBuilder gcpIAMSettingsBuilder;

    public List<Role> listRoles(EdsConfigs.Gcp googleCloud) throws IOException {
        IAMSettings settings = gcpIAMSettingsBuilder.buildIAMSettings(googleCloud);
        try (IAMClient client = IAMClient.create(settings)) {
            List<Role> roles = Lists.newArrayList();
            // 预定义角色
            roles.addAll(Lists.newArrayList(client.listRoles(ListRolesRequest.newBuilder()
                                                                     .setParent("")
                                                                     .build())
                                                    .iterateAll()));
            // 项目级自定义角色
            roles.addAll(Lists.newArrayList(client.listRoles(ListRolesRequest.newBuilder()
                                                                     .setParent(googleCloud.getProject()
                                                                                        .toProjectName())
                                                                     .build())
                                                    .iterateAll()));
            return roles;
        }
    }

}
