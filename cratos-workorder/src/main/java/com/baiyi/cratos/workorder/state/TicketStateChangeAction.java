package com.baiyi.cratos.workorder.state;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 10:44
 * &#064;Version 1.0
 */
public enum TicketStateChangeAction {
    /**
     * 创建工单
     */
    CREATE,
    /**
     * 提交工单
     */
    SUBMIT,
    /**
     * 审批同意
     */
    APPROVAL_APPROVED,
    /**
     * 审批拒绝
     */
    APPROVAL_REJECTED,


    DO_NEXT

}
