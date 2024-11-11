package com.baiyi.cratos.wrapper.kubernetes;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.common.kubernetes.KubernetesResourceTemplateCustom;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.Env;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplate;
import com.baiyi.cratos.domain.view.kubernetes.resource.KubernetesResourceTemplateVO;
import com.baiyi.cratos.service.EnvService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBaseWrapper;
import com.google.api.client.util.Lists;
import com.google.api.client.util.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/1 14:52
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.KUBERNETES_RESOURCE_TEMPLATE)
public class KubernetesResourceTemplateWrapper extends BaseDataTableConverter<KubernetesResourceTemplateVO.Template, KubernetesResourceTemplate> implements IBaseWrapper<KubernetesResourceTemplateVO.Template> {

    private final EnvService envService;

    @Override
    @BusinessWrapper(ofTypes = {BusinessTypeEnum.BUSINESS_TAG, BusinessTypeEnum.BUSINESS_DOC, BusinessTypeEnum.KUBERNETES_RESOURCE_TEMPLATE_MEMBER})
    public void wrap(KubernetesResourceTemplateVO.Template vo) {
        KubernetesResourceTemplateCustom.Custom templateCustom = KubernetesResourceTemplateCustom.loadAs(
                vo.getCustom());
        vo.setNamespaces(getNamespaces(templateCustom));
        vo.setKinds(getKinds(vo));
    }

    /**
     * 从所有数据源实例中获取Namespaces
     *
     * @param templateCustom
     * @return
     */
    private List<String> getNamespaces(KubernetesResourceTemplateCustom.Custom templateCustom) {
        Set<String> namespaces = Sets.newHashSet();
        templateCustom.getInstances()
                .stream()
                .map(KubernetesResourceTemplateCustom.KubernetesInstance::getNamespaces)
                .forEach(namespaces::addAll);
        return Lists.newArrayList(namespaces)
                .stream()
                .map(e -> {
                    Env uniqueKey = Env.builder()
                            .envName(e)
                            .build();
                    Env env = envService.getByUniqueKey(uniqueKey);
                    return KubernetesResourceTemplateCustom.SortableNamespace.builder()
                            .namespace(e)
                            .order(env != null ? env.getSeq() : Integer.MAX_VALUE)
                            .build();
                })
                .sorted(Comparator.comparingInt(KubernetesResourceTemplateCustom.SortableNamespace::getOrder))
                .map(KubernetesResourceTemplateCustom.SortableNamespace::getNamespace)
                .toList();
    }

    private Set<String> getKinds(KubernetesResourceTemplateVO.Template vo) {
        return vo.getMembers()
                .keySet();
    }

}