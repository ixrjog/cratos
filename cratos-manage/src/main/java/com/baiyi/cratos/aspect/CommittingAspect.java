package com.baiyi.cratos.aspect;

import com.baiyi.cratos.common.util.IdentityUtils;
import com.baiyi.cratos.domain.Commited;
import com.baiyi.cratos.domain.annotation.Committing;
import com.baiyi.cratos.domain.generator.BusinessCommit;
import com.baiyi.cratos.service.BusinessCommitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/12/10 15:39
 * &#064;Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class CommittingAspect {

    // SpEL
    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
    private final ExpressionParser expressionParser = new SpelExpressionParser();

    private final BusinessCommitService businessCommitService;

    @Pointcut(value = "@annotation(com.baiyi.cratos.domain.annotation.Committing)")
    public void annotationPoint() {
    }

    @AfterReturning(value = "@annotation(committing)", returning = "commitedResult")
    public void doAfterReturning(JoinPoint joinPoint, Committing committing, Object commitedResult) {
        if (commitedResult instanceof Commited commited) {
            Authentication authentication = SecurityContextHolder.getContext()
                    .getAuthentication();
            final String username = authentication.getName();
            Integer businessId = getBusinessId(joinPoint, committing);
            if (!IdentityUtils.hasIdentity(businessId)) {
                log.warn("businessId does not exist.");
                return;
            }
            submit(commited, username, committing.typeOf()
                    .name(), businessId);
        }
    }

    private Integer getBusinessId(JoinPoint joinPoint, Committing committing) {
        //获取切面方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //获取方法的形参名称
        String[] params = discoverer.getParameterNames(method);
        //获取方法的实际参数值
        Object[] arguments = joinPoint.getArgs();
        //设置解析SpEL所需的数据上下文
        EvaluationContext context = new StandardEvaluationContext();
        IntStream.range(0, Objects.requireNonNull(params).length)
                .forEachOrdered(len -> context.setVariable(params[len], arguments[len]));
        //解析表达式并获取SpEL的值
        Expression expression = expressionParser.parseExpression(committing.businessId());
        Object businessIdSpEL = expression.getValue(context);
        if (businessIdSpEL instanceof Integer businessId) {
            return businessId;
        }
        return null;
    }

    private void submit(Commited commited, String username, String businessType, Integer businessId) {
        BusinessCommit businessCommit = BusinessCommit.builder()
                .name(commited.getName())
                .username(username)
                .businessType(businessType)
                .businessId(businessId)
                .commitId(commited.getCommitId())
                .commitMessage(commited.getCommitMessage())
                .commitContent(commited.getCommitContent())
                .build();
        businessCommitService.add(businessCommit);
    }

}
