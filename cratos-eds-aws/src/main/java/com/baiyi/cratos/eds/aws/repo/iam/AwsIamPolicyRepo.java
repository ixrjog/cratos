package com.baiyi.cratos.eds.aws.repo.iam;

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.model.*;
import com.baiyi.cratos.eds.aws.service.AmazonIdentityManagementService;
import com.baiyi.cratos.eds.core.config.model.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/17 15:47
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AwsIamPolicyRepo {

    public List<AttachedPolicy> listUserPolicies(EdsAwsConfigModel.Aws aws, String userName) {
        ListAttachedUserPoliciesRequest request = new ListAttachedUserPoliciesRequest().withUserName(userName);
        List<AttachedPolicy> attachedPolicies = Lists.newArrayList();
        AmazonIdentityManagement iamClient = AmazonIdentityManagementService.buildAmazonIdentityManagement(aws);
        ListAttachedUserPoliciesResult result;
        do {
            result = iamClient.listAttachedUserPolicies(request);
            attachedPolicies.addAll(result.getAttachedPolicies());
            request.setMarker(result.getMarker());
        } while (result.getIsTruncated());
        return attachedPolicies;
    }

    public List<Policy> listPolicies(EdsAwsConfigModel.Aws aws) {
        ListPoliciesRequest request = new ListPoliciesRequest();
        List<Policy> policies = Lists.newArrayList();
        AmazonIdentityManagement iamClient = AmazonIdentityManagementService.buildAmazonIdentityManagement(aws);
        ListPoliciesResult result;
        do {
            result = iamClient.listPolicies(request);
            policies.addAll(result.getPolicies());
            request.setMarker(result.getMarker());
        } while (result.getIsTruncated());
        return policies;
    }

    /**
     * https://docs.aws.amazon.com/IAM/latest/APIReference/API_AttachUserPolicy.html
     *
     * @param aws
     * @param userName
     * @param policyArn
     */
    public void attachUserPolicy(EdsAwsConfigModel.Aws aws, String userName, String policyArn) {
        AttachUserPolicyRequest request = new AttachUserPolicyRequest();
        request.setUserName(userName);
        request.setPolicyArn(policyArn);
        AmazonIdentityManagementService.buildAmazonIdentityManagement(aws)
                .attachUserPolicy(request);
    }

    /**
     * https://docs.aws.amazon.com/IAM/latest/APIReference/API_DetachUserPolicy.html
     *
     * @param aws
     * @param userName
     * @param policyArn
     */
    public void detachUserPolicy(EdsAwsConfigModel.Aws aws, String userName, String policyArn) {
        DetachUserPolicyRequest request = new DetachUserPolicyRequest();
        request.setUserName(userName);
        request.setPolicyArn(policyArn);
        AmazonIdentityManagementService.buildAmazonIdentityManagement(aws)
                .detachUserPolicy(request);
    }

}
