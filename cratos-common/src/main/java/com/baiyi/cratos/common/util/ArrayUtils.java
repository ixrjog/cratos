package com.baiyi.cratos.common.util;

import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;

/**
 * 数组工具类
 * 提供数组操作的常用方法
 * 
 * @Author baiyi
 * @Date 2023/7/26 09:24
 * @Version 1.0
 */
@NoArgsConstructor(access = PRIVATE)
public final class ArrayUtils {

    /**
     * 从字符数组中提取子数组
     * 支持负索引，-1表示最后一个元素，-2表示倒数第二个元素，以此类推
     * 
     * @param array 源字符数组，不能为null
     * @param start 起始索引（包含），支持负数
     * @param end   结束索引（不包含），支持负数
     * @return 提取的字符子数组，如果参数无效则返回空数组
     * @throws IllegalArgumentException 如果数组为null
     */
    public static char[] subArray(char[] array, int start, int end) {
        Objects.requireNonNull(array, "Array cannot be null");
        
        int length = array.length;
        
        // 处理负索引
        start = normalizeIndex(start, length);
        end = normalizeIndex(end, length);
        
        // 边界检查
        if (start >= length || end <= 0 || start >= end) {
            return new char[0];
        }
        
        // 确保索引在有效范围内
        start = Math.max(0, start);
        end = Math.min(length, end);
        
        return Arrays.copyOfRange(array, start, end);
    }

    /**
     * 检查数组是否为空（null或长度为0）
     * 
     * @param array 要检查的数组
     * @return 如果数组为null或长度为0返回true，否则返回false
     */
    public static boolean isEmpty(char[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 检查数组是否不为空
     * 
     * @param array 要检查的数组
     * @return 如果数组不为null且长度大于0返回true，否则返回false
     */
    public static boolean isNotEmpty(char[] array) {
        return !isEmpty(array);
    }

    /**
     * 获取数组的安全长度
     * 
     * @param array 数组
     * @return 数组长度，如果数组为null则返回0
     */
    public static int getLength(char[] array) {
        return array == null ? 0 : array.length;
    }

    /**
     * 反转字符数组
     * 
     * @param array 要反转的数组，不能为null
     * @return 反转后的新数组
     * @throws IllegalArgumentException 如果数组为null
     */
    public static char[] reverse(char[] array) {
        Objects.requireNonNull(array, "Array cannot be null");
        
        char[] reversed = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            reversed[i] = array[array.length - 1 - i];
        }
        return reversed;
    }

    /**
     * 连接多个字符数组
     * 
     * @param arrays 要连接的数组列表
     * @return 连接后的新数组
     */
    public static char[] concat(char[]... arrays) {
        if (arrays == null || arrays.length == 0) {
            return new char[0];
        }
        
        int totalLength = 0;
        for (char[] array : arrays) {
            if (array != null) {
                totalLength += array.length;
            }
        }
        
        char[] result = new char[totalLength];
        int pos = 0;
        for (char[] array : arrays) {
            if (array != null) {
                System.arraycopy(array, 0, result, pos, array.length);
                pos += array.length;
            }
        }
        
        return result;
    }

    /**
     * 标准化索引，处理负数索引
     * 
     * @param index  原始索引
     * @param length 数组长度
     * @return 标准化后的索引
     */
    private static int normalizeIndex(int index, int length) {
        return index < 0 ? index + length : index;
    }

}