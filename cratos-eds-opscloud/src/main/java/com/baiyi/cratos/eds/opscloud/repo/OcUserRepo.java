package com.baiyi.cratos.eds.opscloud.repo;

import com.baiyi.cratos.common.HttpResult;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.opscloud.model.OcUserVO;
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
 * &#064;Date  2025/3/12 13:58
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OcUserRepo {

    public static List<OcUserVO.User> listUser(EdsConfigs.Opscloud opscloud) {
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
            HttpResult<DataTable<OcUserVO.User>> httpResult = opscloudService.queryUserPage(param);
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

    public static OcUserVO.User addUser(EdsConfigs.Opscloud opscloud, OcUserParam.AddUser addUser) {
        OpscloudService opscloudService = OpscloudServiceFactory.createOpscloudService(opscloud);
        HttpResult<OcUserVO.User> httpResult = opscloudService.addUser(addUser);
        if (!httpResult.isSuccess()) {
            return null;
        }
        return httpResult.getBody();
    }

}
