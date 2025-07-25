package com.baiyi.cratos.workorder.config;

import com.baiyi.cratos.domain.util.SpringContextUtils;
import com.baiyi.cratos.workorder.annotation.TicketStates;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.state.machine.BaseTicketStateProcessor;
import com.baiyi.cratos.workorder.state.machine.factory.TicketInStateProcessorFactory;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

/**
 * State processor chain assembler for work order ticket processing.
 * Automatically discovers and assembles state processors into a responsibility chain
 * based on @TicketStates annotations.
 *
 * @author baiyi
 * @version 1.0
 * @date 2025/3/26 16:05
 */
@Slf4j
@Component
public class StateProcessorChainAssembler {

    /**
     * Assembles the state processor chain with custom start and end states.
     * <p>
     * This method performs the following operations:
     * 1. Discovers all state processors annotated with @TicketStates
     * 2. Builds a processor map indexed by state
     * 3. Links processors in sequence from start to end state
     * 4. Registers the assembled chain with the factory
     *
     * @param startState the initial state of the processor chain
     * @param endState   the terminal state of the processor chain
     * @throws NullPointerException if a required state processor is missing or chain is incomplete
     */
    public void config(TicketState startState, TicketState endState) throws NullPointerException {
        log.info("Starting automatic assembly of work order state processor chain: {} -> {}", startState, endState);

        // Discover all beans annotated with @TicketStates
        Map<String, Object> annotatedBeans = SpringContextUtils.getContext()
                .getBeansWithAnnotation(TicketStates.class);

        // Build processor map indexed by state
        Map<TicketState, BaseTicketStateProcessor<?>> stateProcessorMap = Maps.newHashMap();
        annotatedBeans.values()
                .stream()
                .filter(bean -> bean instanceof BaseTicketStateProcessor<?>)
                .forEach(bean -> {
                    BaseTicketStateProcessor<?> processor = (BaseTicketStateProcessor<?>) bean;
                    TicketState processorState = AopUtils.getTargetClass(processor)
                            .getAnnotation(TicketStates.class)
                            .state();
                    stateProcessorMap.put(processorState, processor);
                });

        // Link processors in chain from start to end
        TicketState currentState = startState;
        int maxIterations = stateProcessorMap.size();
        int iterationCount = 0;

        while (!currentState.equals(endState) && iterationCount < maxIterations) {
            BaseTicketStateProcessor<?> currentProcessor = stateProcessorMap.get(currentState);
            if (Objects.isNull(currentProcessor)) {
                throw new NullPointerException(String.format(
                        "State processor not found for state '%s'. Please ensure all required state processors are properly configured and annotated with @TicketStates. Available states: %s",
                        currentState, stateProcessorMap.keySet()));
            }

            TicketState nextState = AopUtils.getTargetClass(currentProcessor)
                    .getAnnotation(TicketStates.class)
                    .target();

            log.info("Linking processor: {} ({} -> {})", currentProcessor.getClass()
                    .getSimpleName(), currentState.name(), nextState.name());

            currentProcessor.setTarget(stateProcessorMap.get(nextState));
            currentState = nextState;
            iterationCount++;
        }

        // Validate chain assembly completion
        if (iterationCount > maxIterations) {
            log.error(
                    "State processor chain assembly failed: Maximum iteration limit ({}) reached. This indicates a circular dependency or missing end state processor. Current state: {}, Target end state: {}",
                    maxIterations, currentState, endState);
        }

        // Register the assembled chain
        TicketInStateProcessorFactory.setStateProcessor(stateProcessorMap.get(startState));
        log.info("State processor chain assembly completed successfully");
    }

    /**
     * Assembles the state processor chain with custom start state and default end state (END).
     *
     * @param startState the initial state of the processor chain
     * @throws NullPointerException if a required state processor is missing or chain is incomplete
     */
    public void config(TicketState startState) throws NullPointerException {
        config(startState, TicketState.END);
    }

    /**
     * Assembles the state processor chain with default start state (CREATE) and end state (END).
     * This is the most commonly used configuration for standard work order processing.
     *
     * @throws NullPointerException if a required state processor is missing or chain is incomplete
     */
    public void config() throws NullPointerException {
        config(TicketState.CREATE, TicketState.END);
    }

}
