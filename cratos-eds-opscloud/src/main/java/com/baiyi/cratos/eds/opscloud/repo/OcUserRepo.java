package com.baiyi.cratos.eds.opscloud.repo;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.eds.core.config.EdsOpscloudConfigModel;
import com.baiyi.cratos.eds.opscloud.model.OcUserVO;
import com.baiyi.cratos.eds.opscloud.param.OcUserParam;
import com.baiyi.cratos.eds.opscloud.service.OpscloudService;
import com.baiyi.cratos.eds.opscloud.service.OpscloudServiceFactory;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/12 13:58
 * &#064;Version 1.0
 */
public class OcUserRepo {

    public static List<OcUserVO.User> listUser(EdsOpscloudConfigModel.Opscloud opscloud) {
        OpscloudService opscloudService = OpscloudServiceFactory.createOpscloudService(opscloud);
        int page = 1;
        int length = 10;
        OcUserParam.UserPageQuery param = OcUserParam.UserPageQuery.builder()
                .extend(false)
                .page(page)
                .length(length)
                .build();
        List<OcUserVO.User> result = Lists.newArrayList();
        while (true) {
            HttpResult<DataTable<OcUserVO.User>> httpResult = opscloudService.queryUserPage(opscloud.getCred()
                    .getAccessToken(), param);
            if (!httpResult.isSuccess()) {
                break;
            }
            if (CollectionUtils.isEmpty(httpResult.getBody()
                    .getData()) || httpResult.getBody()
                    .getData()
                    .size() < length) {
                break;
            }
            result.addAll(httpResult.getBody()
                    .getData());
            param.setPage(++page);
        }
        return result;
    }


}
