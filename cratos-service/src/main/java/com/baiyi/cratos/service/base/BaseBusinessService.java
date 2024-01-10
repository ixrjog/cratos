package com.baiyi.cratos.service.base;

import com.baiyi.cratos.domain.BaseBusiness;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2024/1/9 10:18
 * @Version 1.0
 */
public interface BaseBusinessService<T> extends BaseService<T, Mapper<T>> {

    List<T> selectByBusiness(BaseBusiness.IBusiness business);

}
