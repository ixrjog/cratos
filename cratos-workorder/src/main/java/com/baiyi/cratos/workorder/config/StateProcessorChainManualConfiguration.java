package com.baiyi.cratos.workorder.config;

import com.baiyi.cratos.workorder.state.machine.factory.TicketInStateProcessorFactory;
import com.baiyi.cratos.workorder.state.machine.impl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Manual configuration for state processor chain assembly.
 * Provides an alternative approach to chain assembly using explicit processor linking.
 * This configuration is currently disabled by default (doAssembly = false).
 * 
 * @author baiyi
 * @date 2025/3/27 09:47
 * @version 1.0
 */
@SuppressWarnings("unchecked")
@Component
@RequiredArgsConstructor
public class StateProcessorChainManualConfiguration implements InitializingBean {

    /**
     * Flag to control whether manual assembly should be performed.
     * Set to false to disable manual assembly in favor of auto-configuration.
     */
    private static final boolean ENABLE_MANUAL_ASSEMBLY = false;

    private final TicketCreateStateProcessor ticketCreateStateProcessor;
    private final TicketNewStateProcessor ticketNewStateProcessor;
    private final TicketSubmittedStateProcessor ticketSubmittedStateProcessor;
    private final TicketInApprovalStateProcessor ticketInApprovalStateProcessor;
    private final TicketApprovalCompletedStateProcessor ticketApprovalCompletedStateProcessor;
    private final TicketInProgressStateProcessor ticketInProgressStateProcessor;
    private final TicketProcessingCompletedStateProcessor ticketProcessingCompletedStateProcessor;
    private final TicketCompletedStateProcessor ticketCompletedStateProcessor;

    /**
     * Manually assembles the state processor chain by explicitly linking each processor.
     * This method is currently disabled and serves as an alternative implementation
     * for scenarios where annotation-driven assembly is not suitable.
     * 
     * @throws Exception if chain assembly fails
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if (!ENABLE_MANUAL_ASSEMBLY) {
            return;
        }
        
        // Build the processor chain: CREATE -> NEW -> SUBMITTED -> IN_APPROVAL -> 
        // APPROVAL_COMPLETED -> IN_PROGRESS -> PROCESSING_COMPLETED -> COMPLETED
        ticketCreateStateProcessor.setTarget(ticketNewStateProcessor)
                .setTarget(ticketSubmittedStateProcessor)
                .setTarget(ticketInApprovalStateProcessor)
                .setTarget(ticketApprovalCompletedStateProcessor)
                .setTarget(ticketInProgressStateProcessor)
                .setTarget(ticketProcessingCompletedStateProcessor)
                .setTarget(ticketCompletedStateProcessor);
        
        // Register the chain starting point
        TicketInStateProcessorFactory.setStateProcessor(ticketCreateStateProcessor);
    }

}
