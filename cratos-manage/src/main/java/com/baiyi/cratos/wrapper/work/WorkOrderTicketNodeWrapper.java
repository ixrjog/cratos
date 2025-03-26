package com.baiyi.cratos.wrapper.work;

import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketNode;
import com.baiyi.cratos.domain.view.work.WorkOrderTicketVO;
import com.baiyi.cratos.service.work.WorkOrderTicketNodeService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.wrapper.base.BaseDataTableConverter;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/20 16:05
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
@BusinessType(type = BusinessTypeEnum.WORKORDER_TICKET_NODE)
public class WorkOrderTicketNodeWrapper extends BaseDataTableConverter<WorkOrderTicketVO.TicketNode, WorkOrderTicketNode> implements IBusinessWrapper<WorkOrderTicketVO.HasTicketNodes, WorkOrderTicketVO.TicketNode> {

    private final WorkOrderTicketService workOrderTicketService;
    private final WorkOrderTicketNodeService workOrderTicketNodeService;

    @Override
    public void wrap(WorkOrderTicketVO.TicketNode vo) {
    }

    @Override
    public void businessWrap(WorkOrderTicketVO.HasTicketNodes hasTicketNodes) {
        if (StringUtils.hasText(hasTicketNodes.getTicketNo())) {
            return;
        }
        WorkOrderTicket ticket = workOrderTicketService.getByTicketNo(hasTicketNodes.getTicketNo());
        if (Objects.isNull(ticket)) {
            return;
        }
        Map<String, WorkOrderTicketVO.TicketNode> nodes = workOrderTicketNodeService.queryByTicketId(ticket.getId())
                .stream()
                .map(this::wrapToTarget)
                .collect(Collectors.toMap(WorkOrderTicketVO.TicketNode::getNodeName, Function.identity()));
        hasTicketNodes.setNodes(nodes);
        nodes.values()
                .stream()
                .filter(value -> value.getId()
                        .equals(ticket.getNodeId()))
                .findFirst()
                .ifPresent(value -> hasTicketNodes.setCurrentNode(value.getNodeName()));
    }

}