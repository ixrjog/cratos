package com.baiyi.cratos.domain.param.http.event;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
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
        @JsonDeserialize(using = TimestampDeserializer.class)
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
                String action = alarmActionMatcher.group(1).replace("\\ ", "");
                contentObj.setAction(action);
            }

            // 解析事件名称
            Pattern eventNamePattern = Pattern.compile("事件名称：(.+?)\\s*\\n");
            Matcher eventNameMatcher = eventNamePattern.matcher(content);
            if (eventNameMatcher.find()) {
                String eventName = eventNameMatcher.group(1).replace("\\ ", "");
                contentObj.setEventName(eventName);
            }

            // 解析事件ID
            Pattern eventIdPattern = Pattern.compile("事件ID：(.+?)\\s*\\n");
            Matcher eventIdMatcher = eventIdPattern.matcher(content);
            if (eventIdMatcher.find()) {
                String eventId = eventIdMatcher.group(1).replace("\\ ", "");
                contentObj.setEventId(eventId);
            }

            // 解析告警内容
            Pattern thresholdPattern = Pattern.compile("告警内容：(.+?)\\s*\\n");
            Matcher thresholdMatcher = thresholdPattern.matcher(content);
            if (thresholdMatcher.find()) {
                String threshold = thresholdMatcher.group(1).replace("\\ ", "");
                contentObj.setThreshold(threshold);
            }

            // 解析告警对象
            Pattern entityNamePattern = Pattern.compile("告警对象：(.+?)\\s*\\n");
            Matcher entityNameMatcher = entityNamePattern.matcher(content);
            if (entityNameMatcher.find()) {
                String entityName = entityNameMatcher.group(1).replace("\\ ", "");
                contentObj.setEntityName(entityName);
            }

            // 解析告警时间
            Pattern timePattern = Pattern.compile("告警时间：(.+?)(?:\\s*\\n|$)");
            Matcher timeMatcher = timePattern.matcher(content);
            if (timeMatcher.find()) {
                String timeStr = timeMatcher.group(1).replace("\\ ", "");
                contentObj.setTimeStr(timeStr);
            }
            return contentObj;
        }
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Alert {
        private String actionUrl;
        private String text;
        private String dataTime;
        private Date timestamp;
        private String title;
        private Content content;
        // 接收者
        private Receiver receiver;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class Receiver {
        private String dingtalkUserId;
        private String displayName;
    }

    /**
     * 时间戳反序列化器
     */
    public static class TimestampDeserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String timestamp = p.getText();
            try {
                return new Date(Long.parseLong(timestamp));
            } catch (NumberFormatException e) {
                throw new IOException("Invalid timestamp format: " + timestamp, e);
            }
        }
    }

}
