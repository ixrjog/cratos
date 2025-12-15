package com.baiyi.cratos.workorder.entry.impl.aws;

import com.amazonaws.services.identitymanagement.model.VirtualMFADevice;
import com.baiyi.cratos.common.enums.SysTagKeys;
import com.baiyi.cratos.common.exception.EnableVirtualMFAException;
import com.baiyi.cratos.common.util.MarkdownUtils;
import com.baiyi.cratos.common.util.PasswordGenerator;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.*;
import com.baiyi.cratos.domain.model.AwsModel;
import com.baiyi.cratos.domain.param.http.tag.BusinessTagParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.aws.delegate.AwsMFADelegate;
import com.baiyi.cratos.eds.aws.repo.iam.AwsIamUserRepo;
import com.baiyi.cratos.eds.aws.repo.iam.AwsMFADeviceRepo;
import com.baiyi.cratos.eds.core.config.EdsConfigs;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.TagService;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.CreateAwsIamUserTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.TableHeaderConstants;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import com.baiyi.cratos.workorder.notice.CreateAwsIamUserNoticeSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static com.baiyi.cratos.eds.aliyun.repo.AliyunRamUserRepo.CREATE_LOGIN_PROFILE;
import static com.baiyi.cratos.eds.aws.repo.iam.AwsMFADeviceRepo.SERIAL_NUMBER_TPL;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/3 16:46
 * &#064;Version 1.0
 */
@Slf4j
@Component
@BusinessType(type = BusinessTypeEnum.EDS_INSTANCE)
@WorkOrderKey(key = WorkOrderKeys.AWS_IAM_USER_PERMISSION)
public class AwsIamUserPermissionTicketEntryProvider extends BaseTicketEntryProvider<AwsModel.AwsAccount, WorkOrderTicketParam.AddCreateAwsIamUserTicketEntry> {

    private final EdsInstanceService edsInstanceService;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final BusinessTagFacade businessTagFacade;
    private final TagService tagService;
    private final UserService userService;
    private final WorkOrderService workOrderService;
    private final CreateAwsIamUserNoticeSender createAwsIamUserNoticeSender;
    private final AwsIamUserRepo awsIamUserRepo;
    private final AwsMFADeviceRepo awsMFADeviceRepo;
    private final AwsMFADelegate awsMFADelegate;

