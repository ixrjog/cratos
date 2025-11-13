package com.baiyi.cratos.eds.azure.graph.model;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/11/12 17:46
 * &#064;Version 1.0
 */
public class BaseGraph {

    public interface GraphMapper<T> {
        default T to(java.util.Map<String, Object> map) {
            return GraphConverter.of(map, (Class<T>) this.getClass());
        }
    }

}
