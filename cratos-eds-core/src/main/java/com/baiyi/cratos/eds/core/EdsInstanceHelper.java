package com.baiyi.cratos.eds.core;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.domain.param.http.eds.EdsInstanceParam;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.TagService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/27 10:37
 * &#064;Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class EdsInstanceHelper {

    private final EdsInstanceService edsInstanceService;
    private final TagService tagService;

    public List<EdsInstance> queryInstance(EdsInstanceTypeEnum[] instanceTypes, String tag) {
        return Arrays.stream(instanceTypes)
                .flatMap(instanceType -> queryInstance(instanceType, tag).stream())
                .collect(Collectors.toList());
    }

    public List<EdsInstance> queryInstance(EdsInstanceTypeEnum instanceType, String tagKey) {
        Tag tag = tagService.getByTagKey(tagKey);
        if (Objects.isNull(tag)) {
            return List.of();
        }
        BusinessTagParam.QueryByTag queryByTag = BusinessTagParam.QueryByTag.builder()
                .tagId(tag.getId())
                .businessType(BusinessTypeEnum.EDS_INSTANCE.name())
                .build();
        EdsInstanceParam.InstancePageQuery pageQuery = EdsInstanceParam.InstancePageQuery.builder()
                .page(1)
                .length(1024)
                .edsType(instanceType.name())
                .queryByTag(queryByTag)
                .build();
        return queryInstance(pageQuery);
    }

    private List<EdsInstance> queryInstance(EdsInstanceParam.InstancePageQuery pageQuery) {
        DataTable<EdsInstance> table = edsInstanceService.queryEdsInstancePage(pageQuery.toParam());
        return table.getData();
    }

}
