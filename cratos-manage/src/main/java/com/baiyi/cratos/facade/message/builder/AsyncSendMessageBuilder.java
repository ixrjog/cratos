package com.baiyi.cratos.facade.message.builder;

import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.eds.dingtalk.param.DingtalkMessageParam;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/24 17:48
 * &#064;Version 1.0
 */
public class AsyncSendMessageBuilder {

    private NotificationTemplate notificationTemplate;
    private String msgText;
    private String userId;

    public static AsyncSendMessageBuilder newBuilder() {
        return new AsyncSendMessageBuilder();
    }

    public AsyncSendMessageBuilder withMsgText(String msgText) {
        this.msgText = msgText;
        return this;
    }

    public AsyncSendMessageBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public AsyncSendMessageBuilder withNotificationTemplate(NotificationTemplate notificationTemplate) {
        this.notificationTemplate = notificationTemplate;
        return this;
    }

    private DingtalkMessageParam.Markdown makeMarkdown() {
        return DingtalkMessageParam.Markdown.builder()
                .title(this.notificationTemplate.getTitle())
                .text(this.msgText)
                .build();
    }

    private DingtalkMessageParam.Msg makeMsg() {
        return DingtalkMessageParam.Msg.builder()
                .markdown(makeMarkdown())
                .build();
    }

    public DingtalkMessageParam.AsyncSendMessage build() {
        return DingtalkMessageParam.AsyncSendMessage.builder()
                .msg(makeMsg())
                .useridList(userId)
                .build();
    }

}
