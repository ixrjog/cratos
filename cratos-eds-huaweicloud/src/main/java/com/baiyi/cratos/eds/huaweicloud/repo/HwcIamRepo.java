package com.baiyi.cratos.eds.huaweicloud.repo;

import com.baiyi.cratos.eds.core.config.EdsHuaweicloudConfigModel;
import com.baiyi.cratos.eds.huaweicloud.client.HwcIamClientBuilder;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.iam.v3.IamClient;
import com.huaweicloud.sdk.iam.v3.model.KeystoneListUsersRequest;
import com.huaweicloud.sdk.iam.v3.model.KeystoneListUsersResponse;
import com.huaweicloud.sdk.iam.v3.model.KeystoneListUsersResult;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/17 下午1:48
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class HwcIamRepo {

    public static List<KeystoneListUsersResult> listUsers(
            EdsHuaweicloudConfigModel.Huaweicloud huaweicloud) throws ServiceResponseException {
        return listUsers(huaweicloud.getRegionId(), huaweicloud);
    }

    public static List<KeystoneListUsersResult> listUsers(String regionId,
                                                          EdsHuaweicloudConfigModel.Huaweicloud huaweicloud) throws ServiceResponseException {
        IamClient client = HwcIamClientBuilder.buildIamClient(regionId, huaweicloud);
        KeystoneListUsersRequest request = new KeystoneListUsersRequest();
        KeystoneListUsersResponse response = client.keystoneListUsers(request);
        return response.getUsers();
    }

}
