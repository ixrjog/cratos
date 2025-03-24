package com.baiyi.cratos.service.work;

import com.baiyi.cratos.domain.generator.WorkOrderTicketNode;
import com.baiyi.cratos.mapper.WorkOrderTicketNodeMapper;
import com.baiyi.cratos.service.base.BaseUniqueKeyService;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/18 10:16
 * &#064;Version 1.0
 */
public interface WorkOrderTicketNodeService extends BaseUniqueKeyService<WorkOrderTicketNode, WorkOrderTicketNodeMapper> {

    List<WorkOrderTicketNode> queryByTicketId(int ticketId);

    WorkOrderTicketNode getRootNode(int ticketId);

    WorkOrderTicketNode getByTicketParentId(int ticketId, int parentId);

}
