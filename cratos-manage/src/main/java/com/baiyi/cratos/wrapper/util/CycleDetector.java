package com.baiyi.cratos.wrapper.util;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * &#064;Author  baiyi
 * &#064;Date  2025/4/10 16:18
 * &#064;Version 1.0
 */
public class CycleDetector {
    // 使用ThreadLocal存储当前线程的处理状态
    private static final ThreadLocal<Map<Object, String>> PROCESSING_OBJECTS = ThreadLocal.withInitial(
            IdentityHashMap::new);

    /**
     * 检查对象是否正在被处理，如果是则返回true
     *
     * @param object  要检查的对象
     * @param context 处理上下文(用于日志)
     * @return 如果对象正在被处理则返回true
     */
    public static boolean isProcessing(Object object, String context) {
        if (object == null) {
            return false;
        }

        Map<Object, String> processing = PROCESSING_OBJECTS.get();
        if (processing.containsKey(object)) {
            // 记录循环引用信息
            String previousContext = processing.get(object);
            System.out.println(
                    "Cycle detected: " + context + " -> Object already being processed in: " + previousContext);
            return true;
        }
        return false;
    }

    /**
     * 标记对象正在被处理
     *
     * @param object  要标记的对象
     * @param context 处理上下文(用于日志)
     */
    public static void markProcessing(Object object, String context) {
        if (object != null) {
            PROCESSING_OBJECTS.get()
                    .put(object, context);
        }
    }

    /**
     * 标记对象处理完成
     *
     * @param object 处理完成的对象
     */
    public static void markProcessed(Object object) {
        if (object != null) {
            PROCESSING_OBJECTS.get()
                    .remove(object);
        }
    }

    /**
     * 清理当前线程的所有处理状态
     */
    public static void clear() {
        PROCESSING_OBJECTS.get()
                .clear();
    }
}
