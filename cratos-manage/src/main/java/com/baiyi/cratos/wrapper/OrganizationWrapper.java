package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Organization;
import com.baiyi.cratos.domain.view.channel.OrganizationVO;
import com.baiyi.cratos.service.channel.OrganizationService;
import com.baiyi.cratos.wrapper.base.BaseBusinessDecorator;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/20 14:29
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.ORGANIZATION)
public class OrganizationWrapper extends BaseDataTableConverter<OrganizationVO.Organization, Organization> implements BaseBusinessDecorator<OrganizationVO.HasOrganization, OrganizationVO.Organization> {

    private final OrganizationService organizationService;

    @Override
    public void wrap(OrganizationVO.Organization vo) {
        // This is a good idea
    }

    @Override
    public void decorateBusiness(OrganizationVO.HasOrganization hasBusiness) {
        if (IdentityUtils.hasIdentity(hasBusiness.getOrganizationId())) {
            Organization organization = organizationService.getById(hasBusiness.getOrganizationId());
            if (organization != null) {
                OrganizationVO.Organization bVO = this.convert(organization);
                delegateWrap(bVO);
                hasBusiness.setOrganization(bVO);
            }
        }
    }

}