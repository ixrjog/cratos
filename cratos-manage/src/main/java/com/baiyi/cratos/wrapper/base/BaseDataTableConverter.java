package com.baiyi.cratos.wrapper.base;

import com.baiyi.cratos.common.Converter;
import com.baiyi.cratos.domain.util.SpringContextUtils;
import com.baiyi.cratos.domain.DataTable;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author baiyi
 * @Date 2024/1/9 15:45
 * @Version 1.0
 */
@Slf4j
public abstract class BaseDataTableConverter<T, S> implements BaseWrapper<T>, Converter<S, T> {

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
     * @return
     */
    public T wrapToTarget(S s) {
        T t = convert(s);
        delegateWrap(t);
        return t;
    }

    @SuppressWarnings("unchecked")
    protected void delegateWrap(T t) {
        // BaseDataTableConverter<T, S> bean = (BaseDataTableConverter<T, S>) AopContext.currentProxy();
        BaseWrapper<T> bean = SpringContextUtils.getBean(this.getClass());
        bean.wrap(t);
    }

}
