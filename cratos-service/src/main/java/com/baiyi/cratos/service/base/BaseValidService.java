package com.baiyi.cratos.service.base;

import com.baiyi.cratos.domain.generator.base.IExpiredTime;
import com.baiyi.cratos.domain.generator.base.IValid;
import com.baiyi.cratos.exception.DaoServiceException;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author baiyi
 * @Date 2024/1/23 17:13
 * @Version 1.0
 */
public interface BaseValidService<T extends IValid, M extends Mapper<T>> extends BaseService<T, M> {

    default void updateValidById(int id) {
        T t = this.getById(id);
        if (t == null) {
            throw new DaoServiceException("Domain is empty.");
        }
        if (t.getValid() == null) {
            throw new DaoServiceException("Domain field valid is empty.");
        }
        // 过期处理
        if (t instanceof IExpiredTime expiredTime) {
            if ((expiredTime.getExpiredTime()
                    .getTime() - System.currentTimeMillis()) <= 0 && !t.getValid()) {
                // 过期
                throw new DaoServiceException("Credential has expired and cannot be set as valid");
            }
        }
        t.setValid(!t.getValid());
        updateByPrimaryKey(t);
    }

}
