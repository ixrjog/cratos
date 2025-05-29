package com.baiyi.cratos.workorder.entry.impl.aliyun;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.GetUserMFAInfoResponse;
import com.aliyuncs.ram.model.v20150501.GetUserResponse;
import com.aliyuncs.ram.model.v20150501.UnbindMFADeviceResponse;
import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.model.AliyunModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRamUserRepo;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsAssetService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.ResetAliyunRamUserTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.baiyi.cratos.workorder.notice.ResetAliyunRamUserNoticeSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static com.baiyi.cratos.eds.aliyun.repo.AliyunRamUserRepo.NO_PASSWORD_RESET_REQUIRED;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/21 16:21
 * &#064;Version 1.0
 */
@Slf4j
@Component
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
@WorkOrderKey(key = WorkOrderKeys.ALIYUN_RAM_USER_RESET)
public class AliyunRamUserResetTicketEntryProvider extends BaseTicketEntryProvider<AliyunModel.ResetAliyunAccount, WorkOrderTicketParam.AddResetAliyunRamUserTicketEntry> {

    private final EdsInstanceService edsInstanceService;
    private final AliyunRamUserRepo aliyunRamUserRepo;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;

    private final UserService userService;
    private final WorkOrderService workOrderService;
    private final ResetAliyunRamUserNoticeSender resetAliyunRamUserNoticeSender;
    private final EdsAssetService edsAssetService;

    public AliyunRamUserResetTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                 WorkOrderTicketService workOrderTicketService,
                                                 WorkOrderService workOrderService,
                                                 EdsInstanceService edsInstanceService,
                                                 AliyunRamUserRepo aliyunRamUserRepo,
                                                 EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                 UserService userService,
                                                 ResetAliyunRamUserNoticeSender resetAliyunRamUserNoticeSender,
                                                 EdsAssetService edsAssetService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
        this.aliyunRamUserRepo = aliyunRamUserRepo;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;

