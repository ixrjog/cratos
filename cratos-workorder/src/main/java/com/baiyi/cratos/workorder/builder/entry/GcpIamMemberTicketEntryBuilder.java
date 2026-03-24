package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.GcpModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import com.baiyi.cratos.eds.core.enums.EdsAssetTypeEnum;
import com.baiyi.cratos.eds.googlecloud.enums.GcpIAMMemberType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2026/3/24 10:53
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GcpIamMemberTicketEntryBuilder {

    private static final String GCP_CONSOLE = "https://console.cloud.google.com/";

    private WorkOrderTicketParam.AddGcpIamMemberTicketEntry param;
    private String username;
    // GCP
    private String member;
    private String projectName;
    private String projectId;

    public static GcpIamMemberTicketEntryBuilder newBuilder() {
        return new GcpIamMemberTicketEntryBuilder();
    }

    public GcpIamMemberTicketEntryBuilder withParam(WorkOrderTicketParam.AddGcpIamMemberTicketEntry param) {
        this.param = param;
        return this;
    }

    public GcpIamMemberTicketEntryBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public GcpIamMemberTicketEntryBuilder withMember(String member) {
        this.member = member;
        return this;
    }

    public GcpIamMemberTicketEntryBuilder withProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public GcpIamMemberTicketEntryBuilder withProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        EdsInstanceVO.EdsInstance instance = param.getDetail()
                .getEdsInstance();
        GcpModel.IamMember iamMember = GcpModel.IamMember.builder()
                .username(this.username)
                .member(this.member)
                .projectId(this.projectId)
                .projectName(this.projectName)
                .type(GcpIAMMemberType.USER.getKey())
                .loginLink(GCP_CONSOLE)
                .build();
        param.setDetail(iamMember);
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(this.member)
                .displayName(this.username)
                .instanceId(instance.getId())
                .businessType(param.getBusinessType())
                .subType(EdsAssetTypeEnum.GCP_MEMBER.name())
                .businessId(instance.getId())
                .completed(false)
                .entryKey(StringFormatter.arrayFormat("instanceId:{}:member:{}", instance.getId(), this.member))
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
