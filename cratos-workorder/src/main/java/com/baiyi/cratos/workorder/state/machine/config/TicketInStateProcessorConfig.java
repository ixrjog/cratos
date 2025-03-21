package com.baiyi.cratos.workorder.state.machine.config;

import com.baiyi.cratos.workorder.state.machine.factory.TicketInStateProcessorFactory;
import com.baiyi.cratos.workorder.state.machine.impl.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/21 11:41
 * &#064;Version 1.0
 */
@SuppressWarnings("unchecked")
@Component
@RequiredArgsConstructor
public class TicketInStateProcessorConfig implements InitializingBean {

    private final TicketNewStateProcessor ticketNewStateProcessor;
    private final TicketSubmittedStateProcessor ticketSubmittedStateProcessor;
    private final TicketInApprovalStateProcessor ticketInApprovalStateProcessor;
    private final TicketInApprovalCompletedStateProcessor ticketInApprovalCompletedStateProcessor;
    private final TicketInProgressStateProcessor ticketInProgressStateProcessor;
    private final TicketProcessingCompletedStateProcessor ticketProcessingCompletedStateProcessor;
    private final TicketCompletedStateProcessor ticketCompletedStateProcessor;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 状态链
        ticketNewStateProcessor.setTarget(ticketSubmittedStateProcessor)
                .setTarget(ticketInApprovalStateProcessor)
                .setTarget(ticketInApprovalCompletedStateProcessor)
                .setTarget(ticketInProgressStateProcessor)
                .setTarget(ticketProcessingCompletedStateProcessor)
                .setTarget(ticketCompletedStateProcessor);
        TicketInStateProcessorFactory.setContext(ticketNewStateProcessor);
    }

}
