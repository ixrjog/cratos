package com.baiyi.cratos.workorder.entry.impl.aws;

import com.amazonaws.auth.policy.Policy;
import com.amazonaws.services.identitymanagement.model.User;
import com.baiyi.cratos.common.util.MarkdownUtils;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.AwsModel;
import com.baiyi.cratos.domain.param.http.eds.EdsIdentityParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.domain.view.eds.EdsIdentityVO;
import com.baiyi.cratos.eds.aws.repo.iam.AwsIamPolicyRepo;
import com.baiyi.cratos.eds.aws.repo.iam.AwsIamUserRepo;
import com.baiyi.cratos.eds.core.config.EdsAwsConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.enums.EdsInstanceTypeEnum;
import com.baiyi.cratos.eds.core.facade.EdsIdentityFacade;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.AwsIamPolicyPermissionTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.TableHeaderConstants;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/6/5 14:35
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
@WorkOrderKey(key = WorkOrderKeys.AWS_IAM_POLICY_PERMISSION)
public class AwsIamPolicyPermissionTicketEntryProvider extends BaseTicketEntryProvider<AwsModel.AwsPolicy, WorkOrderTicketParam.AddAwsIamPolicyPermissionTicketEntry> {

    private final EdsInstanceService edsInstanceService;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final EdsIdentityFacade edsIdentityFacade;
    private final AwsIamPolicyRepo awsIamPolicyRepo;
    private final AwsIamUserRepo awsIamUserRepo;

    public AwsIamPolicyPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                     WorkOrderTicketService workOrderTicketService,
                                                     WorkOrderService workOrderService,

                                                     EdsInstanceService edsInstanceService,
                                                     EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                     EdsIdentityFacade edsIdentityFacade,
                                                     AwsIamPolicyRepo awsIamPolicyRepo, AwsIamUserRepo awsIamUserRepo) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
        this.edsIdentityFacade = edsIdentityFacade;
        this.awsIamPolicyRepo = awsIamPolicyRepo;
        this.awsIamUserRepo = awsIamUserRepo;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return MarkdownUtils.createTableHeader(TableHeaderConstants.AWS_IAM_POLICY_PERMISSION);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                AwsModel.AwsPolicy awsPolicy) throws WorkOrderTicketException {
        EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, Policy> policyHolder = (EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, Policy>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.AWS_IAM_POLICY.name());
        EdsAwsConfigModel.Aws aws = policyHolder.getInstance()
                .getEdsConfigModel();
        String iamUsername = awsPolicy.getCloudAccount()
                .getUsername();
        try {
            String policyARN = awsPolicy.getAsset()
                    .getAssetKey();
            boolean alreadyAttached = awsIamPolicyRepo.listUserPolicies(aws, iamUsername)
                    .stream()
                    .anyMatch(e -> e.getPolicyArn()
                            .equals(policyARN));
            if (!alreadyAttached) {
                awsIamPolicyRepo.attachUserPolicy(aws, iamUsername, policyARN);
            }
            // 在最后一条entry执行同步资产
            if (isLastEntry(entry)) {
                User iamUser = awsIamUserRepo.getUser(aws, iamUsername);
                EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, User> iamUserHolder = (EdsInstanceProviderHolder<EdsAwsConfigModel.Aws, User>) edsInstanceProviderHolderBuilder.newHolder(
                        entry.getInstanceId(), EdsAssetTypeEnum.AWS_IAM_USER.name());
                iamUserHolder.importAsset(iamUser);
            }
        } catch (Exception e) {
            WorkOrderTicketException.runtime(e.getMessage());
        }
    }

    @Override
    protected WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddAwsIamPolicyPermissionTicketEntry param) {
        int instanceId = Optional.of(param)
                .map(WorkOrderTicketParam.AddAwsIamPolicyPermissionTicketEntry::getDetail)
                .map(AwsModel.AwsPolicy::getAsset)
                .map(EdsAssetVO.Asset::getInstanceId)
                .orElseThrow(() -> new WorkOrderTicketException("AWS IAM policy asset is null"));
        EdsIdentityVO.CloudAccount cloudAccount = getAndVerifyCloudAccount(instanceId);
        return AwsIamPolicyPermissionTicketEntryBuilder.newBuilder()
                .withParam(param)
                .withCloudAccount(cloudAccount)
                .buildEntry();
    }

    private EdsIdentityVO.CloudAccount getAndVerifyCloudAccount(int instanceId) {
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
                .filter(account -> account.getInstance()
                        .getId()
                        .equals(instanceId))
                .findFirst()
                .orElseThrow(() -> new WorkOrderTicketException(
                        "Your AWS IAM account does not exist, please apply for an account first."));
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        AwsModel.AwsPolicy awsPolicy = loadAs(entry);
        EdsAssetVO.Asset policy = awsPolicy.getAsset();
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        return MarkdownUtils.createTableRow(
                instance.getInstanceName(), awsPolicy.getCloudAccount()
                        .getAccountLogin()
                        .getLoginUsername(), policy.getName(), policy.getAssetKey()
        );
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("IAM Policy")
                .build();
    }

}
