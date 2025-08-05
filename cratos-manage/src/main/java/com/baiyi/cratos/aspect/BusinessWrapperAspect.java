package com.baiyi.cratos.aspect;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import com.baiyi.cratos.wrapper.factory.BusinessWrapperFactory;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

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
        if (BusinessWrapper.InvokeAts.AFTER.equals(businessWrapper.invokeAt())) {
            run(joinPoint, businessWrapper);
        }
    }

    @Before(value = "@annotation(businessWrapper)")
    public void beforeAdvice(JoinPoint joinPoint, BusinessWrapper businessWrapper) {
        if (BusinessWrapper.InvokeAts.BEFORE.equals(businessWrapper.invokeAt())) {
            run(joinPoint, businessWrapper);
        }
    }

}
