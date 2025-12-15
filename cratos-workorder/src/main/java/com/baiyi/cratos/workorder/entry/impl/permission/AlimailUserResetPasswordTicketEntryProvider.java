package com.baiyi.cratos.workorder.entry.impl.permission;

import com.aliyuncs.ram.model.v20150501.GetUserResponse;
import com.baiyi.cratos.common.util.MarkdownUtils;
import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.alimail.repo.AlimailUserRepo;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsIdentityFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.ResetAlimailUserTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.TableHeaderConstants;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.baiyi.cratos.workorder.notice.ResetAlimailUserNoticeSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/27 18:29
 * &#064;Version 1.0
 */
@Slf4j
@Component
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
@WorkOrderKey(key = WorkOrderKeys.ALIMAIL_USER_RESET)
public class AlimailUserResetPasswordTicketEntryProvider extends BaseTicketEntryProvider<EdsIdentityVO.MailAccount, WorkOrderTicketParam.AddResetAlimailUserTicketEntry> {

    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final EdsInstanceService edsInstanceService;
    private final EdsIdentityFacade edsIdentityFacade;
    private final WorkOrderService workOrderService;
    private final UserService userService;
    private final ResetAlimailUserNoticeSender resetAlimailUserNoticeSender;

    public AlimailUserResetPasswordTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                       WorkOrderTicketService workOrderTicketService,
                                                       WorkOrderService workOrderService,
                                                       EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                       EdsInstanceService edsInstanceService,
                                                       EdsIdentityFacade edsIdentityFacade,
                                                       UserService userService,
                                                       ResetAlimailUserNoticeSender resetAlimailUserNoticeSender) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
        this.edsInstanceService = edsInstanceService;
        this.edsIdentityFacade = edsIdentityFacade;
        this.workOrderService = workOrderService;
        this.userService = userService;
        this.resetAlimailUserNoticeSender = resetAlimailUserNoticeSender;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return MarkdownUtils.createTableHeader(TableHeaderConstants.ALIMAIL_USER_RESET);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                EdsIdentityVO.MailAccount mailAccount) throws WorkOrderTicketException {
        EdsInstanceProviderHolder<EdsConfigs.Alimail, GetUserResponse.User> holder = (EdsInstanceProviderHolder<EdsConfigs.Alimail, GetUserResponse.User>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.ALIMAIL_USER.name());
        EdsConfigs.Alimail alimail = holder.getInstance()
                .getConfig();
        String newPassword = PasswordGenerator.generateMailPassword();
        try {
            final String mailUserId = mailAccount.getAccount()
                    .getAssetId();
            AlimailUserRepo.resetPassword(alimail, mailUserId, newPassword);
        } catch (Exception e) {
            WorkOrderTicketException.runtime(e.getMessage());
        }
        sendMsg(workOrderTicket, mailAccount.getAccountLogin()
                .getLoginUsername(), newPassword, mailAccount.getAccountLogin()
                .getLoginUrl());
    }

    /**
     * 发送通知
     *
     * @param workOrderTicket
     * @param mail
     * @param password
     */
    private void sendMsg(WorkOrderTicket workOrderTicket, String mail, String password, String loginURL) {
        try {
            WorkOrder workOrder = workOrderService.getById(workOrderTicket.getWorkOrderId());
            User applicantUser = userService.getByUsername(workOrderTicket.getUsername());
            resetAlimailUserNoticeSender.sendMsg(workOrder, workOrderTicket, mail, password, loginURL, applicantUser);
        } catch (Exception e) {
            throw new WorkOrderTicketException("Sending user notification failed err: {}", e.getMessage());
        }
    }

    @Override
    protected WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddResetAlimailUserTicketEntry param) {
        int assetId = Optional.of(param)
                .map(WorkOrderTicketParam.AddResetAlimailUserTicketEntry::getDetail)
                .map(EdsIdentityVO.MailAccount::getAccount)
                .map(EdsAssetVO.Asset::getId)
                .orElseThrow(() -> new WorkOrderTicketException("Alimail user assetId is null"));
        EdsIdentityVO.MailAccount mailAccount = getAndVerifyMailAccount(assetId);
        return ResetAlimailUserTicketEntryBuilder.newBuilder()
                .withParam(param)
                .withMailAccount(mailAccount)
                .buildEntry();
    }

    private EdsIdentityVO.MailAccount getAndVerifyMailAccount(int assetId) {
        EdsIdentityParam.QueryMailIdentityDetails queryMailIdentityDetails = EdsIdentityParam.QueryMailIdentityDetails.builder()
                .username(SessionUtils.getUsername())
                .build();
        EdsIdentityVO.MailIdentityDetails mailIdentityDetails = edsIdentityFacade.queryMailIdentityDetails(
                queryMailIdentityDetails);
        return mailIdentityDetails.getAccounts()
                .values()
                .stream()
                .flatMap(List::stream)
                .filter(account -> account.getAccount()
                        .getId()
                        .equals(assetId))
                .findFirst()
                .orElseThrow(() -> new WorkOrderTicketException("The email account is not yours."));
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        EdsIdentityVO.MailAccount mailAccount = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        return MarkdownUtils.createTableRow(instance.getInstanceName(), mailAccount.getAccount()
                .getAssetId(), mailAccount.getAccountLogin()
                .getLoginUsername(), mailAccount.getAccountLogin()
                .getLoginUrl());
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("Mail")
                .build();
    }

}
