package com.baiyi.cratos.domain.util;

import com.google.common.collect.MapMaker;
import lombok.NoArgsConstructor;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

/**
 * @Author baiyi
 * @Date 2020/1/7 1:52 下午
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class BeanCopierUtils {

    private static final Map<String, BeanCopier> BEAN_COPIER_MAP = new MapMaker().initialCapacity(32).concurrencyLevel(32).makeMap();

    /**
     * bean 对象copy
     *
     * @param source
     * @param targetClazz
     */
    public static <T> T copyProperties(Object source, Class<T> targetClazz) {
        if (source == null) {
            return null;
        }
        String beanKey = generateKey(source.getClass(), targetClazz);
        BeanCopier copier;
        if (!BEAN_COPIER_MAP.containsKey(beanKey)) {
            copier = BeanCopier.create(source.getClass(), targetClazz, false);
            BEAN_COPIER_MAP.put(beanKey, copier);
        } else {
            copier = BEAN_COPIER_MAP.get(beanKey);
        }
        T targetObject;
        try {
            targetObject = targetClazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        copier.copy(source, targetObject, null);
        return targetObject;
    }

    /**
     * @param source
     * @param targetClass
     * @throws Exception
     */
    public static <T> List<T> copyListProperties(List<?> source, Class<T> targetClass) {
        if (CollectionUtils.isEmpty(source)) {
            return new ArrayList<>();
        }
        List<T> target = new ArrayList<>();
        for (Object obj : source) {
            T targetObject;
            try {
                // targetObject = targetClass.newInstance();
                targetObject = targetClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            copyProperties(obj, targetObject);
            target.add(targetObject);
        }
        return target;
    }

    /**
     * bean 对象copy
     *
     * @param source
     * @param target
     */
    public static void copyProperties(Object source, Object target) {
        if (source == null) {
            return;
        }
        String beanKey = generateKey(source.getClass(), target.getClass());
        BeanCopier copier;
        if (!BEAN_COPIER_MAP.containsKey(beanKey)) {
            copier = BeanCopier.create(source.getClass(), target.getClass(), false);
            BEAN_COPIER_MAP.put(beanKey, copier);
        } else {
            copier = BEAN_COPIER_MAP.get(beanKey);
        }
        copier.copy(source, target, null);
    }

    private static String generateKey(Class<?> class1, Class<?> class2) {
        return class1.toString() + class2.toString();
    }

}