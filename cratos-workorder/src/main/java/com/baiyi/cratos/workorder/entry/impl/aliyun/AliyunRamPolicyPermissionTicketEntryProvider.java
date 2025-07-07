package com.baiyi.cratos.workorder.entry.impl.aliyun;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.ram.model.v20150501.GetPolicyResponse;
import com.aliyuncs.ram.model.v20150501.GetUserResponse;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.EdsAssetIndex;
import com.baiyi.cratos.domain.generator.EdsInstance;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.AliyunModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRamPolicyRepo;
import com.baiyi.cratos.eds.aliyun.repo.AliyunRamUserRepo;
import com.baiyi.cratos.eds.core.config.EdsAliyunConfigModel;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolder;
import com.baiyi.cratos.eds.core.holder.EdsInstanceProviderHolderBuilder;
import com.baiyi.cratos.service.EdsAssetIndexService;
import com.baiyi.cratos.service.EdsInstanceService;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.AliyunRamPolicyPermissionTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.baiyi.cratos.eds.core.constants.EdsAssetIndexConstants.CLOUD_ACCOUNT_USERNAME;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/16 11:34
 * &#064;Version 1.0
 */
@Component
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
@WorkOrderKey(key = WorkOrderKeys.ALIYUN_RAM_POLICY_PERMISSION)
public class AliyunRamPolicyPermissionTicketEntryProvider extends BaseTicketEntryProvider<AliyunModel.AliyunPolicy, WorkOrderTicketParam.AddAliyunRamPolicyPermissionTicketEntry> {

    private final EdsInstanceService edsInstanceService;
    private final AliyunRamUserRepo aliyunRamUserRepo;
    private final AliyunRamPolicyRepo aliyunRamPolicyRepo;
    private final EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder;
    private final EdsAssetIndexService edsAssetIndexService;

    public AliyunRamPolicyPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                        WorkOrderTicketService workOrderTicketService,
                                                        WorkOrderService workOrderService,
                                                        EdsInstanceService edsInstanceService,
                                                        AliyunRamUserRepo aliyunRamUserRepo,
                                                        AliyunRamPolicyRepo aliyunRamPolicyRepo,
                                                        EdsInstanceProviderHolderBuilder edsInstanceProviderHolderBuilder,
                                                        EdsAssetIndexService edsAssetIndexService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.edsInstanceService = edsInstanceService;
        this.aliyunRamUserRepo = aliyunRamUserRepo;
        this.aliyunRamPolicyRepo = aliyunRamPolicyRepo;
        this.edsInstanceProviderHolderBuilder = edsInstanceProviderHolderBuilder;
        this.edsAssetIndexService = edsAssetIndexService;
    }

    private static final String ROW_TPL = "| {} | {} | {} | {} | {} |";

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return """
                | Aliyun Instance | RAM Login Username | Policy Name | Policy Type | Policy Desc |
                | --- | --- | --- | --- | --- |
                """;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                AliyunModel.AliyunPolicy aliyunPolicy) throws WorkOrderTicketException {
        EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetPolicyResponse.Policy> policyHolder = (EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetPolicyResponse.Policy>) edsInstanceProviderHolderBuilder.newHolder(
                entry.getInstanceId(), EdsAssetTypeEnum.ALIYUN_RAM_POLICY.name());
        EdsAliyunConfigModel.Aliyun aliyun = policyHolder.getInstance()
                .getEdsConfigModel();
        try {
            GetUserResponse.User user = aliyunRamUserRepo.getUser(aliyun, aliyunPolicy.getRamUsername());
            if (user == null) {
                WorkOrderTicketException.runtime("Aliyun RAM user {} is null", aliyunPolicy.getRamUsername());
            }
            EdsAssetVO.Asset asset = aliyunPolicy.getAsset();
            String policyName = asset.getAssetKey();
            String policyType = asset.getKind();
            boolean alreadyAttached = aliyunRamUserRepo.listUsersForPolicy(aliyun, policyName, policyType)
                    .stream()
                    .anyMatch(e -> aliyunPolicy.getRamUsername()
                            .equals(e.getUserName()));
            if (!alreadyAttached) {
                aliyunRamPolicyRepo.attachPolicyToUser(aliyun.getRegionId(), aliyun, aliyunPolicy.getRamUsername(),
                        policyName, policyType);
                // TODO 同步资产
            }
        } catch (ClientException e) {
            if (e.getMessage() != null && e.getMessage()
                    .startsWith("EntityAlreadyExists.User.Policy")) {
                return;
            }
            WorkOrderTicketException.runtime(e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected WorkOrderTicketEntry paramToEntry(WorkOrderTicketParam.AddAliyunRamPolicyPermissionTicketEntry param) {
        int instanceId = Optional.of(param)
                .map(WorkOrderTicketParam.AddAliyunRamPolicyPermissionTicketEntry::getDetail)
                .map(AliyunModel.AliyunPolicy::getAsset)
                .map(EdsAssetVO.Asset::getInstanceId)
                .orElseThrow(() -> new WorkOrderTicketException("Aliyun RAM policy asset is null"));
        EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetPolicyResponse.Policy> holder = (EdsInstanceProviderHolder<EdsAliyunConfigModel.Aliyun, GetPolicyResponse.Policy>) edsInstanceProviderHolderBuilder.newHolder(
                instanceId, EdsAssetTypeEnum.ALIYUN_RAM_POLICY.name());
        EdsAliyunConfigModel.Aliyun aliyun = holder.getInstance()
                .getEdsConfigModel();
        return AliyunRamPolicyPermissionTicketEntryBuilder.newBuilder()
                .withParam(param)
                .withUsername(SessionUtils.getUsername())
                .withAliyun(aliyun)
                .buildEntry();
    }

    @Override
    protected void verifyEntryParam(WorkOrderTicketParam.AddAliyunRamPolicyPermissionTicketEntry param,
                                    WorkOrderTicketEntry entry) {
        // 查询用户账户是否存在
        List<EdsAssetIndex> accountIndices = edsAssetIndexService.queryInstanceIndexByNameAndValue(
                entry.getInstanceId(), CLOUD_ACCOUNT_USERNAME, param.getDetail()
                        .getRamUsername());
        if (CollectionUtils.isEmpty(accountIndices)) {
            WorkOrderTicketException.runtime(
                    "Your Aliyun RAM account does not exist, please apply for an account first.");
        }
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        AliyunModel.AliyunPolicy aliyunPolicy = loadAs(entry);
        EdsAssetVO.Asset policy = aliyunPolicy.getAsset();
        EdsInstance instance = edsInstanceService.getById(entry.getInstanceId());
        return StringFormatter.arrayFormat(ROW_TPL, instance.getInstanceName(), aliyunPolicy.getRamLoginUsername(),
                policy.getAssetKey(), policy.getKind(),
                StringUtils.hasText(policy.getDescription()) ? policy.getDescription() : "--");
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .namespaces(entry.getNamespace())
                .desc("RAM Policy")
                .build();
    }

}
