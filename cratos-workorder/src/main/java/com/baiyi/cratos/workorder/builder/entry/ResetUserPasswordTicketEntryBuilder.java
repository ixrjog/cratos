package com.baiyi.cratos.workorder.builder.entry;

import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.BeanCopierUtils;
import com.baiyi.cratos.domain.util.StringFormatter;
import com.baiyi.cratos.domain.view.user.UserVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/23 11:25
 * &#064;Version 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResetUserPasswordTicketEntryBuilder {

    private WorkOrderTicketParam.AddResetUserPasswordTicketEntry param;
    private User user;

    public static ResetUserPasswordTicketEntryBuilder newBuilder() {
        return new ResetUserPasswordTicketEntryBuilder();
    }

    public ResetUserPasswordTicketEntryBuilder withParam(WorkOrderTicketParam.AddResetUserPasswordTicketEntry param) {
        this.param = param;
        return this;
    }

    public ResetUserPasswordTicketEntryBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public WorkOrderTicketEntry buildEntry() {
        UserVO.User detail = BeanCopierUtils.copyProperties(user, UserVO.User.class);
        return WorkOrderTicketEntry.builder()
                .ticketId(param.getTicketId())
                .name(user.getUsername())
                .displayName(user.getDisplayName())
                .instanceId(0)
                .businessType(param.getBusinessType())
                .businessId(user.getId())
                .completed(false)
                .entryKey(StringFormatter.format("username:{}", user.getUsername()))
                .valid(true)
                .content(YamlUtils.dump(detail))
                .build();
    }

}
