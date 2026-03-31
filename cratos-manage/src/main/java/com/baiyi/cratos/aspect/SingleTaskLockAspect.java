package com.baiyi.cratos.aspect;

import com.baiyi.cratos.annotation.SingleTaskLock;
import com.baiyi.cratos.common.RedisUtil;
import com.baiyi.cratos.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @Author baiyi
 * @Date 2026/3/30
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SingleTaskLockAspect {

    private final RedisUtil redisUtil;

    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
    private final ExpressionParser expressionParser = new SpelExpressionParser();

    @Around("@annotation(singleTaskLock)")
    public Object around(ProceedingJoinPoint joinPoint, SingleTaskLock singleTaskLock) throws Throwable {
        String lockKey = resolveLockKey(joinPoint, singleTaskLock);
        if (redisUtil.get(lockKey) != null) {
            long ttl = redisUtil.getExpire(lockKey);
            throw new SingleTaskLockException("Task is rate limited, please retry after " + ttl + " seconds.");
        }
        redisUtil.set(lockKey, 1, singleTaskLock.maxLockTime());
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
            throw e;
        }
    }

    private String resolveLockKey(ProceedingJoinPoint joinPoint, SingleTaskLock singleTaskLock) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] params = discoverer.getParameterNames(method);
        Object[] arguments = joinPoint.getArgs();
        EvaluationContext context = new StandardEvaluationContext();
        IntStream.range(0, Objects.requireNonNull(params).length)
                .forEachOrdered(i -> context.setVariable(params[i], arguments[i]));
        Object keyValue = expressionParser.parseExpression(singleTaskLock.key()).getValue(context);
        return singleTaskLock.keyPrefix() + ":" + keyValue;
    }

    public static class SingleTaskLockException extends BaseException {
        @Serial
        private static final long serialVersionUID = -186179459375149138L;

        public SingleTaskLockException(String message) {
            super(message);
        }
    }

}
