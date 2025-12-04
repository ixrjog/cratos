package com.baiyi.cratos.eds.core.config.base;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/12/3 10:15
 * &#064;Version 1.0
 */
public interface HasDnsNameServers {

    void setNameServers(List<String> nameServers);

    List<String> getNameServers();

}
