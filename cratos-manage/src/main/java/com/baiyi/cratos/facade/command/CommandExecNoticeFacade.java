package com.baiyi.cratos.facade.command;

import com.baiyi.cratos.domain.generator.CommandExecApproval;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/2/24 15:59
 * &#064;Version 1.0
 */
public interface CommandExecNoticeFacade {

    /**
     * 发送审批通知
     * @param commandExecApproval
     */
    void sendApprovalNotice(CommandExecApproval commandExecApproval);

    /**
     * 发送审批结果通知
     * @param commandExecApproval
     */
    void sendApprovalResultNotice(CommandExecApproval commandExecApproval);

}
