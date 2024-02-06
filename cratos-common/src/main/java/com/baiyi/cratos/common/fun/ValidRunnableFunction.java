package com.baiyi.cratos.common.fun;

/**
 * @Author baiyi
 * @Date 2024/2/6 16:47
 * @Version 1.0
 */
@FunctionalInterface
public interface ValidRunnableFunction {

    void withValid(Runnable validRunnable, Runnable invalidRunnable);

}
