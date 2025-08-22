package com.baiyi.cratos.common.util;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 8位ID生成工具类
 * 
 * 功能特性:
 * - 生成8位长度的随机ID
 * - 字符集包含数字(0-9)和小写字母(a-z)，共36个字符
 * - 支持高并发场景
 * - 提供安全随机和快速随机两种模式
 * 
 * @author cratos
 * @version 1.0
 */
public class IdGenerator {
    
    /**
     * 字符集：数字0-9 + 小写字母a-z (共36个字符)
     */
    private static final String CHARACTERS = "0123456789abcdefghijklmnopqrstuvwxyz";
    
    /**
     * ID长度
     */
    private static final int ID_LENGTH = 8;
    
    /**
     * 字符集长度
     */
    private static final int CHARSET_SIZE = CHARACTERS.length();
    
    /**
     * 安全随机数生成器（线程安全，适用于安全要求高的场景）
     */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    
    /**
     * 私有构造函数，防止实例化
     */
    private IdGenerator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * 生成8位随机ID（快速模式）
     * 使用ThreadLocalRandom，性能更好，适用于一般场景
     * 
     * @return 8位随机ID字符串
     */
    public static String generateId() {
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        
        for (int i = 0; i < ID_LENGTH; i++) {
            int index = random.nextInt(CHARSET_SIZE);
            sb.append(CHARACTERS.charAt(index));
        }
        
        return sb.toString();
    }
    
    /**
     * 生成8位随机ID（安全模式）
     * 使用SecureRandom，安全性更高，适用于安全要求高的场景
     * 
     * @return 8位随机ID字符串
     */
    public static String generateSecureId() {
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        
        for (int i = 0; i < ID_LENGTH; i++) {
            int index = SECURE_RANDOM.nextInt(CHARSET_SIZE);
            sb.append(CHARACTERS.charAt(index));
        }
        
        return sb.toString();
    }
    
    /**
     * 批量生成8位随机ID
     * 
     * @param count 生成数量
     * @return ID数组
     */
    public static String[] generateIds(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }
        
        String[] ids = new String[count];
        for (int i = 0; i < count; i++) {
            ids[i] = generateId();
        }
        
        return ids;
    }
    
    /**
     * 批量生成8位随机ID（安全模式）
     * 
     * @param count 生成数量
     * @return ID数组
     */
    public static String[] generateSecureIds(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }
        
        String[] ids = new String[count];
        for (int i = 0; i < count; i++) {
            ids[i] = generateSecureId();
        }
        
        return ids;
    }
    
    /**
     * 验证ID格式是否正确
     * 
     * @param id 待验证的ID
     * @return true表示格式正确，false表示格式错误
     */
    public static boolean isValidId(String id) {
        if (id == null || id.length() != ID_LENGTH) {
            return false;
        }
        
        for (char c : id.toCharArray()) {
            if (CHARACTERS.indexOf(c) == -1) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * 获取字符集信息
     * 
     * @return 字符集字符串
     */
    public static String getCharacterSet() {
        return CHARACTERS;
    }
    
    /**
     * 获取ID长度
     * 
     * @return ID长度
     */
    public static int getIdLength() {
        return ID_LENGTH;
    }
    
    /**
     * 获取理论上的总组合数
     * 
     * @return 总组合数 (36^8)
     */
    public static long getTotalCombinations() {
        return (long) Math.pow(CHARSET_SIZE, ID_LENGTH);
    }
}
