package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.BusinessTag;
import com.baiyi.cratos.domain.generator.ServerAccount;
import com.baiyi.cratos.domain.generator.Tag;
import com.baiyi.cratos.enums.RemoteManagementProtocolEnum;
import com.baiyi.cratos.facade.SimpleEdsAccountFacade;
import com.baiyi.cratos.service.BusinessTagService;
import com.baiyi.cratos.service.ServerAccountService;
import com.baiyi.cratos.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/20 下午4:20
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class SimpleEdsAccountFacadeImpl implements SimpleEdsAccountFacade {

    private final TagService tagService;
    private final BusinessTagService businessTagService;
    private final ServerAccountService serverAccountService;
    private final Tag edsTagUniqueKey = Tag.builder()
            .tagKey("EDS")
            .build();

    @Override
    public List<ServerAccount> queryServerAccounts() {
        Tag edsTag = tagService.getByUniqueKey(edsTagUniqueKey);
        if (edsTag == null) {
            return Collections.emptyList();
        }
        List<BusinessTag> businessTags = businessTagService.queryByBusinessTypeAndTagId(
                BusinessTypeEnum.SERVER_ACCOUNT.name(), edsTag.getId());
        List<ServerAccount> serverAccounts = serverAccountService.queryByIds(businessTags.stream()
                .map(BusinessTag::getBusinessId)
                .toList());
        return serverAccounts.stream()
                .filter(e -> RemoteManagementProtocolEnum.SSH.name()
                        .equals(e.getProtocol()))
                .toList();
    }

}
