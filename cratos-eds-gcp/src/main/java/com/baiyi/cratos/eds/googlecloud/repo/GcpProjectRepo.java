package com.baiyi.cratos.eds.googlecloud.repo;

import com.baiyi.cratos.eds.core.config.EdsGoogleCloudConfigModel;
import com.baiyi.cratos.eds.googlecloud.builder.GcpProjectSettingsBuilder;
import com.google.api.client.util.Sets;
import com.google.cloud.resourcemanager.v3.ProjectsClient;
import com.google.cloud.resourcemanager.v3.ProjectsSettings;
import com.google.iam.v1.GetIamPolicyRequest;
import com.google.iam.v1.Policy;
import com.google.iam.v1.Binding;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * @Author 修远
 * @Date 2024/7/29 下午4:56
 * @Since 1.0
 */

@Component
@RequiredArgsConstructor
public class GcpProjectRepo {

    private final GcpProjectSettingsBuilder projectSettingsBuilder;

    public Map<String,List<String>>listMembers(EdsGoogleCloudConfigModel.GoogleCloud googleCloud) throws IOException {
        ProjectsSettings settings = projectSettingsBuilder.buildProjectSettings(googleCloud);
        try (ProjectsClient client = ProjectsClient.create(settings)) {
            GetIamPolicyRequest request = GetIamPolicyRequest.newBuilder()
                    .setResource(googleCloud.getProject().toProjectName())
                    .build();
            Policy policy = client.getIamPolicy(request);
            Set<String> members = Sets.newHashSet();
            policy.getBindingsList().forEach(binding -> members.addAll(binding.getMembersList()));

            Map<String, List<String>> memberRolesMap = new HashMap<>();
            for (Binding binding : policy.getBindingsList()) {
                String role = binding.getRole();
                for (String member : binding.getMembersList()) {
                    memberRolesMap.computeIfAbsent(member, k -> new ArrayList<>()).add(role);
                }
            }
            return memberRolesMap;
        }
    }
}
