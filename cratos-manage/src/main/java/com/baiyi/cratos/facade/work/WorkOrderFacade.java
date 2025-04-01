package com.baiyi.cratos.facade.work;

import com.baiyi.cratos.domain.param.http.work.WorkOrderParam;
import com.baiyi.cratos.domain.view.work.WorkOrderVO;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 14:07
 * &#064;Version 1.0
 */
public interface WorkOrderFacade {

    WorkOrderVO.Menu getWorkOrderMenu();

    void updateWorkOrder(WorkOrderParam.UpdateWorkOrder updateWorkOrder);

}
