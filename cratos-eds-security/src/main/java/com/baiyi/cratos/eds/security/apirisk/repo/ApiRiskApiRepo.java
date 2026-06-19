package com.baiyi.cratos.eds.security.apirisk.repo;

import com.baiyi.cratos.common.builder.DictBuilder;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.security.apirisk.result.ApiRiskApiResult;
import com.baiyi.cratos.eds.security.apirisk.result.base.ApiRiskResponse;
import com.baiyi.cratos.eds.security.apirisk.result.base.ApiRiskResponseData;
import com.baiyi.cratos.eds.security.apirisk.service.ApiRiskService;
import com.baiyi.cratos.eds.security.apirisk.service.ApiRiskServiceFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/6/12 14:43
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiRiskApiRepo {

    private static final int PAGE_SIZE = 100;
    /**
     * 翻页上限,避免接口异常导致死循环。
     */
    private static final int MAX_PAGE = 1000;

    public static ApiRiskResponse<ApiRiskApiResult.Api> listApi(EdsConfigs.ApiRisk apiRisk, int page,
                                                                int size) {
        ApiRiskService apiRiskService = ApiRiskServiceFactory.createApiRiskService(apiRisk);
        Map<String, String> param = DictBuilder.newBuilder()
                .put("page", page)
                .put("size", size)
                .build();
        return apiRiskService.listApi(param);
    }

    /**
     * 分页查询所有 HTTP API。
     *
     * @param apiRisk 配置
     * @return 全部 Api 列表
     */
    public static List<ApiRiskApiResult.Api> listAllApi(EdsConfigs.ApiRisk apiRisk) {
        ApiRiskService apiRiskService = ApiRiskServiceFactory.createApiRiskService(apiRisk);
        List<ApiRiskApiResult.Api> all = new ArrayList<>();
        for (int page = 1; page <= MAX_PAGE; page++) {
            Map<String, String> param = DictBuilder.newBuilder()
                    .put("page", page)
                    .put("size", PAGE_SIZE)
                    .build();
            ApiRiskResponse<ApiRiskApiResult.Api> response = apiRiskService.listApi(param);
            ApiRiskResponseData<ApiRiskApiResult.Api> data = response == null ? null : response.getData();
            if (data == null || CollectionUtils.isEmpty(data.getRows())) {
                break;
            }
            all.addAll(data.getRows());
            // 已取满总数,或本页不足一页,说明到末页
            Long totalCount = data.getTotalCount();
            if ((totalCount != null && all.size() >= totalCount) || data.getRows()
                    .size() < PAGE_SIZE) {
                break;
            }
        }
        return all;
    }

}
