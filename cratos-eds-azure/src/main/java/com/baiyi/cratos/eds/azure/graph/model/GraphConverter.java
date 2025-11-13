package com.baiyi.cratos.eds.azure.graph.model;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2025/11/12 17:32
 * @Version 1.0
 */
public  class GraphConverter {

//    /**
//     * 从实现类获取泛型类型并转换对象
//     *
//     * @param map 源数据 Map
//     * @return 转换后的对象
//     */
//    @SuppressWarnings("unchecked")
//    public T of(Map<String, Object> map) {
//        if (map == null) {
//            return null;
//        }
//        try {
//            Class<T> clazz = Generics.find(this.getClass(), BaseEdsConfigLoader.class, 0);
//            T instance = clazz.getDeclaredConstructor()
//                    .newInstance();
//            for (Field field : clazz.getDeclaredFields()) {
//                field.setAccessible(true);
//                String fieldName = field.getName();
//                // 处理特殊字段映射
//                String mapKey = fieldName.equals("odataType") ? "@odata.type" : fieldName;
//                Object value = map.get(mapKey);
//                if (value != null) {
//                    try {
//                        field.set(instance, value);
//                    } catch (IllegalAccessException | IllegalArgumentException e) {
//                        // 忽略设置失败的字段
//                    }
//                }
//            }
//            return instance;
//        } catch (Exception e) {
//            return null;
//        }
//    }

    /**
     * 静态方法保持向后兼容
     */
    public static <T> T of(Map<String, Object> map, Class<T> clazz) {
        if (map == null || clazz == null) {
            return null;
        }

        try {
            T instance = clazz.getDeclaredConstructor()
                    .newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();

                String mapKey = fieldName.equals("odataType") ? "@odata.type" : fieldName;

                Object value = map.get(mapKey);
                if (value != null) {
                    try {
                        field.set(instance, value);
                    } catch (IllegalAccessException | IllegalArgumentException e) {
                        // 忽略设置失败的字段
                    }
                }
            }

            return instance;
        } catch (Exception e) {
            return null;
        }
    }

}
