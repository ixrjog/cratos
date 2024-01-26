package com.baiyi.cratos.eds;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/26 11:26
 * @Version 1.0
 */
public interface IExternalDataSourceInstance<P,T> {

    IExternalDataSourceConfig getConfig();

    List<P> getProvider();

}
