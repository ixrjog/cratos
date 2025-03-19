package com.baiyi.cratos.facade.work;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.param.http.work.WorkOrderTicketParam;
import com.baiyi.cratos.domain.view.work.WorkOrderVO;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/17 15:00
 * &#064;Version 1.0
 */
public class WorkOrderFacadeTest extends BaseUnit {

    @Resource
    public WorkOrderFacade workOrderFacade;

    @Test
    void test1() {
        WorkOrderVO.Menu menu = workOrderFacade.getWorkOrderMenu();
        System.out.println(menu);
    }

    @Test
    void test2() {
        SessionUtils.setUsername("baiyi");
        WorkOrderTicketParam.CreateTicket createTicket =  WorkOrderTicketParam.CreateTicket.builder()
                .workOrderKey("APPLICATION_PERMISSION")
                .build();
        workOrderFacade.createTicket(createTicket);
    }

}
