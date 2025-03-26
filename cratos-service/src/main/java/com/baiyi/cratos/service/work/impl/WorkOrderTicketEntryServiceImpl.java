package com.baiyi.cratos.service.work.impl;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.mapper.WorkOrderTicketEntryMapper;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/19 10:47
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.WORKORDER_TICKET_ENTRY)
public class WorkOrderTicketEntryServiceImpl implements WorkOrderTicketEntryService {

    private final WorkOrderTicketEntryMapper workOrderTicketEntryMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:WORKORDER_TICKET_ENTRY:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public WorkOrderTicketEntry getByUniqueKey(@NonNull WorkOrderTicketEntry record) {
        Example example = new Example(WorkOrderTicketEntry.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ticketId", record.getTicketId())
                .andEqualTo("businessType", record.getBusinessType())
                .andEqualTo("entryKey", record.getEntryKey());
        return workOrderTicketEntryMapper.selectOneByExample(example);
    }

    @Override
    public List<WorkOrderTicketEntry> queryTicketEntries(int ticketId) {
        Example example = new Example(WorkOrderTicketEntry.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ticketId", ticketId);
        return workOrderTicketEntryMapper.selectByExample(example);
    }

    @Override
    public int countByTicketId(int ticketId) {
        Example example = new Example(WorkOrderTicketEntry.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ticketId", ticketId);
        return workOrderTicketEntryMapper.selectCountByExample(example);
    }

}
