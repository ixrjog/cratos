package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.AliyunDataWorksModel;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsInstanceVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/6 15:50
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddAliyunDataWorksInstanceTicketEntryBuilder {

    private WorkOrderTicketParam.AddAliyunDataWorksInstanceTicketEntry param;
    private String username;

    public static AddAliyunDataWorksInstanceTicketEntryBuilder newBuilder() {
        return new AddAliyunDataWorksInstanceTicketEntryBuilder();
    }

    public AddAliyunDataWorksInstanceTicketEntryBuilder withParam(
            WorkOrderTicketParam.AddAliyunDataWorksInstanceTicketEntry param) {
        this.param = param;
        return this;
    }

    public AddAliyunDataWorksInstanceTicketEntryBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    private String toDataWorksAkAccount() {
        return StringFormatter.arrayFormat("ak-dataworks-{}", username.replace(".", "")
                .toLowerCase());
    }

    public WorkOrderTicketEntry buildEntry() {
        EdsInstanceVO.EdsInstance instance = param.getDetail()
                .getEdsInstance();
        AliyunDataWorksModel.AliyunAccount aliyunAccount = param.getDetail();
        String akAccount = toDataWorksAkAccount();
        aliyunAccount.setAccount(akAccount);
        aliyunAccount.setUsername(username);
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(akAccount)
                .displayName(akAccount)
                .instanceId(instance.getId())
                .businessType(param.getBusinessType())
                .businessId(instance.getId())
                .completed(false)
                .entryKey(StringFormatter.arrayFormat("instanceId:{}:username:{}", instance.getId(), username))
                .valid(true)
                .content(YamlUtils.dump(param.getDetail()))
                .build();
    }

}
