package com.baiyi.cratos.workorder.entry.impl.aws;

import com.baiyi.cratos.common.util.MarkdownUtils;
import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.AwsModel;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.aws.repo.iam.AwsIamUserRepo;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsIdentityFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.ResetAwsIamUserTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.baiyi.cratos.workorder.notice.ResetAwsIamUserNoticeSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.baiyi.cratos.eds.aws.repo.iam.AwsIamUserRepo.NO_PASSWORD_RESET_REQUIRED;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/29 10:11
 * &#064;Version 1.0
 */
@Slf4j
@Component
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
@WorkOrderKey(key = WorkOrderKeys.AWS_IAM_USER_RESET)
public class AwsIamUserResetTicketEntryProvider extends BaseTicketEntryProvider<AwsModel.ResetAwsAccount, WorkOrderTicketParam.AddResetAwsIamUserTicketEntry> {

    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final UserService userService;
    private final WorkOrderService workOrderService;
    private final EdsIdentityFacade edsIdentityFacade;
    private final AwsIamUserRepo awsIamUserRepo;
    private final ResetAwsIamUserNoticeSender resetAwsIamUserNoticeSender;

    public AwsIamUserResetTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                              WorkOrderTicketService workOrderTicketService,
                                              WorkOrderService workOrderService,
                                              EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                              UserService userService, EdsIdentityFacade edsIdentityFacade,
                                              AwsIamUserRepo awsIamUserRepo,
                                              ResetAwsIamUserNoticeSender resetAwsIamUserNoticeSender) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
        this.userService = userService;
        this.workOrderService = workOrderService;
        this.edsIdentityFacade = edsIdentityFacade;
        this.awsIamUserRepo = awsIamUserRepo;
        this.resetAwsIamUserNoticeSender = resetAwsIamUserNoticeSender;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return MarkdownUtils.generateMarkdownSeparator(
                "| Aws Instance | Account ID or alias | IAM Username | Login Link |");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                AwsModel.ResetAwsAccount resetAwsAccount) throws WorkOrderTicketException {
        EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, com.amazonaws.services.identitymanagement.model.User> holder = (EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, com.amazonaws.services.identitymanagement.model.User>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.AWS_IAM_USER.name());
        EdsAwsConfigModel.Aws aws = holder.getInstance()
                .getEdsConfigModel();
        final String newPassword = PasswordGenerator.generatePassword();
        resetIAMUserPassword(aws, resetAwsAccount.getAccountLogin()
                .getLoginUsername(), newPassword);
        sendMsg(workOrderTicket, workOrderTicket.getUsername(), resetAwsAccount.getAccountLogin()
                .getAccountId(), resetAwsAccount.getAccountLogin()
                .getLoginUsername(), newPassword, resetAwsAccount.getAccountLogin()
                .getLoginUrl());
    }

    private void resetIAMUserPassword(EdsAwsConfigModel.Aws aws, String iamUsername, String newPassword) {
        try {
            awsIamUserRepo.updateLoginProfile(aws, iamUsername, newPassword, NO_PASSWORD_RESET_REQUIRED);
            log.info("Reset AWS IAM user password successfully: {}", iamUsername);
        } catch (Exception e) {
            throw new WorkOrderTicketException("Reset AWS IAM user password failed err: {}", e.getMessage());
        }
    }

    private void sendMsg(WorkOrderTicket workOrderTicket, String username, String accountId, String iamLoginUsername,
                         String password, String loginLink) {
        try {
            WorkOrder workOrder = workOrderService.getById(workOrderTicket.getWorkOrderId());
            User applicantUser = userService.getByUsername(username);
            resetAwsIamUserNoticeSender.sendMsg(workOrder, workOrderTicket, accountId, iamLoginUsername, password,
                    loginLink, applicantUser);
        } catch (Exception e) {
            throw new WorkOrderTicketException("Sending user notification failed err: {}", e.getMessage());
        }
    }

    @Override
    protected WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddResetAwsIamUserTicketEntry param) {
        int assetId = Optional.ofNullable(param)
                .map(WorkOrderTicketParam.AddResetAwsIamUserTicketEntry::getDetail)
                .map(AwsModel.ResetAwsAccount::getAccount)
                .map(EdsAssetVO.Asset::getId)
                .orElseThrow(() -> new WorkOrderTicketException("AWS IAM user asset is null"));
        EdsIdentityVO.CloudAccount cloudAccount = getAndVerifyCloudAccount(assetId);
        return ResetAwsIamUserTicketEntryBuilder.newBuilder()
                .withParam(param)
                .withCloudAccount(cloudAccount)
                .buildEntry();
    }

    private EdsIdentityVO.CloudAccount getAndVerifyCloudAccount(int assetId) {
        EdsIdentityParam.QueryCloudIdentityDetails queryCloudIdentityDetails = EdsIdentityParam.QueryCloudIdentityDetails.builder()
                .username(SessionUtils.getUsername())
                .instanceType(EdsInstanceTypeEnum.AWS.name())
                .build();
        EdsIdentityVO.CloudIdentityDetails cloudIdentityDetails = edsIdentityFacade.queryCloudIdentityDetails(
                queryCloudIdentityDetails);
        return cloudIdentityDetails.getAccounts()
                .values()
                .stream()
                .flatMap(List::stream)
                .filter(account -> account.getAccount()
                        .getId()
                        .equals(assetId))
                .findFirst()
                .orElseThrow(() -> new WorkOrderTicketException("The AWS IAM account is not yours."));
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        EdsIdentityVO.CloudAccount cloudAccount = loadAs(entry);
        //       | Aws Instance | Account ID or alias | IAM Username | Login Link |
        String instanceName = Optional.of(cloudAccount)
                .map(EdsIdentityVO.CloudAccount::getInstance)
                .map(EdsInstanceVO.EdsInstance::getInstanceName)
                .orElse("--");
        String accountId = Optional.ofNullable(cloudAccount.getAccountLogin())
                .map(EdsIdentityVO.AccountLoginDetails::getAccountId)
                .orElse("--");
        String iamUsername = Optional.ofNullable(cloudAccount.getAccountLogin())
                .map(EdsIdentityVO.AccountLoginDetails::getLoginUsername)
                .orElse("--");
        String link = Optional.ofNullable(cloudAccount.getAccountLogin())
                .map(EdsIdentityVO.AccountLoginDetails::getLoginUrl)
                .orElse("--");
        return MarkdownUtils.generateMarkdownTableRow(instanceName, accountId, iamUsername, link);
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("IAM User")
                .build();
    }

}
