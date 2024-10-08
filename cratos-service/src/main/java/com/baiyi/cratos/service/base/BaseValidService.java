package com.baiyi.cratos.service.base;

import com.baiyi.cratos.domain.generator.base.HasExpiredTime;
import com.baiyi.cratos.domain.generator.base.HasValid;
import com.baiyi.cratos.exception.DaoServiceException;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author baiyi
 * @Date 2024/1/23 17:13
 * @Version 1.0
 */
public interface BaseValidService<T extends HasValid, M extends Mapper<T>> extends BaseService<T, M> {

    /**
     * 设置 valid 属性
     *
     * @param id
     */
    default void updateValidById(int id) {
        T record = this.getById(id);
        if (record == null) {
            throw new DaoServiceException("Domain is empty.");
        }
        if (record.getValid() == null) {
            throw new DaoServiceException("Domain field valid is empty.");
        }
        // 过期处理
        if (record instanceof HasExpiredTime expiredTime) {
            if ((expiredTime.getExpiredTime()
                    .getTime() - System.currentTimeMillis()) <= 0 && !record.getValid()) {
                // 过期
                throw new DaoServiceException(record.getClass()
                        .getSimpleName() + " has expired and cannot be set as valid");
            }
        }
        record.setValid(!record.getValid());
        updateByPrimaryKey(record);
    }

}
