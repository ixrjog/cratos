package com.baiyi.cratos.facade.message.builder;

import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.dingtalk.param.DingtalkMessageParam;
import com.baiyi.cratos.eds.dingtalk.sender.DingtalkMessageSender;
import lombok.extern.slf4j.Slf4j;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/24 17:48
 * &#064;Version 1.0
 */
@Slf4j
public class AsyncSendMessageAgency {

    private NotificationTemplate notificationTemplate;
    private String msgText;
    private String userId;
    private DingtalkMessageSender dingtalkMessageSender;
    private EdsConfigs.Dingtalk dingtalk;

    public static AsyncSendMessageAgency newBuilder() {
        return new AsyncSendMessageAgency();
    }

    public AsyncSendMessageAgency withMsgText(String msgText) {
        this.msgText = msgText;
        return this;
    }

    public AsyncSendMessageAgency withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public AsyncSendMessageAgency withNotificationTemplate(NotificationTemplate notificationTemplate) {
        this.notificationTemplate = notificationTemplate;
        return this;
    }

    public AsyncSendMessageAgency withDingtalkMessageSender(DingtalkMessageSender dingtalkMessageSender) {
        this.dingtalkMessageSender = dingtalkMessageSender;
        return this;
    }

    public AsyncSendMessageAgency withDingtalk(EdsConfigs.Dingtalk dingtalk) {
        this.dingtalk = dingtalk;
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

    public void asyncSend() {
        try {
            DingtalkMessageParam.AsyncSendMessage asyncSendMessage = DingtalkMessageParam.AsyncSendMessage.builder()
                    .msg(makeMsg())
                    .useridList(userId)
                    .build();
            this.dingtalkMessageSender.asyncSend(dingtalk, asyncSendMessage);
        } catch (Exception ex) {
            log.debug(ex.getMessage());
        }
    }

}
