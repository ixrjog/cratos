package com.baiyi.cratos.eds.aws.repo.iam;

import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.model.AccessKeyMetadata;
import com.amazonaws.services.identitymanagement.model.ListAccessKeysRequest;
import com.amazonaws.services.identitymanagement.model.ListAccessKeysResult;
import com.baiyi.cratos.eds.aws.service.AmazonIdentityManagementService;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/7 11:19
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AwsIamAccessKeyRepo {

    public static List<AccessKeyMetadata> listAccessKeys(EdsAwsConfigModel.Aws aws, String userName) {
        ListAccessKeysRequest request = new ListAccessKeysRequest().withUserName(userName);
        List<AccessKeyMetadata> keyMetadata = Lists.newArrayList();
        AmazonIdentityManagement iamClient = AmazonIdentityManagementService.buildAmazonIdentityManagement(aws);
        ListAccessKeysResult result;
        do {
            result = iamClient.listAccessKeys(request);
            keyMetadata.addAll(result.getAccessKeyMetadata());
            request.setMarker(result.getMarker());
        } while (result.getIsTruncated());
        return keyMetadata;
    }

}
