package com.baiyi.cratos.wrapper.base;

import com.baiyi.cratos.common.Converter;
import com.baiyi.cratos.domain.DataTable;

/**
 * @Author baiyi
 * @Date 2024/1/2 18:12
 * @Version 1.0
 */
public interface IDataTableConverter<T, S> extends IBaseWrapper<T>, Converter<S, T> {

    default DataTable<T> wrap(DataTable<S> dataTable, Class<T> targetClass) {
        return new DataTable<>(dataTable.getData().stream().map(d -> wrap(d, targetClass)).toList(), dataTable.getTotalNum());
    }

    /**
     * Convert & Wrap
     *
     * @param s
     * @param targetClass
     * @return
     */
    default T wrap(S s, Class<T> targetClass) {
        T t = convert(s, targetClass);
        wrap(t);
        return t;
    }

}