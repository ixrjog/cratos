package com.baiyi.cratos.facade.impl;

import com.baiyi.cratos.facade.InspectionNotificationFacade;
import com.baiyi.cratos.facade.inspection.CertificateInspection;
import com.baiyi.cratos.facade.inspection.DomainInspection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/8 上午10:05
 * &#064;Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InspectionNotificationFacadeImpl implements InspectionNotificationFacade {

    private final DomainInspection domainInspection;

    private final CertificateInspection certificateInspection;

    @Override
    public void doTask() {
        try {
            domainInspection.inspectionTask();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        try {
            certificateInspection.inspectionTask();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
