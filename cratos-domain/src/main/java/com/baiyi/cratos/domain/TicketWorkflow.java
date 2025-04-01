package com.baiyi.cratos.domain;

import com.baiyi.cratos.domain.model.WorkflowModel;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/18 11:06
 * &#064;Version 1.0
 */
public class TicketWorkflow {

    public interface HasWorkflowData extends HasWorkflow {
        void setWorkflowData(WorkflowModel.Workflow workflow);
    }

    public interface HasWorkflow {
        String getWorkflow();
    }

}
