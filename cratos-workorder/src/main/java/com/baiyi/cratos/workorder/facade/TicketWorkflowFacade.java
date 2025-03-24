package com.baiyi.cratos.workorder.facade;

import com.baiyi.cratos.domain.generator.WorkOrder;
import com.baiyi.cratos.domain.view.user.UserVO;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/24 11:43
 * &#064;Version 1.0
 */
public interface TicketWorkflowFacade {

    List<UserVO.User> querySelectableUsersByTags(List<String> tags);

    List<UserVO.User> queryNodeApprovalUsers(WorkOrder workOrder, String nodeName);

    boolean isApprover(WorkOrder workOrder, String nodeName, String username);

}
