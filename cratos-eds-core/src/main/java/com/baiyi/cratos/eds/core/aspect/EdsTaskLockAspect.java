package com.baiyi.cratos.eds.core.aspect;

import com.baiyi.cratos.common.RedisUtil;
import com.baiyi.cratos.common.util.StringFormatter;
import com.baiyi.cratos.common.util.ReadableDurationUtil;
import com.baiyi.cratos.eds.core.annotation.EdsInstanceAssetType;
import com.baiyi.cratos.eds.core.annotation.EdsTaskLock;
import com.baiyi.cratos.eds.core.exception.EdsTaskLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @Author baiyi
 * @Date 2024/2/27 10:52
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@Order(-1)
public class EdsTaskLockAspect {

    private static final String LOCK_KEY = "OC5:EDS:TASK:INSTANCE:TYPE:{}:ID:{}:ASSET_TYPE:{}";

    private final RedisUtil redisUtil;

    private static final int RUNNING = 1;

    // SpEL
    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
    private final ExpressionParser expressionParser = new SpelExpressionParser();

    @Pointcut(value = "@annotation(com.baiyi.cratos.eds.core.annotation.EdsTaskLock)")
    public void annotationPoint() {
    }

    @Around("@annotation(edsTaskLock)")
    public Object around(ProceedingJoinPoint joinPoint, EdsTaskLock edsTaskLock) throws Throwable {
        String key = generateLockKey(joinPoint, edsTaskLock);
        if (!StringUtils.hasText(key)) {
            return joinPoint.proceed();
        }
        String providerBeanName = AopUtils.getTargetClass(joinPoint.getTarget())
                .getSimpleName();
        try {
            if (isLocked(key)) {
                log.warn("Execute {} task repeat", providerBeanName);
                return new EdsTaskLockException("The task is currently running.");
            } else {
                // 加锁
                lock(key, edsTaskLock.maxLockTime());
                log.info("Execute {} task start", providerBeanName);
                StopWatch stopWatch = new StopWatch();
                stopWatch.start("Execute asset sync task");
                Object result = joinPoint.proceed();
                //unlock(key);
                stopWatch.stop();
                log.info("Execute {} task ended, runtime={}/s", providerBeanName, stopWatch.getTotalTimeSeconds());
                return result;
            }
        } catch (Exception e) {
            log.warn("Execute {} task error: {}", providerBeanName, e.getMessage());
        } finally {
            // 解锁
            unlock(key);
        }
        return new Throwable();
    }

    private String generateLockKey(JoinPoint joinPoint, EdsTaskLock edsTaskLock) {
        EdsInstanceAssetType edsInstanceAssetType = AopUtils.getTargetClass(joinPoint.getTarget())
                .getAnnotation(EdsInstanceAssetType.class);
        // 缺少注解
        if (edsInstanceAssetType == null) {
            return null;
        }
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
        Expression expression = expressionParser.parseExpression(edsTaskLock.instanceId());
        Object instanceIdSpEL = expression.getValue(context);
        if (instanceIdSpEL instanceof Integer instanceId) {
            return StringFormatter.arrayFormat(LOCK_KEY, edsInstanceAssetType.instanceType(), instanceId, edsInstanceAssetType.assetType());
        }
        return null;
    }

    private void lock(String lockKey, String time) {
        Duration duration = ReadableDurationUtil.parse(time);
        redisUtil.set(lockKey, RUNNING, duration.getSeconds());
    }

    private void unlock(String lockKey) {
        redisUtil.del(lockKey);
    }

    private boolean isLocked(String lockKey) {
        return redisUtil.get(lockKey) != null;
    }

}
