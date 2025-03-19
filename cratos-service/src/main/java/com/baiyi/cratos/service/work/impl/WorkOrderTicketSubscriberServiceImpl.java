package com.baiyi.cratos.service.work.impl;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderTicketSubscriber;
import com.baiyi.cratos.mapper.WorkOrderTicketSubscriberMapper;
import com.baiyi.cratos.service.work.WorkOrderTicketSubscriberService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/18 16:03
 * &#064;Version 1.0
 */
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.WORKORDER_TICKET_SUBSCRIBER)
public class WorkOrderTicketSubscriberServiceImpl implements WorkOrderTicketSubscriberService {

    private final WorkOrderTicketSubscriberMapper workOrderTicketSubscriberMapper;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:WORKORDER_TICKET_SUBSCRIBER:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public WorkOrderTicketSubscriber getByUniqueKey(@NonNull WorkOrderTicketSubscriber record) {
        Example example = new Example(WorkOrderTicketSubscriber.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ticketId", record.getTicketId())
                .andEqualTo("username", record.getUsername())
                .andEqualTo("subscribeStatus", record.getSubscribeStatus());
        return workOrderTicketSubscriberMapper.selectOneByExample(example);
    }

}
