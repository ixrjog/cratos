package com.baiyi.cratos.workorder.entry.impl.revoke;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.facade.BusinessTagFacade;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.user.UserVO;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.entry.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/15 11:18
 * &#064;Version 1.0
 */
@Slf4j
@Component
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
@WorkOrderKey(key = WorkOrderKeys.REVOKE_USER_PERMISSION)
public class RevokeUserEdsAccountPermissionTicketEntryProvider extends BaseTicketEntryProvider<UserVO.User, WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry> {

    public RevokeUserEdsAccountPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                   WorkOrderTicketService workOrderTicketService,
                                                   WorkOrderService workOrderService, BusinessTagFacade businessTagFacade) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
    }

    private static final String TABLE_TITLE = """
            | Instance Name | Instance Type | Account Type | Account Name |
            | --- | --- | --- | --- |
            """;

    private static final String ROW_TPL = "| {} | {} | {} | {} |";

    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                UserVO.User user) throws WorkOrderTicketException {

    }

    @Override
    protected WorkOrderTicketEntry paramToEntry(
            WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry addRevokeUserEdsAccountPermissionTicketEntry) {
        // RevokeUserEdsAccountPermissionTicketEntryBuilder

//        return RevokeUserPermissionTicketEntryBuilder.newBuilder()
//                .withParam(addRevokeUserEdsAccountPermissionTicketEntry)
//                .buildEntry();
        return null;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return TABLE_TITLE;
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
//        UserVO.User user = loadAs(entry);
//        BusinessParam.GetByBusiness getByBusiness = BusinessParam.GetByBusiness.builder()
//                .businessType(BusinessTypeEnum.USER.name())
//                .businessId(user.getId())
//                .build();
//        List<BusinessTagVO.BusinessTag> businessTags = businessTagFacade.getBusinessTagByBusiness(getByBusiness);
//        String tags = CollectionUtils.isEmpty(businessTags) ? "-" : businessTags.stream()
//                .map(e -> e.getTag()
//                        .getTagKey())
//                .collect(Collectors.joining(","));
//        return StringFormatter.arrayFormat(ROW_TPL, user.getUsername(), user.getName(), user.getDisplayName(),
//                user.getEmail(), tags);
        return null;
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return TicketEntryModel.EntryDesc.builder()
                .name(entry.getName())
                .desc("User")
                .build();
    }

    @Override
    public WorkOrderTicketEntry addEntry(WorkOrderTicketParam.AddRevokeUserEdsAccountPermissionTicketEntry param) {
        boolean userEntryExists = queryTicketEntries(param.getTicketId()).stream()
                .anyMatch(entry -> BusinessTypeEnum.USER.name()
                        .equals(entry.getBusinessType()));
        if (userEntryExists) {
            WorkOrderTicketException.runtime("Only one user information can be inserted.");
        }
        WorkOrderTicketEntry entry = super.addEntry(param);
       // addUserAccountAssets(param);
        return entry;
    }


}
