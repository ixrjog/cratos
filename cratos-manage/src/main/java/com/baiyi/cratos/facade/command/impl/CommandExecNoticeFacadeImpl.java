package com.baiyi.cratos.facade.command.impl;

import com.baiyi.cratos.common.builder.SimpleMapBuilder;
import com.baiyi.cratos.common.enums.NotificationTemplateKeys;
import com.baiyi.cratos.common.util.beetl.BeetlUtil;
import com.baiyi.cratos.domain.generator.CommandExec;
import com.baiyi.cratos.domain.generator.CommandExecApproval;
import com.baiyi.cratos.domain.generator.NotificationTemplate;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.util.LanguageUtils;
import com.baiyi.cratos.facade.command.CommandExecNoticeFacade;
import com.baiyi.cratos.facade.message.EdsDingtalkMessageFacade;
import com.baiyi.cratos.service.CommandExecService;
import com.baiyi.cratos.service.NotificationTemplateService;
import com.baiyi.cratos.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/24 15:59
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CommandExecNoticeFacadeImpl implements CommandExecNoticeFacade {

    private final CommandExecService commandExecService;
    private final NotificationTemplateService notificationTemplateService;
    private final EdsDingtalkMessageFacade dingtalkMessageFacade;
    private final UserService userService;
    private final LanguageUtils languageUtils;

    @Override
    @Async
    public void sendApprovalNotice(CommandExecApproval commandExecApproval) {
        CommandExec commandExec = commandExecService.getById(commandExecApproval.getCommandExecId());
        if (Objects.isNull(commandExec)) {
            return;
        }
        User sendToUser = userService.getByUsername(commandExecApproval.getUsername());
        if (Objects.isNull(sendToUser)) {
            return;
        }
        NotificationTemplate notificationTemplate = getNotificationTemplate(
                NotificationTemplateKeys.COMMAND_EXEC_APPROVAL_NOTICE.name(), sendToUser);
        if (Objects.isNull(notificationTemplate)) {
            return;
        }
        try {
            String msgText = toApprovalMsgText(notificationTemplate.getContent(), commandExec, commandExecApproval);
            dingtalkMessageFacade.sendToDingtalkUser(sendToUser, notificationTemplate, msgText);
        } catch (IOException ioException) {
            log.debug(ioException.getMessage());
        }
    }

    @Override
    @Async
    public void sendApprovalResultNotice(CommandExecApproval commandExecApproval) {
        CommandExec commandExec = commandExecService.getById(commandExecApproval.getCommandExecId());
        if (Objects.isNull(commandExec)) {
            return;
        }
        User sendToUser = userService.getByUsername(commandExec.getUsername());
        if (Objects.isNull(sendToUser)) {
            return;
        }
        NotificationTemplate notificationTemplate = getNotificationTemplate(
                NotificationTemplateKeys.COMMAND_EXEC_APPROVAL_RESULT_NOTICE.name(), sendToUser);
        if (Objects.isNull(notificationTemplate)) {
            return;
        }
        try {
            String msgText = toApprovalResultMsgText(notificationTemplate.getContent(), commandExec,
                    commandExecApproval);
            dingtalkMessageFacade.sendToDingtalkUser(sendToUser, notificationTemplate, msgText);
        } catch (IOException ioException) {
            log.debug(ioException.getMessage());
        }
    }

    private String toApprovalMsgText(String templateContent, CommandExec commandExec,
                                     CommandExecApproval commandExecApproval) throws IOException {
        return BeetlUtil.renderTemplate(templateContent, SimpleMapBuilder.newBuilder()
                .put("id", commandExecApproval.getCommandExecId())
                .put("applicant", commandExec.getUsername())
                .put("applyRemark", commandExec.getApplyRemark())
                .build());
    }

    private String toApprovalResultMsgText(String templateContent, CommandExec commandExec,
                                           CommandExecApproval commandExecApproval) throws IOException {
        return BeetlUtil.renderTemplate(templateContent, SimpleMapBuilder.newBuilder()
                .put("id", commandExecApproval.getCommandExecId())
                .put("approvedBy", commandExecApproval.getUsername())
                .put("approvalStatus", commandExecApproval.getApprovalStatus())
                .put("approveRemark", commandExecApproval.getApproveRemark())
                .build());
    }

    private NotificationTemplate getNotificationTemplate(String notificationTemplateKey, User user) {
        NotificationTemplate query = NotificationTemplate.builder()
                .notificationTemplateKey(notificationTemplateKey)
                .lang(languageUtils.getUserLanguage(user))
                .build();
        return notificationTemplateService.getByUniqueKey(query);
    }

}
