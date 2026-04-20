package com.baiyi.cratos.facade.channel.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Organization;
import com.baiyi.cratos.domain.param.http.channel.OrganizationParam;
import com.baiyi.cratos.domain.view.channel.OrganizationVO;
import com.baiyi.cratos.facade.channel.OrganizationFacade;
import com.baiyi.cratos.service.channel.OrganizationService;
import com.baiyi.cratos.service.base.BaseValidService;
import com.baiyi.cratos.wrapper.OrganizationWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/20 14:19
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrganizationFacadeImpl implements OrganizationFacade {

    private final OrganizationService organizationService;
    private final OrganizationWrapper organizationWrapper;

    @Override
    public DataTable<OrganizationVO.Organization> queryOrganizationPage(
            OrganizationParam.OrganizationPageQuery pageQuery) {
        DataTable<Organization> table = organizationService.queryOrganizationPage(pageQuery);
        return organizationWrapper.wrapToTarget(table);
    }

    @Override
    public void addOrganization(OrganizationParam.AddOrganization addOrganization) {
        organizationService.add(addOrganization.toTarget());
    }

    @Override
    public void updateOrganization(OrganizationParam.UpdateOrganization updateOrganization) {
        organizationService.updateByPrimaryKey(updateOrganization.toTarget());
    }

    @Override
    public void deleteById(int id) {
        organizationService.deleteById(id);
    }

    @Override
    public BaseValidService<?, ?> getValidService() {
        return organizationService;
    }

}
