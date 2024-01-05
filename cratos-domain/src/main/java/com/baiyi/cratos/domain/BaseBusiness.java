package com.baiyi.cratos.domain;

/**
 * @Author baiyi
 * @Date 2024/1/5 09:46
 * @Version 1.0
 */
public interface BaseBusiness {

    interface IBusiness extends IBusinessType {
        Integer getBusinessId();
    }

    interface IBusinessType {
        String getBusinessType();
    }

}
