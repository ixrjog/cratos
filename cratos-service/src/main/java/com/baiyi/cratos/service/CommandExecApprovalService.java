package com.baiyi.cratos.service;

import com.baiyi.cratos.domain.generator.CommandExecApproval;
import com.baiyi.cratos.mapper.CommandExecApprovalMapper;
import com.baiyi.cratos.service.base.BaseService;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/17 18:19
 * &#064;Version 1.0
 */
public interface CommandExecApprovalService extends BaseService<CommandExecApproval, CommandExecApprovalMapper> {

    CommandExecApproval queryUnapprovedRecord(int commandExecId, String username);

    boolean hasUnfinishedApprovals(int commandExecId);

    List<CommandExecApproval> queryApprovals(int commandExecId, String approvalType);

}