        this.userService = userService;
        this.workOrderService = workOrderService;
        this.resetAliyunRamUserNoticeSender = resetAliyunRamUserNoticeSender;
        this.edsAssetService = edsAssetService;
    }

    private static final String ROW_TPL = "| {} | {} | {} | {} | {} |";

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return """
                | Aliyun Instance | RAM Login Username | Reset Password | Unbind MFA | Login Link |
                | --- | --- | --- | --- | --- |
                """;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                AliyunModel.ResetAliyunAccount resetAliyunAccount) throws WorkOrderTicketException {
        // 创建Aliyun RAM
        EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetUserResponse.User> holder = (EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetUserResponse.User>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.ALIYUN_RAM_USER.name());
        EdsAliyunConfigModel.Aliyun aliyun = holder.getInstance()
                .getEdsConfigModel();
        String ramUsername = resetAliyunAccount.getRamUsername();
        String ramLoginUsername = resetAliyunAccount.getRamLoginUsername();
        String username = resetAliyunAccount.getUsername();
        if (Boolean.TRUE.equals(resetAliyunAccount.getResetPassword())) {
            final String newPassword = PasswordGenerator.generatePassword();
            // 重置密码
            resetRAMUserPassword(aliyun, ramUsername, newPassword);
            // 发送通知
            sendMsg(workOrderTicket, username, ramLoginUsername, newPassword, aliyun.getRam()
                    .toLoginUrl());
        }
        if (Boolean.TRUE.equals(resetAliyunAccount.getUnbindMFA())) {
            // 解绑MFA
            unbindMFA(aliyun, ramUsername);
        }
    }

    private void resetRAMUserPassword(EdsAliyunConfigModel.Aliyun aliyun, String ramUsername, String newPassword) {
        try {
            aliyunRamUserRepo.updateLoginProfile(aliyun, ramUsername, newPassword, NO_PASSWORD_RESET_REQUIRED);
        } catch (ClientException clientException) {
            WorkOrderTicketException.runtime(clientException.getMessage());
        }
    }

    private void unbindMFA(EdsAliyunConfigModel.Aliyun aliyun, String ramUsername) {
        try {
            GetUserMFAInfoResponse.MFADevice mfaDevice = aliyunRamUserRepo.getUserMFAInfo(aliyun, ramUsername);
            String serialNumber = Optional.ofNullable(mfaDevice)
                    .map(GetUserMFAInfoResponse.MFADevice::getSerialNumber)
                    .orElse(null);
            if (StringUtils.hasText(serialNumber)) {
                UnbindMFADeviceResponse.MFADevice unbindMfaDevice = aliyunRamUserRepo.unbindVirtualMFADevice(aliyun,
                        ramUsername);
                Optional.ofNullable(unbindMfaDevice)
                        .map(UnbindMFADeviceResponse.MFADevice::getSerialNumber)
                        .ifPresent(sn -> {
                            try {
                                aliyunRamUserRepo.deleteVirtualMFADevice(aliyun, sn);
                            } catch (ClientException e) {
                                log.debug(e.getMessage());
                            }
                        });
            }
        } catch (ClientException clientException) {
            WorkOrderTicketException.runtime("Unbind RAM user MFA err: {}", clientException.getMessage());
        }
    }

    /**
     * 发送通知
     *
     * @param workOrderTicket
     * @param username
     * @param ramLoginUsername
     */
    private void sendMsg(WorkOrderTicket workOrderTicket, String username, String ramLoginUsername, String password,
                         String loginLink) {
        try {
            WorkOrder workOrder = workOrderService.getById(workOrderTicket.getWorkOrderId());
            User applicantUser = userService.getByUsername(username);
            resetAliyunRamUserNoticeSender.sendMsg(workOrder, workOrderTicket, ramLoginUsername, password, loginLink,
                    applicantUser);
        } catch (Exception e) {
            throw new WorkOrderTicketException("Sending user notification failed err: {}", e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddResetAliyunRamUserTicketEntry param) {
        int instanceId = Optional.ofNullable(param)
                .map(WorkOrderTicketParam.AddResetAliyunRamUserTicketEntry::getDetail)
                .map(AliyunModel.ResetAliyunAccount::getAsset)
                .map(EdsAssetVO.Asset::getInstanceId)
                .orElseThrow(() -> new WorkOrderTicketException("Aliyun RAM user asset is null"));
        AliyunModel.ResetAliyunAccount resetAliyunAccount = param.getDetail();
        EdsAsset edsAsset = edsAssetService.getById(resetAliyunAccount.getAsset()
                .getId());
        // 校验账户
        if (!edsAsset.getAssetKey()
                .equals(resetAliyunAccount.getAsset()
                        .getAssetKey()) || !edsAsset.getInstanceId()
                .equals(instanceId)) {
            WorkOrderTicketException.runtime("Aliyun RAM user asset is null.");
        }
        EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetUserResponse.User> holder = (EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetUserResponse.User>) edsInstanceProviderHolderBuilder.newHolder(
                instanceId, EdsAssetTypeEnum.ALIYUN_RAM_USER.name());
        EdsAliyunConfigModel.Aliyun aliyun = holder.getInstance()
                .getEdsConfigModel();
        return ResetAliyunRamUserTicketEntryBuilder.newBuilder()
                .withParam(param)
                .withUsername(SessionUtils.getUsername())
                .withAliyun(aliyun)
                .buildEntry();
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        AliyunModel.ResetAliyunAccount resetAliyunAccount = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        String resetPassword = Boolean.TRUE.equals(resetAliyunAccount.getResetPassword()) ? "Yes" : "No";
        String unbindMFA = Boolean.TRUE.equals(resetAliyunAccount.getUnbindMFA()) ? "Yes" : "No";
        return StringFormatter.arrayFormat(ROW_TPL, instance.getInstanceName(), resetAliyunAccount.getAccount(),
                resetPassword, unbindMFA, resetAliyunAccount.getLoginLink());
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("RAM User")
                .build();
    }

}
