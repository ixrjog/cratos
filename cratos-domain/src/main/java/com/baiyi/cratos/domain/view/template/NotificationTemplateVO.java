package com.baiyi.cratos.domain.view.template;

import com.baiyi.cratos.domain.view.BaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;

import static lombok.AccessLevel.PRIVATE;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/6/7 上午10:00
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public class NotificationTemplateVO {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class NotificationTemplate extends BaseVO  {
        @Serial
        private static final long serialVersionUID = 5468140178995933859L;
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
