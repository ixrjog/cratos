package com.baiyi.cratos.eds.googlecloud.repo;

import com.baiyi.cratos.eds.core.config.EdsGcpConfigModel;
import com.baiyi.cratos.eds.googlecloud.builder.GcpProjectSettingsBuilder;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Maps;
import com.google.api.client.util.Sets;
import com.google.cloud.resourcemanager.v3.ProjectsClient;
import com.google.cloud.resourcemanager.v3.ProjectsSettings;
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

    public Map<String, List<String>> listMembers(EdsGcpConfigModel.Gcp googleCloud) throws IOException {
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
     * @param googleCloud
     * @param username    user:user@example.com
     * @throws IOException
     */
    public void removeMember(EdsGcpConfigModel.Gcp googleCloud, String username) throws IOException {
        ProjectsSettings settings = projectSettingsBuilder.buildProjectSettings(googleCloud);
        String projectId = googleCloud.getProject()
                .getId();
        //String memberToRemove = "user:user@example.com"; // Replace with the member to remove
        String role = "roles/viewer"; // Replace with the role from which to revoke access

        try (ProjectsClient client = ProjectsClient.create(settings)) {
            // 1. Get the current policy
            GetIamPolicyRequest request = GetIamPolicyRequest.newBuilder()
                    .setResource(projectId)
                    .build();
            Policy policy = client.getIamPolicy(request);

            // 2. Modify the policy
            Policy updatedPolicy = removeMemberFromRole(policy, username, role);

            // 3. Update the policy
            client.setIamPolicy(projectId, updatedPolicy);
        } catch (Exception e) {
            log.error("Error removing member: " + e.getMessage());
        }

    }

    // Helper function to remove a member from a role in the policy
    private static Policy removeMemberFromRole(Policy policy, String memberToRemove, String role) {
        List<Binding> updatedBindings = new ArrayList<>();
        boolean roleFound = false;

        for (Binding binding : policy.getBindingsList()) {
            if (binding.getRole()
                    .equals(role)) {
                roleFound = true;
                List<String> updatedMembers = new ArrayList<>(binding.getMembersList());
                updatedMembers.remove(memberToRemove); // Remove the member

                // Create a new Binding object with the updated members
                Binding updatedBinding = Binding.newBuilder()
                        .setRole(role)
                        .addAllMembers(updatedMembers)
                        .build();
                updatedBindings.add(updatedBinding);
            } else {
                // Keep other bindings as they are
                updatedBindings.add(binding);
            }
        }

        // If the role was not found, return the original policy (nothing to change)
        if (!roleFound) {
            log.debug("Role not found in policy.");
            return policy;
        }

        // Build and return the updated Policy
        return Policy.newBuilder()
                .addAllBindings(updatedBindings)
                .setVersion(policy.getVersion())
                .setEtag(policy.getEtag())
                .build();
    }

}
