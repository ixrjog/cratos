package com.baiyi.cratos.common.exception;

/**
 * @Author baiyi
 * @Date 2024/2/6 16:12
 * @Version 1.0
 */
@FunctionalInterface
public interface BaseExceptionFunction {

    void throwBaseException(BaseException baseException);

}
