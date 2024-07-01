package com.baiyi.cratos.domain.param.template;

import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.domain.param.IToTarget;
import com.baiyi.cratos.domain.param.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/7 上午10:02
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class NotificationTemplateParam {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @SuperBuilder(toBuilder = true)
    @NoArgsConstructor
    @Schema
    public static class NotificationTemplatePageQuery extends PageParam {
        @Schema(description = "Query by name")
        private String queryName;
    }

    @Data
    @Schema
    public static class AddNotificationTemplate implements IToTarget<NotificationTemplate> {

        private String name;

        private String notificationTemplateKey;

        private String notificationTemplateType;

        private String title;

        private String consumer;

        private String lang;

        private String content;

        private String comment;

    }

    @Data
    @Schema
    public static class UpdateNotificationTemplate implements IToTarget<NotificationTemplate> {

        private Integer id;

        private String name;

        private String notificationTemplateKey;

        private String notificationTemplateType;

        private String title;

        private String consumer;

        private String lang;

        private String content;

        private String comment;

    }

}
