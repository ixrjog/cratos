package com.baiyi.cratos.eds.opscloud.repo;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.opscloud.model.OcApplicationVO;
import com.baiyi.cratos.eds.opscloud.param.OcUserParam;
import com.baiyi.cratos.eds.opscloud.service.OpscloudService;
import com.baiyi.cratos.eds.opscloud.service.OpscloudServiceFactory;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/12 14:39
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OcUserPermissionRepo {

    public static List<OcApplicationVO.Application> queryUserApplicationPermission(EdsConfigs.Opscloud opscloud,
                                                                                   int userId) {
        OpscloudService opscloudService = OpscloudServiceFactory.createOpscloudService(opscloud);
        int page = 1;
        int length = 10;
        OcUserParam.UserBusinessPermissionPageQuery param = OcUserParam.UserBusinessPermissionPageQuery.builder()
                .extend(false)
                .authorized(true)
                .businessType(8)
                .page(page)
                .length(length)
                .userId(userId)
                .build();
        List<OcApplicationVO.Application> result = Lists.newArrayList();
        while (true) {
            HttpResult<DataTable<OcApplicationVO.Application>> httpResult = opscloudService.queryUserApplicationPermissionPage(
                    param);
            if (!httpResult.isSuccess()) {
                break;
            }
            if (CollectionUtils.isEmpty(httpResult.getBody()
                                                .getData())) {
                break;
            }
            result.addAll(httpResult.getBody()
                                  .getData());

            if (result.size() >= httpResult.getBody()
                    .getTotalNum()) {
                break;
            }
            param.setPage(++page);
        }
        return result;
    }

}
