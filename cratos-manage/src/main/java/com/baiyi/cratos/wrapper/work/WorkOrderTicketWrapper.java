package com.baiyi.cratos.wrapper.work;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.view.work.WorkOrderTicketVO;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 17:58
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.WORKORDER_TICKET)
public class WorkOrderTicketWrapper extends BaseDataTableConverter<WorkOrderTicketVO.Ticket, WorkOrderTicket> implements IBusinessWrapper<WorkOrderTicketVO.HasTicket, WorkOrderTicketVO.Ticket> {

    private final WorkOrderTicketService workOrderTicketService;

    @Override
    public void wrap(WorkOrderTicketVO.Ticket vo) {
    }

    @Override
    public void businessWrap(WorkOrderTicketVO.HasTicket hasTicket) {
        if (StringUtils.hasText(hasTicket.getTicketId())) {
            WorkOrderTicket workOrderTicket = workOrderTicketService.getByTicketId(hasTicket.getTicketId());
            hasTicket.setTicket(wrapToTarget(workOrderTicket));
        }
    }

}