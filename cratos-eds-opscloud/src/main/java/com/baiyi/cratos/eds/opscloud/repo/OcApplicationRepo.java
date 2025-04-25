package com.baiyi.cratos.eds.opscloud.repo;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.eds.core.config.EdsOpscloudConfigModel;
import com.baiyi.cratos.eds.opscloud.param.OcApplicationParam;
import com.baiyi.cratos.eds.opscloud.service.OpscloudService;
import com.baiyi.cratos.eds.opscloud.service.OpscloudServiceFactory;
import com.baiyi.cratos.eds.opscloud.model.OcApplicationVO;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/2 11:42
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OcApplicationRepo {

    public static List<OcApplicationVO.Application> listApplication(EdsOpscloudConfigModel.Opscloud opscloud) {
        OpscloudService opscloudService = OpscloudServiceFactory.createOpscloudService(opscloud);
        int page = 1;
        int length = 10;
        OcApplicationParam.ApplicationPageQuery param = OcApplicationParam.ApplicationPageQuery.builder()
                .extend(true)
                .page(page)
                .length(length)
                .build();
        List<OcApplicationVO.Application> applications = Lists.newArrayList();
        while (true) {
            HttpResult<DataTable<OcApplicationVO.Application>> httpResult = opscloudService.queryApplicationPage(
                    opscloud.getCred()
                            .getAccessToken(), param);
            if (!httpResult.isSuccess()) {
                break;
            }
            if (CollectionUtils.isEmpty(httpResult.getBody()
                    .getData())|| httpResult.getBody().getData().size() < length) {
                break;
            }
            applications.addAll(httpResult.getBody()
                    .getData());
            param.setPage(++page);
        }
        return applications;
    }

}
