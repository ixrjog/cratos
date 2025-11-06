package com.baiyi.cratos.eds.zabbix.constant;

import java.util.Map;

import static java.util.Map.entry;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/6 11:42
 * &#064;Version 1.0
 */
public interface ZbxParamConstants {

    Map.Entry<String, Object> OUTPUT_EXTEND = entry("output", "extend");
    Map.Entry<String, Object> SELECT_ACKNOWLEDGES_EXTEND = entry("selectAcknowledges", "extend");
    Map.Entry<String, Object> SELECT_TAGS_EXTEND = entry("selectTags", "extend");
    Map.Entry<String, Object> SORT_ORDER_DESC = entry("sortorder", "DESC");
    Map.Entry<String, Object> RECENT = entry("recent", true);

}
