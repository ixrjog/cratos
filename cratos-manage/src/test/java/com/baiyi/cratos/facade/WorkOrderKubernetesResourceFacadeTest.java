package com.baiyi.cratos.facade;

import com.baiyi.cratos.BaseUnit;
import com.baiyi.cratos.domain.YamlUtils;
import com.baiyi.cratos.domain.generator.WorkOrderTicketEntry;
import com.baiyi.cratos.domain.model.ApplicationModel;
import com.baiyi.cratos.service.work.WorkOrderTicketEntryService;
import com.baiyi.cratos.workorder.facade.WorkOrderKubernetesResourceFacade;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/7/2 10:31
 * &#064;Version 1.0
 */
public class WorkOrderKubernetesResourceFacadeTest extends BaseUnit {

    @Resource
    private WorkOrderTicketEntryService workOrderTicketEntryService;

    @Resource
    private WorkOrderKubernetesResourceFacade workOrderKubernetesResourceFacade;

    @Test
    void test() {
        WorkOrderTicketEntry ticketEntry = workOrderTicketEntryService.getById(1653);
        ApplicationModel.CreateFrontEndApplication createFrontEndApplication = YamlUtils.loadAs(
                ticketEntry.getContent(), ApplicationModel.CreateFrontEndApplication.class);
        workOrderKubernetesResourceFacade.createKubernetesResource(createFrontEndApplication);
    }

}
