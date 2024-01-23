package com.baiyi.cratos.service.base;

import com.baiyi.cratos.domain.generator.base.IValid;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author baiyi
 * @Date 2024/1/23 17:13
 * @Version 1.0
 */
public interface BaseValidService<T extends IValid, M extends Mapper<T>> extends BaseService<T, M>{

    default void updateValidById(int id){
       T t = this.getById(id);
       t.setValid(!t.getValid());
       updateByPrimaryKey(t);
    }

}
