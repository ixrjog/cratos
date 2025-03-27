package com.baiyi.cratos.workorder.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 11:41
 * &#064;Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AutoAssemblyStateProcessorConfig implements InitializingBean {

    private final StateProcessorAssemblyWorkers autoConfigurationTicketStateProcessor;

    @Override
    public void afterPropertiesSet() throws Exception {
        autoConfigurationTicketStateProcessor.config();
    }

}
