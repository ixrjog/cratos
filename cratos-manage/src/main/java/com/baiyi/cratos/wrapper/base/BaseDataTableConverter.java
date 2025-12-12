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
public abstract class BaseDataTableConverter<VO, DO> implements BaseWrapper<VO>, Converter<DO, VO> {

    public DataTable<VO> wrapToTarget(DataTable<DO> dataTable) {
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
    public VO wrapToTarget(DO s) {
        VO t = convert(s);
        delegateWrap(t);
        return t;
    }

    @SuppressWarnings("unchecked")
    protected void delegateWrap(VO t) {
        // BaseDataTableConverter<VO, DO> bean = (BaseDataTableConverter<VO, DO>) AopContext.currentProxy();
        BaseWrapper<VO> bean = SpringContextUtils.getBean(this.getClass());
        bean.wrap(t);
    }

}
