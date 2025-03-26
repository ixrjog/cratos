package com.baiyi.cratos.workorder.state.config;

import com.baiyi.cratos.domain.util.SpringContextUtil;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import com.baiyi.cratos.workorder.state.machine.factory.TicketInStateProcessorFactory;
import com.google.common.collect.Maps;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/3/26 16:05
 * &#064;Version 1.0
 */
@Component
public class AutoConfigurationTicketStateProcessor {

    /**
     * 自动装配链路
     * @param startState
     * @param endState
     */
    public void config(TicketState startState, TicketState endState) {
        Map<String, Object> annotatedBeans = SpringContextUtil.getContext()
                .getBeansWithAnnotation(TicketStates.class);
        Map<TicketState, BaseTicketStateProcessor<?>> processorMap = Maps.newHashMap();
        annotatedBeans.values()
                .stream()
                .filter(bean -> bean instanceof BaseTicketStateProcessor<?>)
                .forEach(bean -> {
                    BaseTicketStateProcessor<?> processor = (BaseTicketStateProcessor<?>) bean;
                    TicketState state = AopUtils.getTargetClass(processor)
                            .getAnnotation(TicketStates.class)
                            .state();
                    processorMap.put(state, processor);
                });
        TicketState currentState = startState;
        while (!currentState.equals(endState)) {
            BaseTicketStateProcessor<?> currentProcessor = processorMap.get(currentState);
            TicketState targetState = AopUtils.getTargetClass(currentProcessor)
                    .getAnnotation(TicketStates.class)
                    .target();
            currentProcessor.setTarget(processorMap.get(targetState));
            currentState = targetState;
        }
        TicketInStateProcessorFactory.setContext(processorMap.get(startState));
    }

    public void config(TicketState startState) {
        config(startState, TicketState.END);
    }

    public void config() {
        config(TicketState.CREATE, TicketState.END);
    }

}
