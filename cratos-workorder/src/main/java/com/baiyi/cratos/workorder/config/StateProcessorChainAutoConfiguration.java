package com.baiyi.cratos.workorder.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Auto-configuration for state processor chain assembly.
 * Automatically assembles the state processor chain using annotation-driven discovery
 * when the application context is initialized.
 * 
 * @author baiyi
 * @date 2025/3/21 11:41
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class StateProcessorChainAutoConfiguration implements InitializingBean {

    private final StateProcessorChainAssembler stateProcessorChainAssembler;

    /**
     * Automatically assembles the state processor chain after all beans are initialized.
     * Uses the default configuration (CREATE -> END) with annotation-driven discovery.
     * 
     * @throws Exception if chain assembly fails
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        stateProcessorChainAssembler.config();
    }

}
