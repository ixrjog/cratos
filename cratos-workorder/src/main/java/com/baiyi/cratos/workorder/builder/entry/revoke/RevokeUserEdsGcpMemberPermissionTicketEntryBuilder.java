package com.baiyi.cratos.workorder.builder.entry.revoke;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/16 13:58
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RevokeUserEdsGcpMemberPermissionTicketEntryBuilder {

    private WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry param;

    public static RevokeUserEdsGcpMemberPermissionTicketEntryBuilder newBuilder() {
        return new RevokeUserEdsGcpMemberPermissionTicketEntryBuilder();
    }

    public RevokeUserEdsGcpMemberPermissionTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry param) {
        this.param = param;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        EdsAssetVO.Asset asset = param.getDetail();
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(asset.getName())
                .displayName(asset.getName())
                .instanceId(asset.getInstanceId())
                .businessType(param.getBusinessType())
                .businessId(asset.getBusinessId())
                .subType(asset.getAssetType())
                .completed(false)
                .entryKey(StringFormatter.arrayFormat("instanceId={}:assetId={}", asset.getInstanceId(), asset.getId()))
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
