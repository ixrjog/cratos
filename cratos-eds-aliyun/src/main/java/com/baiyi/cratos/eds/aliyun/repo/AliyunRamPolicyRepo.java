package com.baiyi.cratos.eds.aliyun.repo;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.*;
import com.baiyi.cratos.eds.aliyun.client.common.AliyunClient;
import com.baiyi.cratos.eds.core.config.model.EdsAliyunConfigModel;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/9 下午4:42
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AliyunRamPolicyRepo {

    private final AliyunClient aliyunClient;
    public static final int PAGE_SIZE = 50;

    public List<ListPoliciesResponse.Policy> listPolicies(EdsAliyunConfigModel.Aliyun aliyun) throws ClientException {
        List<ListPoliciesResponse.Policy> policies = Lists.newArrayList();
        String marker;
        ListPoliciesRequest request = new ListPoliciesRequest();
        request.setMaxItems(PAGE_SIZE);
        do {
            ListPoliciesResponse response = aliyunClient.getAcsResponse(aliyun.getRegionId(), aliyun, request);
            policies.addAll(response.getPolicies());
            marker = response.getMarker();
            request.setMarker(marker);
        } while (StringUtils.isNotBlank(marker));
        return policies;
    }

    public GetPolicyResponse.Policy getPolicy(EdsAliyunConfigModel.Aliyun aliyun,
                                              ListPoliciesResponse.Policy policy) throws ClientException {
        GetPolicyRequest request = new GetPolicyRequest();
        request.setPolicyName(policy.getPolicyName());
        request.setPolicyType(policy.getPolicyType());
        GetPolicyResponse response = aliyunClient.getAcsResponse(aliyun.getRegionId(), aliyun, request);
        return response.getPolicy();
    }

    public List<ListPoliciesForUserResponse.Policy> listPoliciesForUser(EdsAliyunConfigModel.Aliyun aliyun,
                                                                        String username) throws ClientException {
        ListPoliciesForUserRequest request = new ListPoliciesForUserRequest();
        request.setUserName(username);
        ListPoliciesForUserResponse response = aliyunClient.getAcsResponse(aliyun.getRegionId(), aliyun, request);
        return response == null ? Collections.emptyList() : response.getPolicies();
    }

    public void attachPolicyToUser(String regionId, EdsAliyunConfigModel.Aliyun aliyun, String ramUsername, String policyName ,String policyType) throws ClientException {
        AttachPolicyToUserRequest request = new AttachPolicyToUserRequest();
        request.setUserName(ramUsername);
        request.setPolicyName(policyName);
        request.setPolicyType(policyType);
        AttachPolicyToUserResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
    }

    public void detachPolicyFromUser(String regionId, EdsAliyunConfigModel.Aliyun aliyun, String ramUsername,String policyName ,String policyType) throws ClientException {
        DetachPolicyFromUserRequest request = new DetachPolicyFromUserRequest();
        request.setUserName(ramUsername);
        request.setPolicyName(policyName);
        request.setPolicyType(policyType);
        DetachPolicyFromUserResponse response = aliyunClient.getAcsResponse(regionId, aliyun, request);
    }

}