    public AwsIamUserPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                   WorkOrderTicketService workOrderTicketService,
                                                   WorkOrderService workOrderService,
                                                   EdsInstanceService edsInstanceService,
                                                   EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                   BusinessTagFacade businessTagFacade, TagService tagService,
                                                   UserService userService,
                                                   CreateAwsIamUserNoticeSender createAliyunRamUserNoticeSender,
                                                   AwsIamUserRepo awsIamUserRepo, AwsMFADeviceRepo awsMFADeviceRepo,
                                                   AwsMFADelegate awsMFADelegate) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
        this.businessTagFacade = businessTagFacade;
        this.tagService = tagService;
        this.userService = userService;
        this.workOrderService = workOrderService;
        this.createAwsIamUserNoticeSender = createAliyunRamUserNoticeSender;
        this.awsIamUserRepo = awsIamUserRepo;
        this.awsMFADeviceRepo = awsMFADeviceRepo;
        this.awsMFADelegate = awsMFADelegate;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return MarkdownUtils.createTableHeader(TableHeaderConstants.AWS_IAM_USER_PERMISSION);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                AwsModel.AwsAccount awsAccount) throws WorkOrderTicketException {
        EdsInstanceProviderHolder<EdsConfigs.Aws, com.amazonaws.services.identitymanagement.model.User> holder = (EdsInstanceProviderHolder<EdsConfigs.Aws, com.amazonaws.services.identitymanagement.model.User>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.AWS_IAM_USER.name());
        EdsConfigs.Aws aws = holder.getInstance()
                .getConfig();
        String iamUsername = awsAccount.getIamUsername();
        String username = awsAccount.getUsername();
        final String password = PasswordGenerator.generatePassword();
        com.amazonaws.services.identitymanagement.model.User createUser = createIamUser(aws, iamUsername, password);
        VirtualMFADevice virtualMFADevice = enableMFADevice(aws, iamUsername);
        // TODO 录入MFA
        // 发送通知
        final String secretKey = new String(virtualMFADevice.getBase32StringSeed()
                                                    .array());
        sendMsg(workOrderTicket, aws, username, iamUsername, password, secretKey);
        // 导入资产
        com.amazonaws.services.identitymanagement.model.User newUser = getIamUser(aws, iamUsername);
        EdsAsset asset = holder.importAsset(newUser);
        // 关联用户
        addUsernameTag(asset, username);
    }

    /**
     * 发送通知
     *
     * @param workOrderTicket
     * @param username
     * @param iamUsername
     * @param password
     */
    private void sendMsg(WorkOrderTicket workOrderTicket, EdsConfigs.Aws aws, String username,
                         String iamUsername, String password, String secretKey) {
        try {
            WorkOrder workOrder = workOrderService.getById(workOrderTicket.getWorkOrderId());
            User applicantUser = userService.getByUsername(username);
            final String accountId = aws.getCred()
                    .getId();
            final String loginUrl = aws.getIam()
                    .toLoginUrl();
            createAwsIamUserNoticeSender.sendMsg(
                    workOrder, workOrderTicket, accountId, iamUsername, password, secretKey, loginUrl, applicantUser);
        } catch (Exception e) {
            throw new WorkOrderTicketException("Sending user notification failed err: {}", e.getMessage());
        }
    }

    private com.amazonaws.services.identitymanagement.model.User getIamUser(EdsConfigs.Aws aws,
                                                                            String iamUsername) {
        try {
            return awsIamUserRepo.getUser(aws, iamUsername);
        } catch (Exception ex) {
            throw new WorkOrderTicketException("Failed to get AWS IAM user err: {}", ex.getMessage());
        }
    }

    private com.amazonaws.services.identitymanagement.model.User createIamUser(EdsConfigs.Aws aws,
                                                                               String iamUsername, String password) {
        try {
            return awsIamUserRepo.createUser(aws, iamUsername, password, CREATE_LOGIN_PROFILE);
        } catch (Exception ex) {
            throw new WorkOrderTicketException("Failed to create AWS IAM user err: {}", ex.getMessage());
        }
    }

    private VirtualMFADevice enableMFADevice(EdsConfigs.Aws aws, String iamUsername) {
        final String serialNumber = StringFormatter.arrayFormat(
                SERIAL_NUMBER_TPL, aws.getCred()
                        .getId(), iamUsername
        );
        try {
            awsMFADeviceRepo.deleteVirtualMFADevice(aws, serialNumber);
            log.debug("Delete IAM virtual MFA device: serialNumber={}", serialNumber);
        } catch (Exception e) {
            log.debug("Failed to delete IAM virtual MFA device: serialNumber={}, {}", serialNumber, e.getMessage());
        }
        try {
            log.info("Create IAM virtual MFA for user {}", iamUsername);
            VirtualMFADevice vMFADevice = awsMFADeviceRepo.createVirtualMFADevice(aws, iamUsername);
            awsMFADelegate.enableMFADevice(aws, iamUsername, vMFADevice);
            return vMFADevice;
        } catch (Exception e) {
            log.error("Failed to enable IAM virtual MFA: {}", e.getMessage());
            throw new EnableVirtualMFAException(e.getMessage());
        }
    }

    private void addUsernameTag(EdsAsset asset, String username) {
        // 标签
        Tag usernameTag = tagService.getByTagKey(SysTagKeys.USERNAME);
        if (Objects.isNull(usernameTag)) {
            return;
        }
        BusinessTagParam.SaveBusinessTag saveBusinessTag = BusinessTagParam.SaveBusinessTag.builder()
                .tagId(usernameTag.getId())
                .businessType(BusinessTypeEnum.EDS_ASSET.name())
                .businessId(asset.getId())
                .tagValue(username)
                .build();
        businessTagFacade.saveBusinessTag(saveBusinessTag);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddCreateAwsIamUserTicketEntry param) {
        int instanceId = Optional.of(param)
                .map(WorkOrderTicketParam.AddCreateAwsIamUserTicketEntry::getDetail)
                .map(AwsModel.AwsAccount::getEdsInstance)
                .map(EdsInstanceVO.EdsInstance::getId)
                .orElseThrow(() -> new WorkOrderTicketException("Eds instanceId is null"));
        EdsInstanceProviderHolder<EdsConfigs.Aws, com.amazonaws.services.identitymanagement.model.User> holder = (EdsInstanceProviderHolder<EdsConfigs.Aws, com.amazonaws.services.identitymanagement.model.User>) edsInstanceProviderHolderBuilder.newHolder(
                param.getDetail()
                        .getEdsInstance()
                        .getId(), EdsAssetTypeEnum.AWS_IAM_USER.name()
        );
        EdsConfigs.Aws aws = holder.getInstance()
                .getConfig();
        return CreateAwsIamUserTicketEntryBuilder.newBuilder()
                .withParam(param)
                .withUsername(SessionUtils.getUsername())
                .withAws(aws)
                .buildEntry();
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        AwsModel.AwsAccount account = loadAs(entry);
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        return MarkdownUtils.createTableRow(
                instance.getInstanceName(), account.getAccountId(),
                account.getIamUsername(), account.getLoginLink()
        );
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
