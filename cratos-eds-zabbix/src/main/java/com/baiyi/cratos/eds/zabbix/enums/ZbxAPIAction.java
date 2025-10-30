package com.baiyi.cratos.eds.zabbix.enums;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/10/29 13:39
 * &#064;Version 1.0
 */
public enum ZbxAPIAction {

    VERSION,

    LOGIN,
    /**
     * 大多数API至少包含四个方法：get、create、update和delete，分别用于检索、创建、更新和删除数据，但某些API可能会提供完全不同的方法集合。
     */
    GET,
    CREATE,
    UPDATE,
    DELETE;

}
