package com.baiyi.cratos.service.base;

import java.util.Date;
import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/7/30 下午1:49
 * &#064;Version 1.0
 */
public interface BaseQueryByExpiryService<T> {

    List<T> queryByLessThanExpiry(Date date);

}
