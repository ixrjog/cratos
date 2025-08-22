package com.baiyi.cratos.common.util;

import java.util.HashSet;
import java.util.Set;

/**
 * IdGenerator工具类测试
 * 
 * @author cratos
 * @version 1.0
 */
public class IdGeneratorTest {
    
    public static void main(String[] args) {
        System.out.println("=== IdGenerator 工具类测试 ===\n");
        
        // 1. 基本功能测试
        testBasicGeneration();
        
        // 2. 批量生成测试
        testBatchGeneration();
        
        // 3. 安全模式测试
        testSecureGeneration();
        
        // 4. 验证功能测试
        testValidation();
        
        // 5. 工具信息测试
        testUtilityInfo();
        
        // 6. 性能测试
        testPerformance();
        
        // 7. 唯一性测试
        testUniqueness();
    }
    
    /**
     * 基本功能测试
     */
    private static void testBasicGeneration() {
        System.out.println("1. 基本功能测试:");
        
        for (int i = 0; i < 10; i++) {
            String id = IdGenerator.generateId();
            System.out.println("   生成ID: " + id + " (长度: " + id.length() + ")");
        }
        System.out.println();
    }
    
    /**
     * 批量生成测试
     */
    private static void testBatchGeneration() {
        System.out.println("2. 批量生成测试:");
        
        String[] ids = IdGenerator.generateIds(5);
        for (int i = 0; i < ids.length; i++) {
            System.out.println("   批量ID[" + i + "]: " + ids[i]);
        }
        System.out.println();
    }
    
    /**
     * 安全模式测试
     */
    private static void testSecureGeneration() {
        System.out.println("3. 安全模式测试:");
        
        for (int i = 0; i < 5; i++) {
            String secureId = IdGenerator.generateSecureId();
            System.out.println("   安全ID: " + secureId);
        }
        System.out.println();
    }
    
    /**
     * 验证功能测试
     */
    private static void testValidation() {
        System.out.println("4. 验证功能测试:");
        
        String validId = IdGenerator.generateId();
        System.out.println("   有效ID: " + validId + " -> " + IdGenerator.isValidId(validId));
        
        String invalidId1 = "12345";  // 长度不够
        System.out.println("   无效ID: " + invalidId1 + " -> " + IdGenerator.isValidId(invalidId1));
        
        String invalidId2 = "1234567Z";  // 包含大写字母
        System.out.println("   无效ID: " + invalidId2 + " -> " + IdGenerator.isValidId(invalidId2));
        
        String invalidId3 = "1234567@";  // 包含特殊字符
        System.out.println("   无效ID: " + invalidId3 + " -> " + IdGenerator.isValidId(invalidId3));
        System.out.println();
    }
    
    /**
     * 工具信息测试
     */
    private static void testUtilityInfo() {
        System.out.println("5. 工具信息测试:");
        System.out.println("   字符集: " + IdGenerator.getCharacterSet());
        System.out.println("   字符集长度: " + IdGenerator.getCharacterSet().length());
        System.out.println("   ID长度: " + IdGenerator.getIdLength());
        System.out.println("   理论组合数: " + String.format("%,d", IdGenerator.getTotalCombinations()));
        System.out.println();
    }
    
    /**
     * 性能测试
     */
    private static void testPerformance() {
        System.out.println("6. 性能测试:");
        
        int testCount = 100000;
        
        // 快速模式性能测试
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < testCount; i++) {
            IdGenerator.generateId();
        }
        long fastTime = System.currentTimeMillis() - startTime;
        
        // 安全模式性能测试
        startTime = System.currentTimeMillis();
        for (int i = 0; i < testCount; i++) {
            IdGenerator.generateSecureId();
        }
        long secureTime = System.currentTimeMillis() - startTime;
        
        System.out.println("   生成 " + String.format("%,d", testCount) + " 个ID:");
        System.out.println("   快速模式耗时: " + fastTime + "ms");
        System.out.println("   安全模式耗时: " + secureTime + "ms");
        System.out.println("   性能比率: " + String.format("%.2f", (double) secureTime / fastTime) + ":1");
        System.out.println();
    }
    
    /**
     * 唯一性测试
     */
    private static void testUniqueness() {
        System.out.println("7. 唯一性测试:");
        
        int testCount = 50000;
        Set<String> idSet = new HashSet<>();
        
        for (int i = 0; i < testCount; i++) {
            String id = IdGenerator.generateId();
            idSet.add(id);
        }
        
        int uniqueCount = idSet.size();
        double uniqueRate = (double) uniqueCount / testCount * 100;
        
        System.out.println("   生成ID数量: " + String.format("%,d", testCount));
        System.out.println("   唯一ID数量: " + String.format("%,d", uniqueCount));
        System.out.println("   唯一性比率: " + String.format("%.4f", uniqueRate) + "%");
        System.out.println("   重复ID数量: " + String.format("%,d", testCount - uniqueCount));
        
        if (uniqueRate > 99.9) {
            System.out.println("   ✅ 唯一性测试通过");
        } else {
            System.out.println("   ⚠️  唯一性需要关注");
        }
        System.out.println();
    }
}
