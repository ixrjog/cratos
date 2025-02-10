package com.baiyi.cratos;

import com.baiyi.cratos.service.base.BaseValidService;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/9/12 16:44
 * &#064;Version 1.0
 */
public interface HasSetValid {

    default void setValidById(int id) {
        BaseValidService<?, ?> baseValidService = getValidService();
        if (baseValidService != null) {
            baseValidService.updateValidById(id);
        }
    }

    BaseValidService<?, ?> getValidService();

}
