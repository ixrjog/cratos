package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.annotation.BusinessDecorator;
import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Domain;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.view.domain.DomainVO;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2024/4/28 上午10:23
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DomainWrapper extends BaseDataTableConverter<DomainVO.Domain, Domain> implements BaseWrapper<DomainVO.Domain> {

    private final EdsInstanceService instanceService;

    @Override
    @BusinessDecorator(types = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC})
    public void wrap(DomainVO.Domain vo) {
        if (IdentityUtils.hasIdentity(vo.getInstanceId())) {
            EdsInstance instance = instanceService.getById(vo.getInstanceId());
            vo.setInstanceName(Optional.ofNullable(instance)
                                       .map(EdsInstance::getInstanceName)
                                       .orElse(""));
        }
    }

}