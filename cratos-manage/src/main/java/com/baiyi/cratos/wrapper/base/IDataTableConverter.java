package com.baiyi.cratos.wrapper.base;

import com.baiyi.cratos.common.Converter;
import com.baiyi.cratos.domain.DataTable;

/**
 * @Author baiyi
 * @Date 2024/1/2 18:12
 * @Version 1.0
 */
public interface IDataTableConverter<T, S> extends IBaseWrapper<T>, Converter<S, T> {

    default DataTable<T> wrapToTarget(DataTable<S> dataTable) {
        return new DataTable<>(dataTable.getData().stream().map(this::wrapToTarget).toList(), dataTable.getTotalNum());
    }

    /**
     * Convert & Wrap
     *
     * @param s
     * @param targetClass
     * @return
     */
    default T wrapToTarget(S s) {
        T t = convert(s);
        getBean().wrap(t);
        //wrap(t);
        return t;
    }

    IBaseWrapper<T> getBean();

}