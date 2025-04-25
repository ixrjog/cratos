package com.baiyi.cratos.workorder.entry.base;

import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.Generics;
import com.baiyi.cratos.exception.DaoServiceException;
import com.baiyi.cratos.service.work.WorkOrderService;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.workorder.entry.TicketEntryProvider;
import com.baiyi.cratos.workorder.entry.TicketEntryProviderFactory;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import com.baiyi.cratos.workorder.util.InvokeEntryResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 13:58
 * &#064;Version 1.0
 */
@RequiredArgsConstructor
public abstract class BaseTicketEntryProvider<Detail, EntryParam extends WorkOrderTicketParam.TicketEntry> implements TicketEntryProvider<Detail, EntryParam>, InitializingBean {

    private final WorkOrderTicketEntryService workOrderTicketEntryService;
    private final WorkOrderTicketService workOrderTicketService;
    private final WorkOrderService workOrderService;

    @Override
    public WorkOrderTicketEntry addEntry(EntryParam param) {
        WorkOrderTicketEntry entry = paramToEntry(param);
        try {
            workOrderTicketEntryService.add(entry);
            return entry;
        } catch (DaoServiceException daoServiceException) {
            throw new WorkOrderTicketException("Repeat adding entries.");
        }
    }

    @Override
    public void processEntry(WorkOrderTicketEntry entry) {
        Detail detail = loadAs(entry);
        WorkOrderTicket ticket = workOrderTicketService.getById(entry.getTicketId());
        processEntry(ticket, entry, detail);
        try {
            processEntry(ticket, entry, detail);
            InvokeEntryResult.success(entry);
        } catch (Exception e) {
            InvokeEntryResult.failed(entry, e.getMessage());
        }
        saveEntry(entry);
    }

    protected void saveEntry(WorkOrderTicketEntry entry) {
        workOrderTicketEntryService.updateByPrimaryKey(entry);
    }

    protected abstract void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry,
                                         Detail detail) throws WorkOrderTicketException;

    protected WorkOrder getWorkOrder(WorkOrderTicketEntry entry) {
        return workOrderService.getById(workOrderTicketService.getById(entry.getTicketId())
                .getWorkOrderId());
    }

    protected List<WorkOrderTicketEntry> queryTicketEntries(int ticketId) {
        return workOrderTicketEntryService.queryTicketEntries(ticketId);
    }

    @SuppressWarnings("unchecked")
    public Detail loadAs(WorkOrderTicketEntry entry) {
        // Get the entity type of generic `D`
        Class<Detail> clazz = Generics.find(this.getClass(), BaseTicketEntryProvider.class, 0);
        if (!StringUtils.hasText(entry.getContent())) {
            WorkOrderTicketException.runtime("Ticket entry content is empty.");
        }
        return YamlUtils.loadAs(entry.getContent(), clazz);
    }

    protected abstract WorkOrderTicketEntry paramToEntry(EntryParam param);

    protected boolean existsEntry(WorkOrderTicketEntry entry) {
        return workOrderTicketEntryService.getByUniqueKey(entry) != null;
    }

    @Override
    public void afterPropertiesSet() {
        TicketEntryProviderFactory.register(this);
    }

}
