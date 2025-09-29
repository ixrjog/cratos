package com.baiyi.cratos.domain.param.http.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/9/29 14:32
 * &#064;Version 1.0
 */
public class EagleCloudEventParam {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class SaseHook {
        @Schema(description = "跳转URL")
        private String actionUrl;
        @Schema(description = "消息内容")
        private String content;
        @Schema(description = "数据时间")
        private String dataTime;
        @Schema(description = "时间戳")
        private Date timestamp;
        @Schema(description = "消息标题")
        private String title;

    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Content {
        @Schema(description = "告警动作")
        private String action;
        @Schema(description = "事件名称")
        private String eventName;
        @Schema(description = "事件ID")
        private String eventId;
        @Schema(description = "告警内容")
        private String threshold;
        @Schema(description = "告警对象")
        private String entityName;
        @Schema(description = "告警时间")
        private String timeStr;

        /**
         * 解析content内容为Content对象
         */
        public static Content parse(SaseHook saseHook) {
            String content = saseHook.getContent();
            if (content == null || content.isEmpty()) {
                return null;
            }
            Content contentObj = new Content();
            // 解析告警动作
            Pattern alarmActionPattern = Pattern.compile("敏感数据(.+?)告警通知");
            Matcher alarmActionMatcher = alarmActionPattern.matcher(content);
            if (alarmActionMatcher.find()) {
                contentObj.setAction(alarmActionMatcher.group(1));
            }

            // 解析事件名称
            Pattern eventNamePattern = Pattern.compile("事件名称：(.+?)\\s*\\n");
            Matcher eventNameMatcher = eventNamePattern.matcher(content);
            if (eventNameMatcher.find()) {
                contentObj.setEventName(eventNameMatcher.group(1));
            }

            // 解析事件ID
            Pattern eventIdPattern = Pattern.compile("事件ID：(.+?)\\s*\\n");
            Matcher eventIdMatcher = eventIdPattern.matcher(content);
            if (eventIdMatcher.find()) {
                contentObj.setEventId(eventIdMatcher.group(1));
            }

            // 解析告警内容
            Pattern thresholdPattern = Pattern.compile("告警内容：(.+?)\\s*\\n");
            Matcher thresholdMatcher = thresholdPattern.matcher(content);
            if (thresholdMatcher.find()) {
                contentObj.setThreshold(thresholdMatcher.group(1));
            }

            // 解析告警对象
            Pattern entityNamePattern = Pattern.compile("告警对象：(.+?)\\s*\\n");
            Matcher entityNameMatcher = entityNamePattern.matcher(content);
            if (entityNameMatcher.find()) {
                contentObj.setEntityName(entityNameMatcher.group(1));
            }

            // 解析告警时间
            Pattern timePattern = Pattern.compile("告警时间：(.+?)(?:\\s*\\n|$)");
            Matcher timeMatcher = timePattern.matcher(content);
            if (timeMatcher.find()) {
                contentObj.setTimeStr(timeMatcher.group(1));
            }
            return contentObj;
        }
    }

}
