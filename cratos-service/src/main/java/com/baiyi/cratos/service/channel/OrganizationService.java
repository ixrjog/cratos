package com.baiyi.cratos.service.channel;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.generator.Organization;
import com.baiyi.cratos.domain.param.http.channel.OrganizationParam;
import com.baiyi.cratos.mapper.OrganizationMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;
import com.baiyi.cratos.service.base.BaseValidService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/20 14:08
 * &#064;Version 1.0
 */
public interface OrganizationService extends BaseUniqueKeyService<Organization, OrganizationMapper>, BaseValidService<Organization, OrganizationMapper> {

    DataTable<Organization> queryOrganizationPage(OrganizationParam.OrganizationPageQuery pageQuery);

}
