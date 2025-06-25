package com.baiyi.cratos.workorder.facade.impl;

import com.baiyi.cratos.common.enums.KubernetesResourceKindEnum;
import com.baiyi.cratos.common.kubernetes.KubernetesResourceTemplateCustom;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplate;
import com.baiyi.cratos.domain.model.ApplicationModel;
import com.baiyi.cratos.domain.param.http.kubernetes.KubernetesResourceTemplateParam;
import com.baiyi.cratos.facade.EnvFacade;
import com.baiyi.cratos.facade.KubernetesResourceTemplateFacade;
import com.baiyi.cratos.service.kubernetes.KubernetesResourceTemplateService;
import com.baiyi.cratos.workorder.facade.WorkOrderKubernetesResourceFacade;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/24 18:09
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class WorkOrderKubernetesResourceFacadeImpl implements WorkOrderKubernetesResourceFacade {

    private final KubernetesResourceTemplateFacade templateFacade;
    private final KubernetesResourceTemplateService templateService;
    private final EnvFacade envFacade;

    private static final String FE_H5_PROJECT = "FE-H5-PROJECT";
    private static final String FE_H5_ROOT = "FE-H5-ROOT";
    private static final Pattern DOMAIN_PATTERN = Pattern.compile("^([^.]+)(\\..+)$");

    private String insertEnvPlaceholder(String domain) {
        if (domain == null || domain.isEmpty()) {
            return domain;
        }
        Matcher matcher = DOMAIN_PATTERN.matcher(domain);
        if (matcher.matches()) {
            String subdomain = matcher.group(1);
            String restDomain = matcher.group(2);
            return subdomain + "-${envName}" + restDomain;
        }
        // 没有点的情况
        return domain + "-${envName}";
    }

    @Override
    public void createKubernetesResource(ApplicationModel.CreateFrontEndApplication createFrontEndApplication) {
        String mappingsPath = Optional.ofNullable(createFrontEndApplication)
                .map(ApplicationModel.CreateFrontEndApplication::getMappingsPath)
                .orElse("/");
        boolean isHomePage = isHomePage(mappingsPath);
        String templateKey = getTemplateKey(isHomePage);
        KubernetesResourceTemplate kubernetesResourceTemplate = templateService.getByTemplateKey(templateKey);
        KubernetesResourceTemplateCustom.Custom templateCustom = KubernetesResourceTemplateCustom.loadAs(
                kubernetesResourceTemplate);
        Map<String, String> data = Maps.newHashMap();
        assert createFrontEndApplication != null;
        data.put("appName", createFrontEndApplication.getApplicationName());
        data.put("host", insertEnvPlaceholder(createFrontEndApplication.getDomain()));
        if (!isHomePage) {
            // path 不含首尾斜杠
            data.put("path", createFrontEndApplication.getMappingsPath()
                    .replaceAll("^/|/$", ""));
        }
        templateCustom.setData(data);
        Set<Integer> instanceIds = templateCustom.getInstances()
                .stream()
                .map(KubernetesResourceTemplateCustom.KubernetesInstance::getId)
                .collect(java.util.stream.Collectors.toSet());

        KubernetesResourceTemplateParam.CreateResourceByTemplate createResourceByTemplate = KubernetesResourceTemplateParam.CreateResourceByTemplate.builder()
                .custom(templateCustom.dump())
                .templateId(kubernetesResourceTemplate.getId())
                .instances(instanceIds)
                .kinds(Set.of(KubernetesResourceKindEnum.DEPLOYMENT.name(), KubernetesResourceKindEnum.SERVICE.name(),
                        KubernetesResourceKindEnum.INGRESS.name()))
                .namespaces(envFacade.getEnvMap()
                        .keySet())
                .build();
        templateFacade.createResourceByTemplate(createResourceByTemplate);
    }

    private String getTemplateKey(boolean isHomePage) {
        return isHomePage ? FE_H5_ROOT : FE_H5_PROJECT;
    }

    private boolean isHomePage(String mappingsPath) {
        return mappingsPath.equals("/") || mappingsPath.isBlank();
    }

}
