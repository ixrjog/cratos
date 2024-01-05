package com.baiyi.cratos.aspect;

import com.baiyi.cratos.annotation.BusinessWrapper;
import com.baiyi.cratos.domain.enums.BusinessTypeEnum;
import com.baiyi.cratos.wrapper.base.IBusinessWrapper;
import com.baiyi.cratos.wrapper.factory.BusinessWrapperFactory;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

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

    @After(value = "@annotation(businessWrapper)")
    public void afterAdvice(JoinPoint joinPoint, BusinessWrapper businessWrapper) {
        Object business = joinPoint.getArgs()[0];
        for (BusinessTypeEnum businessTypeEnum : businessWrapper.businessEnums()) {
            log.debug("BusinessWrapper: {}", businessTypeEnum.name());
            IBusinessWrapper businessWrapperBean = BusinessWrapperFactory.getWrapper(businessTypeEnum.name());
            if (businessWrapperBean != null) {
                businessWrapperBean.businessWrap(business);
            }
        }
    }

}
