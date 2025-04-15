package com.baiyi.cratos.workorder.entry.impl;

import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.business.BusinessParam;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.tag.BusinessTagVO;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.builder.entry.RevokeUserPermissionTicketEntryBuilder;
import com.baiyi.cratos.workorder.entry.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/14 10:06
 * &#064;Version 1.0
 */
@Slf4j
@Component
@BusinessType(type = BusinessTypeEnum.USER)
@WorkOrderKey(key = WorkOrderKeys.REVOKE_USER_PERMISSION)
public class RevokeUserPermissionTicketEntryProvider extends BaseTicketEntryProvider<UserVO.User, WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry> {

    private final BusinessTagFacade businessTagFacade;

    public RevokeUserPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                   WorkOrderTicketService workOrderTicketService,
                                                   WorkOrderService workOrderService, BusinessTagFacade businessTagFacade) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
        this.businessTagFacade = businessTagFacade;
    }

    private static final String TABLE_TITLE = """
            | Username | Name | DisplayName | Email | Tags |
            | --- | --- | --- | --- | --- |
            """;

    private static final String ROW_TPL = "| {} | {} | {} | {} | {} |";

    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                UserVO.User user) throws WorkOrderTicketException {

    }

    @Override
    protected WorkOrderTicketEntry paramToEntry(
            WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry addRevokeUserTicketEntry) {
        return RevokeUserPermissionTicketEntryBuilder.newBuilder()
                .withParam(addRevokeUserTicketEntry)
                .buildEntry();
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return TABLE_TITLE;
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        UserVO.User user = loadAs(entry);
        BusinessParam.GetByBusiness getByBusiness = BusinessParam.GetByBusiness.builder()
                .businessType(BusinessTypeEnum.USER.name())
                .businessId(user.getId())
                .build();
        List<BusinessTagVO.BusinessTag> businessTags = businessTagFacade.getBusinessTagByBusiness(getByBusiness);
        String tags = CollectionUtils.isEmpty(businessTags) ? "-" : businessTags.stream()
                .map(e -> e.getTag()
                        .getTagKey())
                .collect(Collectors.joining(","));
        return StringFormatter.arrayFormat(ROW_TPL, user.getUsername(), user.getName(), user.getDisplayName(),
                user.getEmail(), tags);
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .desc("User")
                .build();
    }

    @Override
    public WorkOrderTicketEntry addEntry(WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry param) {
        boolean userEntryExists = queryTicketEntries(param.getTicketId()).stream()
                .anyMatch(entry -> BusinessTypeEnum.USER.name()
                        .equals(entry.getBusinessType()));
        if (userEntryExists) {
            WorkOrderTicketException.runtime("Only one user information can be inserted.");
        }
        WorkOrderTicketEntry entry = super.addEntry(param);
        addUserAccountAssets(param);
        return entry;
    }

    private void addUserAccountAssets(WorkOrderTicketParam.AddRevokeUserPermissionTicketEntry param) {

    }

}
