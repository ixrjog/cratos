package com.baiyi.cratos.facade.application.model;

import com.baiyi.cratos.common.exception.ApplicationConfigException;
import com.baiyi.cratos.domain.YamlDump;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.Application;
import com.google.common.collect.Maps;
import com.google.gson.JsonSyntaxException;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 17:41
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConfigModel {

    public static ApplicationConfigModel.Config loadAs(Application application) {
        if (application == null) {
            return ApplicationConfigModel.Config.EMPTY;
        }
        return loadAs(application.getConfig());
    }

    public static ApplicationConfigModel.Config loadAs(String content) {
        if (StringUtils.isBlank(content)) {
            return ApplicationConfigModel.Config.EMPTY;
        }
        try {
            return YamlUtils.loadAs(content, ApplicationConfigModel.Config.class);
        } catch (JsonSyntaxException e) {
            throw new ApplicationConfigException("Application config format error: {}", e.getMessage());
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Config extends YamlDump {
        public static final ApplicationConfigModel.Config EMPTY = ApplicationConfigModel.Config.builder()
                .build();
        @Builder.Default
        private Map<String, String> data = Maps.newHashMap();

        private App application;
        private Repository repository;
        private List<Repository> repositories;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class App {
        private String name;
        private String service;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Repository {
        private String type;
        private String sshUrl;
        private String webUrl;
    }

}
