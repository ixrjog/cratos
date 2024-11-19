package com.baiyi.cratos.facade.application;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/11/15 14:42
 * &#064;Version 1.0
 */
public interface ApplicationResourceFacade {

    void scan(String applicationName);

    void deleteById(int id);
}
