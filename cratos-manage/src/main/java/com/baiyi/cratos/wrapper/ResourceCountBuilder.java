package com.baiyi.cratos.wrapper;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2024/1/25 16:26
 * @Version 1.0
 */
public class ResourceCountBuilder {

    private final Map<String, Integer> resourceCount = Maps.newHashMap();

    public static ResourceCountBuilder newBuilder(){
        return new ResourceCountBuilder();
    }

    public ResourceCountBuilder put(Map<String, Integer> resourceCount){
        this.resourceCount.putAll(resourceCount);
        return this;
    }

    public Map<String, Integer> build(){
        return resourceCount;
    }

}
