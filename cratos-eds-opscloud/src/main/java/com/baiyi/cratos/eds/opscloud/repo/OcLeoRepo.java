package com.baiyi.cratos.eds.opscloud.repo;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.opscloud.model.OcLeoVO;
import com.baiyi.cratos.eds.opscloud.param.OcLeoParam;
import com.baiyi.cratos.eds.opscloud.service.OpscloudService;
import com.baiyi.cratos.eds.opscloud.service.OpscloudServiceFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/25 13:38
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OcLeoRepo {

    public static HttpResult<OcLeoVO.BuildImage> queryBuildImageVersion(EdsConfigs.Opscloud opscloud,
                                                                        OcLeoParam.QueryBuildImageVersion queryBuildImageVersion) {
        OpscloudService opscloudService = OpscloudServiceFactory.createOpscloudService(opscloud);
        return opscloudService.queryBuildImageVersion(queryBuildImageVersion);
    }

}
