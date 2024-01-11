package com.baiyi.cratos.wrapper.base;

import com.baiyi.cratos.common.Converter;
import com.baiyi.cratos.domain.DataTable;
import org.springframework.aop.framework.AopContext;

/**
 * @Author baiyi
 * @Date 2024/1/9 15:45
 * @Version 1.0
 */
public abstract class BaseDataTableConverter<T, S> implements IBaseWrapper<T>, Converter<S, T> {

    public DataTable<T> wrapToTarget(DataTable<S> dataTable) {
        return new DataTable<>(dataTable.getData()
                .stream()
                .map(this::wrapToTarget)
                .toList(), dataTable.getTotalNum());
    }

    /**
     * Convert & Wrap
     *
     * @param s
     * @param targetClass
     * @return
     */
    public T wrapToTarget(S s) {
        T t = convert(s);
        wrapFromProxy(t);
        return t;
    }

    @SuppressWarnings("unchecked")
    protected void wrapFromProxy(T t) {
        BaseDataTableConverter<T, S> bean = (BaseDataTableConverter<T, S>) AopContext.currentProxy();
        bean.wrap(t);
    }

}
