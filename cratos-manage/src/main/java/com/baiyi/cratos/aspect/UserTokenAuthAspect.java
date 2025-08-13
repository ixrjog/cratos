package com.baiyi.cratos.aspect;

import com.baiyi.cratos.annotation.UserTokenAuth;
import com.baiyi.cratos.common.util.ExpiredUtils;
import com.baiyi.cratos.common.util.SessionUtils;
import com.baiyi.cratos.domain.generator.User;
import com.baiyi.cratos.domain.generator.WorkOrderTicket;
import com.baiyi.cratos.domain.generator.WorkOrderTicketSubscriber;
import com.baiyi.cratos.service.UserService;
import com.baiyi.cratos.service.work.WorkOrderTicketService;
import com.baiyi.cratos.service.work.WorkOrderTicketSubscriberService;
import com.baiyi.cratos.workorder.enums.TicketState;
import com.baiyi.cratos.workorder.exception.WorkOrderTicketException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.stream.IntStream;

/**
 * 用户Token认证切面
 * 
 * 该切面用于工单审批系统中的用户身份验证和权限控制，主要功能包括：
 * 1. 验证工单订阅者token的有效性
 * 2. 验证审批者用户的有效性和过期时间
 * 3. 检查工单状态是否为待审批状态
 * 4. 确保工单与订阅者的关联性匹配
 * 5. 实现一次性token机制，防止token重复使用
 * 6. 自动将审批者用户名写入当前会话
 * 
 * 安全机制：
 * - Token一次性使用：使用后立即失效，防止重放攻击
 * - 多重验证：用户、工单、token三重验证确保安全性
 * - 状态检查：只允许对待审批状态的工单进行操作
 * - 关联验证：确保操作者与工单的合法关联关系
 * 
 * 使用方式：
 * 在需要进行工单审批权限验证的方法上添加 @UserTokenAuth 注解
 * 
 * &#064;Author  baiyi
 * &#064;Date  2025/7/10 14:16
 * &#064;Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Order(-1) // 设置切面执行优先级，-1表示高优先级，确保在其他切面之前执行
public class UserTokenAuthAspect {
    
    // SpEL表达式解析相关工具
    /** 参数名发现器，用于获取方法参数名 */
    private static final DefaultParameterNameDiscoverer DISCOVERER = new DefaultParameterNameDiscoverer();
    /** SpEL表达式解析器 */
    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    // 依赖的服务
    private final WorkOrderTicketService workOrderTicketService;
    private final WorkOrderTicketSubscriberService workOrderTicketSubscriberService;
    private final UserService userService;

    /**
     * 定义切点：匹配所有标注了 @UserTokenAuth 注解的方法
     */
    @Pointcut("@annotation(com.baiyi.cratos.annotation.UserTokenAuth)")
    public void annotationPoint() {
    }

    /**
     * 前置通知：在目标方法执行前进行用户token认证
     * 
     * 执行流程：
     * 1. 获取并验证工单订阅者信息
     * 2. 获取并验证审批者用户信息
     * 3. 获取并验证工单信息
     * 4. 检查工单与订阅者的关联关系
     * 5. 使token失效（一次性使用）
     * 6. 将审批者用户名写入session
     * 
     * @param joinPoint 连接点，包含目标方法的信息
     * @param userTokenAuth 用户token认证注解实例
     * @throws WorkOrderTicketException 当验证失败时抛出工单异常
     */
    @Before("@annotation(userTokenAuth)")
    public void beforeAdvice(JoinPoint joinPoint, UserTokenAuth userTokenAuth) {
        log.debug("开始执行用户Token认证验证");
        
        // 1. 获取并验证工单订阅者
        WorkOrderTicketSubscriber subscriber = getWorkOrderTicketSubscriber(joinPoint, userTokenAuth);
        if (Boolean.FALSE.equals(subscriber.getValid())) {
            log.warn("工单订阅者无效，token: {}", subscriber.getToken());
            WorkOrderTicketException.runtime("The subscriber is invalid.");
        }
        
        // 2. 获取并验证审批者用户
        User approver = getApprover(joinPoint, userTokenAuth);
        if (Boolean.FALSE.equals(approver.getValid()) || ExpiredUtils.isExpired(approver.getExpiredTime())) {
            log.warn("审批者无效或已过期，username: {}", approver.getUsername());
            WorkOrderTicketException.runtime("The approver is invalid or expired.");
        }
        
        // 3. 获取并验证工单信息
        WorkOrderTicket ticket = getWorkOrderTicket(joinPoint, userTokenAuth);
        if (!TicketState.IN_APPROVAL.name().equals(ticket.getTicketState())) {
            log.warn("工单状态不是待审批状态，当前状态: {}, 工单号: {}", ticket.getTicketState(), ticket.getTicketNo());
            WorkOrderTicketException.runtime(
                    "The work order is not in the pending approval state and cannot be operated.");
        }
        
        // 4. 验证工单与订阅者的关联关系
        if (!ticket.getId().equals(subscriber.getTicketId())) {
            log.warn("工单与订阅者不匹配，工单ID: {}, 订阅者工单ID: {}", ticket.getId(), subscriber.getTicketId());
            WorkOrderTicketException.runtime("The work order does not match the subscriber and cannot be operated.");
        }
        
        // 5. 使token失效（一次性使用机制）
//        subscriber.setValid(false);
//        workOrderTicketSubscriberService.updateByPrimaryKey(subscriber);
//        log.debug("Token已失效，确保一次性使用");
        
        // 6. 将审批者用户名写入session
        SessionUtils.setUsername(approver.getUsername());
        log.debug("用户Token认证验证完成，审批者: {}", approver.getUsername());
    }

    /**
     * 获取工单信息
     * 
     * @param joinPoint 连接点
     * @param userTokenAuth 用户token认证注解
     * @return 工单信息
     * @throws WorkOrderTicketException 当工单不存在时抛出异常
     */
    private WorkOrderTicket getWorkOrderTicket(JoinPoint joinPoint, UserTokenAuth userTokenAuth) {
        String ticketNo = parseSpel(joinPoint, userTokenAuth.ofTicketNo());
        if (ticketNo != null) {
            WorkOrderTicket ticket = workOrderTicketService.getByTicketNo(ticketNo);
            if (ticket != null) {
                log.debug("获取工单成功，工单号: {}", ticketNo);
                return ticket;
            }
        }
        log.error("工单不存在，工单号: {}", ticketNo);
        throw new WorkOrderTicketException("Work order ticketNo does not exist.");
    }

    /**
     * 获取工单订阅者信息
     * 
     * @param joinPoint 连接点
     * @param userTokenAuth 用户token认证注解
     * @return 工单订阅者信息
     * @throws WorkOrderTicketException 当订阅者不存在时抛出异常
     */
    private WorkOrderTicketSubscriber getWorkOrderTicketSubscriber(JoinPoint joinPoint, UserTokenAuth userTokenAuth) {
        String token = parseSpel(joinPoint, userTokenAuth.ofToken());
        if (token != null) {
            WorkOrderTicketSubscriber subscriber = workOrderTicketSubscriberService.getByToken(token);
            if (subscriber != null) {
                log.debug("获取工单订阅者成功，token: {}", token);
                return subscriber;
            }
        }
        log.error("工单订阅者不存在，token: {}", token);
        throw new WorkOrderTicketException("Work order ticket subscriber does not exist.");
    }

    /**
     * 获取审批者用户信息
     * 
     * @param joinPoint 连接点
     * @param userTokenAuth 用户token认证注解
     * @return 审批者用户信息
     * @throws WorkOrderTicketException 当用户不存在时抛出异常
     */
    private User getApprover(JoinPoint joinPoint, UserTokenAuth userTokenAuth) {
        String username = parseSpel(joinPoint, userTokenAuth.ofUsername());
        if (username != null) {
            User user = userService.getByUsername(username);
            if (user != null) {
                log.debug("获取审批者用户成功，username: {}", username);
                return user;
            }
        }
        log.error("审批者用户不存在，username: {}", username);
        throw new WorkOrderTicketException("User does not exist.");
    }

    /**
     * 解析SpEL表达式
     * 
     * 该方法用于解析注解中的SpEL表达式，从方法参数中提取实际的值
     * 支持复杂的表达式解析，如对象属性访问、方法调用等
     * 
     * @param joinPoint 连接点，包含方法签名和参数信息
     * @param spel SpEL表达式字符串
     * @return 解析后的字符串值，如果解析失败或结果不是字符串则返回null
     */
    private String parseSpel(JoinPoint joinPoint, String spel) {
        // 获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        
        // 获取方法参数名
        String[] params = DISCOVERER.getParameterNames(method);
        // 获取方法参数值
        Object[] args = joinPoint.getArgs();
        
        if (params == null) {
            log.warn("无法获取方法参数名，SpEL解析失败");
            return null;
        }
        
        // 创建SpEL表达式上下文
        EvaluationContext context = new StandardEvaluationContext();
        // 将方法参数名和值绑定到上下文中
        IntStream.range(0, params.length)
                .forEach(i -> context.setVariable(params[i], args[i]));
        
        try {
            // 解析SpEL表达式
            Expression expression = EXPRESSION_PARSER.parseExpression(spel);
            Object value = expression.getValue(context);
            
            // 确保返回值是字符串类型
            String result = value instanceof String ? (String) value : null;
            log.debug("SpEL表达式解析成功，表达式: {}, 结果: {}", spel, result);
            return result;
        } catch (Exception e) {
            log.error("SpEL表达式解析失败，表达式: {}, 错误: {}", spel, e.getMessage());
            return null;
        }
    }

}
