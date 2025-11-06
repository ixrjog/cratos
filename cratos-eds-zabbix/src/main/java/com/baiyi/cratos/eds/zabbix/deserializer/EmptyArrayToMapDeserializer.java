package com.baiyi.cratos.eds.zabbix.deserializer;

import com.baiyi.cratos.eds.zabbix.result.ZbxHostResult;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理空数组转换为空Map的反序列化器
 */
public class EmptyArrayToMapDeserializer extends JsonDeserializer<Map<String, ZbxHostResult.Host>> {
    
    @Override
    public Map<String, ZbxHostResult.Host> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.currentToken() == JsonToken.START_ARRAY) {
            // 如果是空数组，跳过并返回空Map
            if (p.nextToken() == JsonToken.END_ARRAY) {
                return new HashMap<>();
            }
            // 如果数组不为空，抛出异常
            throw new IllegalArgumentException("Expected empty array but found non-empty array");
        }
        
        // 如果是对象，正常反序列化为Map<String, Host>
        if (p.currentToken() == JsonToken.START_OBJECT) {
            return p.readValueAs(new TypeReference<Map<String, ZbxHostResult.Host>>() {});
        }
        
        return new HashMap<>();
    }

}
