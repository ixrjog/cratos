package com.baiyi.cratos.facade.work.entry;

import com.baiyi.cratos.common.exception.WorkOrderTicketException;
import com.baiyi.cratos.domain.YamlUtil;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.util.Generics;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 13:58
 * &#064;Version 1.0
 */
@RequiredArgsConstructor
public abstract class BaseTicketEntryProvider<Detail, EntryParam extends WorkOrderTicketParam.TicketEntry> implements TicketEntryProvider<EntryParam>, InitializingBean {

    private final WorkOrderTicketEntryService workOrderTicketEntryService;
    private final WorkOrderTicketService workOrderTicketService;

    @Override
    public WorkOrderTicketEntry addEntry(EntryParam param) {
        WorkOrderTicketEntry entry = paramToEntry(param);
        if (existsEntry(entry)) {
            WorkOrderTicketException.runtime("Repeat adding entries.");
        }
        workOrderTicketEntryService.add(entry);
        return entry;

    }

    @Override
    public void processEntry(WorkOrderTicketEntry entry) {
        Detail detail = loadAs(entry);
        WorkOrderTicket ticket = workOrderTicketService.getById(entry.getTicketId());
        processEntry(ticket, entry, detail);
    }

    protected abstract void processEntry(WorkOrderTicket workOrderTicket, WorkOrderTicketEntry entry, Detail detail);

    @SuppressWarnings("unchecked")
    protected Detail loadAs(WorkOrderTicketEntry entry) {
        // Get the entity type of generic `D`
        Class<Detail> clazz = Generics.find(this.getClass(), BaseTicketEntryProvider.class, 0);
        if (!StringUtils.hasText(entry.getContent())) {
            WorkOrderTicketException.runtime("Ticket entry content is empty.");
        }
        return YamlUtil.loadAs(entry.getContent(), clazz);
    }

    protected abstract WorkOrderTicketEntry paramToEntry(EntryParam param);

    private boolean existsEntry(WorkOrderTicketEntry entry) {
        return workOrderTicketEntryService.getByUniqueKey(entry) != null;
    }

    @Override
    public void afterPropertiesSet() {
        TicketEntryProviderFactory.register(this);
    }

}
