package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.GcpModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.googlecloud.enums.GcpIAMMemberType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/24 13:49
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GcpIamRoleTicketEntryBuilder {

    private WorkOrderTicketParam.AddGcpIamRoleTicketEntry param;
    // private String username;
    private Integer instanceId;
    // GCP
    private String member;
    private String projectName;
    private String projectId;
    private GcpModel.IamRole iamRole;
    private String username;

    public static GcpIamRoleTicketEntryBuilder newBuilder() {
        return new GcpIamRoleTicketEntryBuilder();
    }

    public GcpIamRoleTicketEntryBuilder withParam(WorkOrderTicketParam.AddGcpIamRoleTicketEntry param) {
        this.param = param;
        return this;
    }

    public GcpIamRoleTicketEntryBuilder withInstanceId(Integer instanceId) {
        this.instanceId = instanceId;
        return this;
    }

    public GcpIamRoleTicketEntryBuilder withMember(String member) {
        this.member = member;
        return this;
    }

    public GcpIamRoleTicketEntryBuilder withProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public GcpIamRoleTicketEntryBuilder withProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public GcpIamRoleTicketEntryBuilder withIamRole(GcpModel.IamRole iamRole) {
        this.iamRole = iamRole;
        return this;
    }

    public GcpIamRoleTicketEntryBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        GcpModel.MemberRole memberRole = GcpModel.MemberRole.builder()
                .member(this.member)
                .projectId(this.projectId)
                .projectName(this.projectName)
                .type(GcpIAMMemberType.USER.getKey())
                .username(this.username)
                .role(this.iamRole)
                .build();
        int businessId = Optional.of(param)
                .map(WorkOrderTicketParam.AddGcpIamRoleTicketEntry::getDetail)
                .map(GcpModel.MemberRole::getAsset)
                .map(EdsAssetVO.Asset::getId)
                .orElse(0);
        param.setDetail(memberRole);
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(this.iamRole.getName())
                .displayName(this.iamRole.getTitle())
                .instanceId(this.instanceId)
                .businessType(param.getBusinessType())
                .subType(EdsAssetTypeEnum.GCP_MEMBER.name())
                .businessId(businessId)
                .completed(false)
                .entryKey(StringFormatter.arrayFormat(
                        "instanceId:{}:member:{}:role:{}", this.instanceId, this.member,
                        this.iamRole.getName()
                ))
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
