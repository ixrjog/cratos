package com.baiyi.cratos.eds;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/8 上午10:41
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class EdsInstanceHelper {

    private final EdsInstanceService edsInstanceService;

    private final TagService tagService;

    private final BusinessTagService businessTagService;

    private final EdsInstanceProviderHolderBuilder holderBuilder;

    public List<EdsInstance> queryValidEdsInstance(EdsInstanceTypeEnum edsInstanceTypeEnum, String tagKey) {
        List<EdsInstance> edsInstanceList = edsInstanceService.queryValidEdsInstanceByType(edsInstanceTypeEnum.name());
        if (CollectionUtils.isEmpty(edsInstanceList)) {
            return Collections.emptyList();
        }
        Tag uniqueKey = Tag.builder()
                .tagKey(tagKey)
                .build();
        Tag tag = tagService.getByUniqueKey(uniqueKey);
        if (tag == null) {
            return Collections.emptyList();
        }
        return edsInstanceList.stream()
                .filter(e -> {
                    BusinessTag businessTagUniqueKey = BusinessTag.builder()
                            .businessType(BusinessTypeEnum.EDS_INSTANCE.name())
                            .businessId(e.getId())
                            .tagId(tag.getId())
                            .build();
                    return businessTagService.getByUniqueKey(businessTagUniqueKey) != null;
                })
                .toList();
    }

    public List<? extends EdsInstanceProviderHolder<?, ?>> buildHolder(List<EdsInstance> edsInstanceList, String assetType) {
        return edsInstanceList.stream()
                .map(e -> holderBuilder.newHolder(e.getId(), assetType))
                .toList();
    }

}
