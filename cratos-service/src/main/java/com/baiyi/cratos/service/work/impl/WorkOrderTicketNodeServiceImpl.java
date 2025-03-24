package com.baiyi.cratos.service.work.impl;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderTicketNode;
import com.baiyi.cratos.mapper.WorkOrderTicketNodeMapper;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

import static com.baiyi.cratos.common.configuration.CachingConfiguration.RepositoryName.LONG_TERM;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/18 10:16
 * &#064;Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.WORKORDER_TICKET_NODE)
public class WorkOrderTicketNodeServiceImpl implements WorkOrderTicketNodeService {

    private final WorkOrderTicketNodeMapper workOrderTicketNodeMapper;
    public static final int ROOT_PARENT_ID = 0;

    @Override
    @CacheEvict(cacheNames = LONG_TERM, key = "'DOMAIN:WORKORDER_TICKET_NODE:ID:' + #id")
    public void clearCacheById(int id) {
    }

    @Override
    public WorkOrderTicketNode getByUniqueKey(@NonNull WorkOrderTicketNode record) {
        Example example = new Example(WorkOrderTicketNode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ticketId", record.getTicketId())
                .andEqualTo("nodeName", record.getNodeName())
                .andEqualTo("username", record.getUsername());
        return workOrderTicketNodeMapper.selectOneByExample(example);
    }

    @Override
    public List<WorkOrderTicketNode> queryByTicketId(int ticketId) {
        Example example = new Example(WorkOrderTicketNode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ticketId", ticketId);
        return workOrderTicketNodeMapper.selectByExample(example);
    }

    @Override
    public WorkOrderTicketNode getByTicketParentId(int ticketId, int parentId) {
        Example example = new Example(WorkOrderTicketNode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ticketId", ticketId)
                .andEqualTo("parentId", parentId);
        return workOrderTicketNodeMapper.selectOneByExample(example);
    }

    @Override
    public WorkOrderTicketNode getRootNode(int ticketId) {
        Example example = new Example(WorkOrderTicketNode.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("ticketId", ticketId)
                .andEqualTo("parentId", ROOT_PARENT_ID);
        return workOrderTicketNodeMapper.selectOneByExample(example);
    }

}
