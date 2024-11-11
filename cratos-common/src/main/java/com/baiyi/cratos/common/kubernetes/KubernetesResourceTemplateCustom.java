package com.baiyi.cratos.common.kubernetes;

import com.baiyi.cratos.common.exception.KubernetesResourceTemplateException;
import com.baiyi.cratos.common.util.YamlUtil;
import com.baiyi.cratos.domain.YamlDump;
import com.baiyi.cratos.domain.generator.KubernetesResourceTemplate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonSyntaxException;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/6 10:45
 * &#064;Version 1.0
 */
public class KubernetesResourceTemplateCustom {

    public static Custom loadAs(KubernetesResourceTemplate kubernetesResourceTemplate) {
        if (kubernetesResourceTemplate == null) {
            return Custom.EMPTY;
        }
        return loadAs(kubernetesResourceTemplate.getCustom());
    }

    public static Custom loadAs(String content) {
        if (StringUtils.isBlank(content)) {
            return Custom.EMPTY;
        }
        try {
            return YamlUtil.loadAs(content, Custom.class);
        } catch (JsonSyntaxException e) {
            throw new KubernetesResourceTemplateException("Custom content format error: {}", e.getMessage());
        }
    }

    /**
     * 合并
     *
     * @param featureCustom
     * @param mainCustom
     * @return
     */
    public static Custom merge(Custom featureCustom, Custom mainCustom) {
        Custom custom = Custom.builder()
                .build();
        custom.data.putAll(mainCustom.data);
        custom.data.putAll(featureCustom.data);
        if (!CollectionUtils.isEmpty(featureCustom.getInstances())) {
            custom.instances = featureCustom.instances;
        } else {
            custom.instances = mainCustom.instances;
        }
        return custom;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Custom extends YamlDump {
        public static final Custom EMPTY = Custom.builder()
                .build();
        @Builder.Default
        private Map<String, String> data = Maps.newHashMap();
        @Builder.Default
        private List<KubernetesInstance> instances = Lists.newArrayList();
//        @Builder.Default
//        private Set<String> namespaces = Sets.newHashSet();
//        @Builder.Default
//        private Set<String> kinds = Sets.newHashSet();
        @Builder.Default
        private List<Strategy> strategies = Lists.newArrayList();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KubernetesInstance {
        private String name;
        private Integer id;
        private List<String> namespaces;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SortableNamespace  {
        String namespace;
        int order;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Strategy {
        private String name;
        private String value;
        private Integer order;
    }

}
