package com.baiyi.cratos.facade.channel;

import com.baiyi.cratos.HasSetValid;
import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.http.channel.OrganizationParam;
import com.baiyi.cratos.domain.view.channel.OrganizationVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/4/20 14:19
 * &#064;Version 1.0
 */
public interface OrganizationFacade extends HasSetValid {

    DataTable<OrganizationVO.Organization> queryOrganizationPage(OrganizationParam.OrganizationPageQuery pageQuery);

    void addOrganization(OrganizationParam.AddOrganization addOrganization);

    void updateOrganization(OrganizationParam.UpdateOrganization updateOrganization);

    void deleteById(int id);

}
