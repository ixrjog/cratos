package com.baiyi.cratos.domain.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Properties文件读取工具类
 * 支持多语言文件读取和消息格式化
 *
 * @author baiyi
 */
@Slf4j
public class PropertiesReaderUtils {

    /**
     * Properties文件缓存
     */
    private static final ConcurrentHashMap<String, Properties> PROPERTIES_CACHE = new ConcurrentHashMap<>();

    /**
     * 默认的properties文件路径
     */
    private static final String DEFAULT_MESSAGES_PATH = "messages.properties";
    private static final String MESSAGES_EN_US_PATH = "messages_en_US.properties";
    private static final String MESSAGES_ZH_CN_PATH = "messages_zh_CN.properties";

    /**
     * 读取指定properties文件
     *
     * @param filePath properties文件路径（相对于classpath）
     * @return Properties对象
     */
    public static Properties loadProperties(String filePath) {
        return PROPERTIES_CACHE.computeIfAbsent(filePath, path -> {
            Properties properties = new Properties();
            try {
                ClassPathResource resource = new ClassPathResource(path);
                if (resource.exists()) {
                    // 方法1：使用UTF-8编码读取
                    try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(),
                            StandardCharsets.UTF_8)) {
                        properties.load(reader);
                        log.debug("Successfully loaded properties file with UTF-8: {}", path);
                    }
                } else {
                    log.warn("Properties file not found: {}", path);
                }
            } catch (IOException e) {
                log.error("Failed to load properties file: {}", path, e);
                // 如果UTF-8失败，尝试GBK编码
                try {
                    ClassPathResource resource = new ClassPathResource(path);
                    try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(),
                            Charset.forName("GBK"))) {
                        properties.load(reader);
                        log.debug("Successfully loaded properties file with GBK: {}", path);
                    }
                } catch (IOException ex) {
                    log.error("Failed to load properties file with GBK: {}", path, ex);
                }
            }
            return properties;
        });
    }


    /**
     * 从指定properties文件获取配置值
     *
     * @param filePath properties文件路径
     * @param key      配置键
     * @return 配置值，如果不存在返回null
     */
    public static String getProperty(String filePath, String key) {
        Properties properties = loadProperties(filePath);
        return properties.getProperty(key);
    }

    /**
     * 从指定properties文件获取配置值，支持默认值
     *
     * @param filePath     properties文件路径
     * @param key          配置键
     * @param defaultValue 默认值
     * @return 配置值，如果不存在返回默认值
     */
    public static String getProperty(String filePath, String key, String defaultValue) {
        Properties properties = loadProperties(filePath);
        return properties.getProperty(key, defaultValue);
    }

    /**
     * 从英文消息文件获取配置
     *
     * @param key 配置键
     * @return 配置值
     */
    public static String getEnUsMessage(String key) {
        return getProperty(MESSAGES_EN_US_PATH, key);
    }

    /**
     * 从中文消息文件获取配置
     *
     * @param key 配置键
     * @return 配置值
     */
    public static String getZhCnMessage(String key) {
        return getProperty(MESSAGES_ZH_CN_PATH, key);
    }

    /**
     * 从默认消息文件获取配置
     *
     * @param key 配置键
     * @return 配置值
     */
    public static String getDefaultMessage(String key) {
        return getProperty(DEFAULT_MESSAGES_PATH, key);
    }

    /**
     * 获取格式化的消息（支持占位符）
     *
     * @param filePath properties文件路径
     * @param key      配置键
     * @param args     格式化参数
     * @return 格式化后的消息
     */
    public static String getFormattedMessage(String filePath, String key, Object... args) {
        String message = getProperty(filePath, key);
        if (StringUtils.hasText(message) && args != null && args.length > 0) {
            try {
                return StringFormatter.arrayFormat(message, args);
            } catch (Exception e) {
                log.warn("Failed to format message: key={}, message={}", key, message, e);
                return message;
            }
        }
        return message;
    }

    /**
     * 获取格式化的英文消息
     *
     * @param key  配置键
     * @param args 格式化参数
     * @return 格式化后的消息
     */
    public static String getFormattedEnUsMessage(String key, Object... args) {
        return getFormattedMessage(MESSAGES_EN_US_PATH, key, args);
    }

    /**
     * 获取格式化的中文消息
     *
     * @param key  配置键
     * @param args 格式化参数
     * @return 格式化后的消息
     */
    public static String getFormattedZhCnMessage(String key, Object... args) {
        return getFormattedMessage(MESSAGES_ZH_CN_PATH, key, args);
    }

    /**
     * 检查指定的key是否存在
     *
     * @param filePath properties文件路径
     * @param key      配置键
     * @return 是否存在
     */
    public static boolean containsKey(String filePath, String key) {
        Properties properties = loadProperties(filePath);
        return properties.containsKey(key);
    }

    /**
     * 获取所有配置键
     *
     * @param filePath properties文件路径
     * @return 所有配置键的集合
     */
    public static java.util.Set<String> getAllKeys(String filePath) {
        Properties properties = loadProperties(filePath);
        return properties.stringPropertyNames();
    }

    /**
     * 清除缓存
     */
    public static void clearCache() {
        PROPERTIES_CACHE.clear();
        log.info("Properties cache cleared");
    }

    /**
     * 清除指定文件的缓存
     *
     * @param filePath properties文件路径
     */
    public static void clearCache(String filePath) {
        PROPERTIES_CACHE.remove(filePath);
        log.info("Properties cache cleared for file: {}", filePath);
    }

    /**
     * 获取缓存统计信息
     *
     * @return 缓存的文件数量
     */
    public static int getCacheSize() {
        return PROPERTIES_CACHE.size();
    }
}
