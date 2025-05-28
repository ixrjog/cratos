package com.baiyi.cratos.workorder.entry.impl.permission;

import com.aliyuncs.ram.model.v20150501.GetUserResponse;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.alimail.model.AlimailUser;
import com.baiyi.cratos.eds.core.config.EdsAlimailConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsIdentityFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.ResetAlimailUserTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
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

    public AlimailUserResetPasswordTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                       WorkOrderTicketService workOrderTicketService,
                                                       WorkOrderService workOrderService,
                                                       EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                       EdsInstanceService edsInstanceService,
                                                       EdsIdentityFacade edsIdentityFacade) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
        this.edsInstanceService = edsInstanceService;
        this.edsIdentityFacade = edsIdentityFacade;
    }

    private static final String ROW_TPL = "| {} | {} | {} |";

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return """
                | Alimail Instance | User ID | Mail |
                | --- | --- | --- |
                """;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                EdsIdentityVO.MailAccount mailAccount) throws WorkOrderTicketException {
        EdsInstanceProviderHolder<EdsAlimailConfigModel.Alimail, GetUserResponse.User> holder = (EdsInstanceProviderHolder<EdsAlimailConfigModel.Alimail, GetUserResponse.User>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.ALIMAIL_USER.name());
        EdsAlimailConfigModel.Alimail alimail = holder.getInstance()
                .getEdsConfigModel();

    }

    /**
     * 发送通知
     *
     * @param workOrderTicket
     * @param mail
     * @param password
     */
    private void sendMsg(WorkOrderTicket workOrderTicket, String mail, String password) {
        try {
//            WorkOrder workOrder = workOrderService.getById(workOrderTicket.getWorkOrderId());
//            User applicantUser = userService.getByUsername(username);
//            resetAliyunRamUserNoticeHelper.sendMsg(workOrder, workOrderTicket, ramLoginUsername, password, loginLink,
//                    applicantUser);
        } catch (Exception e) {
            throw new WorkOrderTicketException("Sending user notification failed err: {}", e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddResetAlimailUserTicketEntry param) {
        int assetId = Optional.of(param)
                .map(WorkOrderTicketParam.AddResetAlimailUserTicketEntry::getDetail)
                .map(EdsIdentityVO.MailAccount::getAccount)
                .map(EdsAssetVO.Asset::getId)
                .orElseThrow(() -> new WorkOrderTicketException("Alimail user assetId is null"));
        EdsIdentityVO.MailAccount mailAccount = getAndVerifyMailAccount(assetId);
        EdsInstanceProviderHolder<EdsAlimailConfigModel.Alimail, AlimailUser.User> holder = (EdsInstanceProviderHolder<EdsAlimailConfigModel.Alimail, AlimailUser.User>) edsInstanceProviderHolderBuilder.newHolder(
                mailAccount.getAccount()
                        .getInstanceId(), EdsAssetTypeEnum.ALIMAIL_USER.name());
        EdsAlimailConfigModel.Alimail alimail = holder.getInstance()
                .getEdsConfigModel();
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
        return StringFormatter.arrayFormat(ROW_TPL, instance.getInstanceName(), mailAccount.getAccount()
                .getAssetId(), mailAccount.getAccountLogin()
                .getLoginUsername());
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
