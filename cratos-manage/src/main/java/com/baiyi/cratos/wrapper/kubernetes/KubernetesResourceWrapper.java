package com.baiyi.cratos.wrapper.kubernetes;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.KubernetesResource;
import com.baiyi.cratos.domain.view.kubernetes.resource.KubernetesResourceVO;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/7 11:02
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.KUBERNETES_RESOURCE)
public class KubernetesResourceWrapper extends BaseDataTableConverter<KubernetesResourceVO.Resource, KubernetesResource> implements IBaseWrapper<KubernetesResourceVO.Resource> {

    @Override
    //@BusinessWrapper(ofTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC, BusinessTypeEnum.KUBERNETES_RESOURCE_TEMPLATE_MEMBER})
    public void wrap(KubernetesResourceVO.Resource vo) {
    }

}