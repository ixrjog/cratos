package com.baiyi.cratos.domain;

import org.springframework.aop.support.AopUtils;

/**
 * @Author baiyi
 * @Date 2024/1/5 09:46
 * @Version 1.0
 */
public interface BaseBusiness {

    /**
     * 从注解获取BusinessType
     */
    interface IBusinessAnnotate extends IBusiness, IBusinessTypeAnnotate {
        default String getBusinessType() {
            return AopUtils.getTargetClass(this)
                    .getAnnotation(com.baiyi.cratos.domain.annotation.BusinessType.class)
                    .type()
                    .name();
        }
    }

    interface IBusiness extends IBusinessType {
        Integer getBusinessId();
    }

    interface IBusinessType {
        String getBusinessType();
    }

    interface IBusinessTypeAnnotate {
        default String getBusinessType() {
            return AopUtils.getTargetClass(this)
                    .getAnnotation(com.baiyi.cratos.domain.annotation.BusinessType.class)
                    .type()
                    .name();
        }
    }

}
