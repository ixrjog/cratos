package com.baiyi.cratos.common.builder;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/16 1:24 下午
 * @Version 1.0
 */
public class DictBuilder {

    private final Map<String, String> dict = Maps.newHashMap();

    private DictBuilder() {
    }

    static public DictBuilder newBuilder() {
        return new DictBuilder();
    }

    public DictBuilder put(String name, String value) {
        if (!StringUtils.isEmpty(value)) {
            this.dict.put(name, value);
        }
        return this;
    }

    public DictBuilder put(Map<String, String> dict) {
        if (dict != null) {
            this.dict.putAll(dict);
        }
        return this;
    }

    public Map<String, String> build() {
        return dict;
    }

}