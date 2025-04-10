package com.baiyi.cratos.aspect;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import com.baiyi.cratos.wrapper.factory.BusinessWrapperFactory;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2024/1/5 10:59
 * @Version 1.0
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Slf4j
@Aspect
@Component
public class BusinessWrapperAspect {

    @Pointcut(value = "@annotation(com.baiyi.cratos.annotation.BusinessWrapper)")
    public void annotationPoint() {
    }

    private static final ThreadLocal<Set<Object>> PROCESSING_OBJECTS = ThreadLocal.withInitial(HashSet::new);

    @Around("@annotation(com.baiyi.cratos.annotation.BusinessWrapper)")
    public Object aroundBusinessWrapper(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] != null) {
            // 检查是否正在处理同一对象
            if (!PROCESSING_OBJECTS.get()
                    .add(args[0])) {
                // 检测到循环调用，跳过处理
                return null;
            }

            try {
                // 执行原方法
                return joinPoint.proceed();
            } finally {
                // 处理完成后移除对象
                PROCESSING_OBJECTS.get()
                        .remove(args[0]);
            }
        }
        return joinPoint.proceed();
    }

    private void run(JoinPoint joinPoint, BusinessWrapper businessWrapper) {
        Object business = joinPoint.getArgs()[0];
        // 未指定types则从类注解中获取BusinessType
        BusinessTypeEnum[] types = businessWrapper.ofTypes().length != 0 ? businessWrapper.ofTypes() : new BusinessTypeEnum[]{AopUtils.getTargetClass(
                        joinPoint.getTarget())
                .getAnnotation(BusinessType.class).type()};
        Arrays.stream(types)
                .forEachOrdered(businessTypeEnum -> {
                    log.debug("BusinessWrapper: {}", businessTypeEnum.name());
                    IBusinessWrapper businessWrapperBean = BusinessWrapperFactory.getWrapper(businessTypeEnum.name());
                    if (businessWrapperBean != null) {
                        businessWrapperBean.businessWrap(business);
                    }
                });
    }

    @After(value = "@annotation(businessWrapper)")
    public void afterAdvice(JoinPoint joinPoint, BusinessWrapper businessWrapper) {
        if (!businessWrapper.invokeAt()) {
            run(joinPoint, businessWrapper);
        }
    }

    @Before(value = "@annotation(businessWrapper)")
    public void beforeAdvice(JoinPoint joinPoint, BusinessWrapper businessWrapper) {
        if (businessWrapper.invokeAt()) {
            run(joinPoint, businessWrapper);
        }
    }

}
