package com.baiyi.cratos.common;

import com.baiyi.cratos.BaseUnit;
import com.google.api.client.util.Sets;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.certificatemanager.v1.CertificateManagerClient;
import com.google.cloud.certificatemanager.v1.CertificateManagerSettings;
import com.google.cloud.certificatemanager.v1.LocationName;
import com.google.cloud.iam.admin.v1.IAMClient;
import com.google.cloud.iam.admin.v1.IAMSettings;
import com.google.cloud.resourcemanager.v3.ProjectName;
import com.google.cloud.resourcemanager.v3.ProjectsClient;
import com.google.cloud.resourcemanager.v3.ProjectsSettings;
import com.google.iam.v1.GetIamPolicyRequest;
import com.google.iam.v1.Policy;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/17 下午4:14
 * &#064;Version 1.0
 */
public class GCPTest extends BaseUnit {

    private final static String PROJECT_ID = "palmpay-nigeria";
    private final static String PROJECT_NAME = "projects/palmpay-nigeria";

    @Test
    void test1() throws IOException {
        String credentialPath = "/Users/zl/cratos-data/key.json";
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(new FileInputStream(credentialPath));
        IAMSettings settings = IAMSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
        try (IAMClient client = IAMClient.create(settings)) {
//            ListServiceAccountsRequest request = ListServiceAccountsRequest.newBuilder()
//                    .setName(PROJECT_NAME)
//                    .setPageSize(100)
//                    .build();
//            IAMClient.ListServiceAccountsPagedResponse listServiceAccountsPagedResponse = client.listServiceAccounts(
//                    request);
//            System.out.println(listServiceAccountsPagedResponse);
//            GetRoleRequest getRoleRequest = GetRoleRequest.newBuilder().setName("roles/viewer").build();
//            Role role = client.getRole(getRoleRequest);
//            role.getIncludedPermissionsList().forEach(permission -> System.out.println(permission));


//            ListRolesRequest listRolesRequest = ListRolesRequest.newBuilder().setParent(PROJECT_NAME).build();
//            IAMClient.ListRolesPagedResponse listRolesResponse = client.listRoles(listRolesRequest);
//            System.out.println(listRolesResponse);
        }
    }

    @Test
    void certificateTest() throws IOException {
        String credentialPath = "/Users/zl/cratos-data/key.json";
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(new FileInputStream(credentialPath));
        CertificateManagerSettings settings = CertificateManagerSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
        try (CertificateManagerClient client = CertificateManagerClient.create(settings)) {
            LocationName parent = LocationName.of(PROJECT_ID, "global");
            CertificateManagerClient.ListCertificatesPagedResponse listCertificatesPagedResponse = client.listCertificates(
                    parent);
            System.out.println(listCertificatesPagedResponse);
        }
    }


    @Test
    void projectTest() throws IOException {
        String credentialPath = "/Users/zl/cratos-data/key.json";
        GoogleCredentials credentials = ServiceAccountCredentials.fromStream(new FileInputStream(credentialPath));
        ProjectsSettings settings = ProjectsSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
        try (ProjectsClient client = ProjectsClient.create(settings)) {
            GetIamPolicyRequest request = GetIamPolicyRequest.newBuilder()
                    .setResource(ProjectName.of(PROJECT_ID).toString())
                    .build();
            Policy policy = client.getIamPolicy(request);


            Set<String> members = Sets.newHashSet();
            policy.getBindingsList().forEach(binding -> members.addAll(binding.getMembersList()));

            Map<String, List<String>> memberRolesMap = new HashMap<>();

            // Populate the map with members and roles from the policy bindings
//            for (Binding binding : policy.getBindingsList()) {
//                String role = binding.getRole();
//                for (String member : binding.getMembersList()) {
//                    memberRolesMap.computeIfAbsent(member, k -> new ArrayList<>()).add(role);
//                }
//            }
//            System.err.println(memberRolesMap);

            System.err.println(members);
        }
    }

    @Test
    void test() throws IOException {
//        GcpIamRepo.test1();
        // GcpIamRepo.test2();
    }

}
