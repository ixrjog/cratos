package com.baiyi.cratos.service.work.impl;

import com.baiyi.cratos.domain.DataTable;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.mapper.WorkOrderTicketMapper;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 17:45
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.WORKORDER_TICKET)
public class WorkOrderTicketServiceImpl implements WorkOrderTicketService {

    private final WorkOrderTicketMapper workOrderTicketMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:WORKORDER_TICKET:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public DataTable<WorkOrderTicket> queryPageByParam(WorkOrderTicketParam.MyTicketPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<WorkOrderTicket> data = workOrderTicketMapper.queryPageByParam(pageQuery);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public WorkOrderTicket getByUniqueKey(@NonNull WorkOrderTicket record) {
        Example example = new Example(WorkOrderTicket.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ticketNo", record.getTicketNo());
        return workOrderTicketMapper.selectOneByExample(example);
    }

}
