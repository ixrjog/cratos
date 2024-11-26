package com.baiyi.cratos.eds.harbor.repo;

import com.baiyi.cratos.common.builder.DictBuilder;
import com.baiyi.cratos.eds.core.config.EdsHarborConfigModel;
import com.baiyi.cratos.eds.harbor.model.HarborRepository;
import com.baiyi.cratos.eds.harbor.service.HarborService;
import com.baiyi.cratos.eds.harbor.service.HarborServiceFactory;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/16 上午11:52
 * &#064;Version 1.0
 */
public class HarborRepositoryRepo {


    public static List<HarborRepository.Repository> listRepositories(EdsHarborConfigModel.Harbor harbor,
                                                                     String project) {
        HarborService harborService = HarborServiceFactory.createHarborService(harbor);
        int page = 1;
        int size = 10;
        Map<String, String> param = DictBuilder.newBuilder()
                .put("page", page)
                .put("size", size)
                .build();
        List<HarborRepository.Repository> result = Lists.newArrayList();
        while (true) {
            List<HarborRepository.Repository> repositories = harborService.listRepositories(harbor.getCred()
                    .toBasic(), project, param);
            if (CollectionUtils.isEmpty(repositories)) {
                return result;
            }
            if (repositories.size() < size) {
                result.addAll(repositories);
                return result;
            }
            page++;
            param.put("page", String.valueOf(page));
        }
    }

}
