package com.baiyi.cratos.workorder.enums;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/20 16:49
 * &#064;Version 1.0
 */
public enum TicketState {

    CREATE,
    /**
     * 新建
     */
    NEW,
    /**
     * 已提交
     */
    SUBMITTED,
    /**
     * 审批中
     */
    IN_APPROVAL,
    /**
     * 审批完成
     */
    APPROVAL_COMPLETED,
    /**
     * 处理中
     */
    IN_PROGRESS,
    /**
     * 处理完成
     */
    PROCESSING_COMPLETED,
    /**
     * 完成
     */
    COMPLETED,

    END

}
