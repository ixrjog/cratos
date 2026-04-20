package com.baiyi.cratos.wrapper;

import com.baiyi.cratos.domain.generator.Organization;
import com.baiyi.cratos.domain.view.channel.OrganizationVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.BaseWrapper;
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
public class OrganizationWrapper extends BaseDataTableConverter<OrganizationVO.Organization, Organization> implements BaseWrapper<OrganizationVO.Organization> {

    @Override
    public void wrap(OrganizationVO.Organization vo) {
        // This is a good idea
    }

}