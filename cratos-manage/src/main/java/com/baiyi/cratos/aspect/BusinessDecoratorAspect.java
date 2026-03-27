package com.baiyi.cratos.aspect;

import com.baiyi.cratos.annotation.BusinessDecorator;
import com.baiyi.cratos.domain.annotation.BusinessType;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.wrapper.base.BaseBusinessDecorator;
import com.baiyi.cratos.wrapper.factory.BusinessDecoratorFactory;
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
public class BusinessDecoratorAspect {

    @Pointcut(value = "@annotation(com.baiyi.cratos.annotation.BusinessDecorator)")
    public void annotationPoint() {
    }

    private void decorate(JoinPoint joinPoint, BusinessDecorator decorator) {
        Object business = joinPoint.getArgs()[0];
        // 未指定types则从类注解中获取BusinessType
        BusinessTypeEnum[] types = decorator.types().length != 0 ? decorator.types() : new BusinessTypeEnum[]{AopUtils.getTargetClass(
                        joinPoint.getTarget())
                .getAnnotation(BusinessType.class).type()};
        Arrays.stream(types)
                .forEachOrdered(businessTypeEnum -> {
                    log.debug("BusinessDecorator: {}", businessTypeEnum.name());
                    BaseBusinessDecorator decoratorBean = BusinessDecoratorFactory.getDecorator(
                            businessTypeEnum.name());
                    if (decoratorBean != null) {
                        decoratorBean.decorateBusiness(business);
                    }
                });
    }

    @After(value = "@annotation(decorator)")
    public void afterAdvice(JoinPoint joinPoint, BusinessDecorator decorator) {
        if (BusinessDecorator.Phase.AFTER.equals(decorator.phase())) {
            decorate(joinPoint, decorator);
        }
    }

    @Before(value = "@annotation(decorator)")
    public void beforeAdvice(JoinPoint joinPoint, BusinessDecorator decorator) {
        if (BusinessDecorator.Phase.BEFORE.equals(decorator.phase())) {
            decorate(joinPoint, decorator);
        }
    }

}
