package com.baiyi.cratos.facade.kubernetes;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.param.kubernetes.KubernetesResourceTemplateParam;
import com.baiyi.cratos.domain.view.kubernetes.resource.KubernetesResourceTemplateVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/4 16:15
 * &#064;Version 1.0
 */
public interface KubernetesResourceTemplateMemberFacade {

    DataTable<KubernetesResourceTemplateVO.Member> queryMemberPage(
            KubernetesResourceTemplateParam.MemberPageQuery pageQuery);

    void addMember(KubernetesResourceTemplateParam.AddMember addMember);

    void updateMember(KubernetesResourceTemplateParam.UpdateMember updateMember);

    void setValidById(int id);

    void deleteById(int id);

}
