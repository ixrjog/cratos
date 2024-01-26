package com.baiyi.cratos.common.builder;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/1/26 14:15
 * @Version 1.0
 */
public class SimpleMapBuilder {

    private final Map<String, Object> simpleMap = Maps.newHashMap();

    private SimpleMapBuilder() {
    }

    static public SimpleMapBuilder newBuilder() {
        return new SimpleMapBuilder();
    }

    public SimpleMapBuilder put(String name, Object value) {
        this.simpleMap.put(name, value);
        return this;
    }

    public SimpleMapBuilder put(Map<String, Object> map) {
        this.simpleMap.putAll(map);
        return this;
    }

    public Map<String, Object> build() {
        return simpleMap;
    }

}
