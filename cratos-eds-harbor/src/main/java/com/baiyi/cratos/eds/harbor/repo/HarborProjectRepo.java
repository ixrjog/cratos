package com.baiyi.cratos.eds.harbor.repo;

import com.baiyi.cratos.common.builder.DictBuilder;
import com.baiyi.cratos.eds.core.config.EdsHarborConfigModel;
import com.baiyi.cratos.eds.harbor.model.HarborProject;
import com.baiyi.cratos.eds.harbor.service.HarborService;
import com.baiyi.cratos.eds.harbor.service.HarborServiceFactory;
import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/16 上午11:07
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HarborProjectRepo {

    public static List<HarborProject.Project> listProjects(EdsHarborConfigModel.Harbor harbor) {
        HarborService harborService = HarborServiceFactory.createHarborService(harbor);
        int page = 1;
        int size = 10;
        Map<String, String> param = DictBuilder.newBuilder()
                .put("page", page)
                .put("size", size)
                .build();
        List<HarborProject.Project> result = Lists.newArrayList();
        while (true) {
            List<HarborProject.Project> projects = harborService.listProjects(param);
            if (CollectionUtils.isEmpty(projects)) {
                return result;
            }
            if (projects.size() < size) {
                result.addAll(projects);
                return result;
            }
            page++;
            param.put("page", String.valueOf(page));
        }
    }

}
