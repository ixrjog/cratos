package com.baiyi.cratos.eds.googlecloud.repo;

import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.googlecloud.builder.GcpProjectSettingsBuilder;
import com.baiyi.cratos.eds.googlecloud.enums.GcpIAMMemberType;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import com.google.api.client.util.Sets;
import com.google.cloud.resourcemanager.v3.ProjectsClient;
import com.google.cloud.resourcemanager.v3.ProjectsSettings;
import com.google.common.base.Joiner;
import com.google.iam.v1.Binding;
import com.google.iam.v1.GetIamPolicyRequest;
import com.google.iam.v1.Policy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author 修远
 * @Date 2024/7/29 下午4:56
 * @Since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GcpProjectRepo {

    private final GcpProjectSettingsBuilder projectSettingsBuilder;

    public Map<String, List<String>> listMembers(EdsConfigs.Gcp googleCloud) throws IOException {
        ProjectsSettings settings = projectSettingsBuilder.buildProjectSettings(googleCloud);
        try (ProjectsClient client = ProjectsClient.create(settings)) {
            GetIamPolicyRequest request = GetIamPolicyRequest.newBuilder()
                    .setResource(googleCloud.getProject()
                                         .toProjectName())
                    .build();
            Policy policy = client.getIamPolicy(request);
            Set<String> members = Sets.newHashSet();
            policy.getBindingsList()
                    .forEach(binding -> members.addAll(binding.getMembersList()));
            Map<String, List<String>> memberRolesMap = Maps.newHashMap();
            policy.getBindingsList()
                    .forEach(binding -> {
                        String role = binding.getRole();
                        binding.getMembersList()
                                .forEach(member -> memberRolesMap.computeIfAbsent(member, k -> Lists.newArrayList())
                                        .add(role));
                    });
            return memberRolesMap;
        }
    }

    /**
     * 查询成员在项目中的所有角色
     */
    public List<String> listMemberRoles(EdsConfigs.Gcp googleCloud, GcpIAMMemberType type, String member) throws IOException {
        ProjectsSettings settings = projectSettingsBuilder.buildProjectSettings(googleCloud);
        String typeMember = Joiner.on(":").join(type.getKey(), member);
        try (ProjectsClient client = ProjectsClient.create(settings)) {
            Policy policy = client.getIamPolicy(GetIamPolicyRequest.newBuilder()
                    .setResource(googleCloud.getProject().toProjectName()).build());
            return policy.getBindingsList().stream()
                    .filter(binding -> binding.getMembersList().contains(typeMember))
                    .map(Binding::getRole)
                    .toList();
        }
    }

    /**
     * GoogleID加入项目，并授权角色
     *
     * @param googleCloud
     * @param member
     * @param role
     * @throws IOException
     */
    public void addMember(EdsConfigs.Gcp googleCloud, GcpIAMMemberType type, String member,
                          String role) throws IOException {
        ProjectsSettings settings = projectSettingsBuilder.buildProjectSettings(googleCloud);
        String projectId = googleCloud.getProject()
                .toProjectName();
        String typeMember = Joiner.on(":")
                .join(type.getKey(), member);
        try (ProjectsClient client = ProjectsClient.create(settings)) {
            Policy policy = client.getIamPolicy(GetIamPolicyRequest.newBuilder()
                                                        .setResource(projectId)
                                                        .build());
            Binding newBinding = Binding.newBuilder()
                    .setRole(role)
                    .addMembers(typeMember)
                    .build();
            Policy updatedPolicy = Policy.newBuilder(policy)
                    .addBindings(newBinding)
                    .build();
            client.setIamPolicy(projectId, updatedPolicy);
        }
    }

    public void grantRole(EdsConfigs.Gcp googleCloud, GcpIAMMemberType type, String member,
                          String role) throws IOException {
        ProjectsSettings settings = projectSettingsBuilder.buildProjectSettings(googleCloud);
        String projectId = googleCloud.getProject()
                .toProjectName();
        String typeMember = Joiner.on(":")
                .join(type.getKey(), member);
        try (ProjectsClient client = ProjectsClient.create(settings)) {
            Policy policy = client.getIamPolicy(GetIamPolicyRequest.newBuilder()
                                                        .setResource(projectId)
                                                        .build());
            // 查找已有的 role binding，存在则追加 member，不存在则新建
            List<Binding> bindings = new ArrayList<>(policy.getBindingsList());
            boolean found = false;
            for (int i = 0; i < bindings.size(); i++) {
                if (bindings.get(i)
                        .getRole()
                        .equals(role)) {
                    if (bindings.get(i)
                            .getMembersList()
                            .contains(typeMember)) {
                        return; // 已有该授权
                    }
                    bindings.set(
                            i, bindings.get(i)
                                    .toBuilder()
                                    .addMembers(typeMember)
                                    .build()
                    );
                    found = true;
                    break;
                }
            }
            if (!found) {
                bindings.add(Binding.newBuilder()
                                     .setRole(role)
                                     .addMembers(typeMember)
                                     .build());
            }
            Policy updatedPolicy = Policy.newBuilder()
                    .addAllBindings(bindings)
                    .setVersion(policy.getVersion())
                    .setEtag(policy.getEtag())
                    .build();
            client.setIamPolicy(projectId, updatedPolicy);
        }
    }

    /**
     * 从项目中移除成员（从所有角色中移除）
     *
     * @param googleCloud
     * @param type
     * @param member 邮箱地址
     * @throws IOException
     */
    public void removeMember(EdsConfigs.Gcp googleCloud, GcpIAMMemberType type, String member) throws IOException {
        ProjectsSettings settings = projectSettingsBuilder.buildProjectSettings(googleCloud);
        String projectId = googleCloud.getProject().toProjectName();
        String typeMember = Joiner.on(":").join(type.getKey(), member);
        try (ProjectsClient client = ProjectsClient.create(settings)) {
            Policy policy = client.getIamPolicy(GetIamPolicyRequest.newBuilder()
                    .setResource(projectId).build());
            List<Binding> updatedBindings = policy.getBindingsList().stream()
                    .map(binding -> {
                        if (!binding.getMembersList().contains(typeMember)) {
                            return binding;
                        }
                        List<String> members = new ArrayList<>(binding.getMembersList());
                        members.remove(typeMember);
                        return Binding.newBuilder()
                                .setRole(binding.getRole())
                                .addAllMembers(members)
                                .build();
                    })
                    .filter(binding -> !binding.getMembersList().isEmpty())
                    .toList();
            Policy updatedPolicy = Policy.newBuilder()
                    .addAllBindings(updatedBindings)
                    .setVersion(policy.getVersion())
                    .setEtag(policy.getEtag())
                    .build();
            client.setIamPolicy(projectId, updatedPolicy);
        }
    }

}
