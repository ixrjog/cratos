package com.baiyi.cratos.eds.zabbix.result.base;

import com.baiyi.cratos.eds.zabbix.deserializer.EmptyArrayToMapDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

/**
 * 专门处理Map结果的Zabbix响应类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZbxMapResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = -2135779601525149910L;
    
    private String jsonrpc;
    
    @JsonDeserialize(using = EmptyArrayToMapDeserializer.class)
    private Map<String, T> result;
}
