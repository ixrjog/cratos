package com.baiyi.cratos.workorder.entry.impl.aliyun;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.eds.EdsAssetVO;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.annotation.WorkOrderKey;
import com.baiyi.cratos.workorder.entry.base.BaseTicketEntryProvider;
import com.baiyi.cratos.workorder.enums.WorkOrderKeys;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.model.TicketEntryModel;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/5/16 11:34
 * &#064;Version 1.0
 */
@BusinessType(type = BusinessTypeEnum.EDS_ASSET)
@WorkOrderKey(key = WorkOrderKeys.ALIYUN_RAM_POLICY_PERMISSION)
public class AliyunRamPolicyPermissionTicketEntryProvider extends BaseTicketEntryProvider<EdsAssetVO.Asset, WorkOrderTicketParam.AddAliyunRamPolicyPermissionTicketEntry> {

    private static final String ROW_TPL = "| {} | {} |";

    public AliyunRamPolicyPermissionTicketEntryProvider(WorkOrderTicketEntryService workOrderTicketEntryService,
                                                        WorkOrderTicketService workOrderTicketService,
                                                        WorkOrderService workOrderService) {
        super(workOrderTicketEntryService, workOrderTicketService, workOrderService);
    }

    @Override
    protected void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                EdsAssetVO.Asset asset) throws WorkOrderTicketException {

    }

    @Override
    protected WorkOrderTicketEntry paramToEntry(
            WorkOrderTicketParam.AddAliyunRamPolicyPermissionTicketEntry addAliyunRamPolicyPermissionTicketEntry) {
        return null;
    }

    @Override
    public String getTableTitle(WorkOrderTicketEntry entry) {
        return "";
    }

    @Override
    public String getEntryTableRow(WorkOrderTicketEntry entry) {
        return "";
    }

    @Override
    public TicketEntryModel.EntryDesc getEntryDesc(WorkOrderTicketEntry entry) {
        return null;
    }

}
