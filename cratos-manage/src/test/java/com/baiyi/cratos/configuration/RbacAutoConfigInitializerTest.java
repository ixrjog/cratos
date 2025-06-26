package com.baiyi.cratos.configuration;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * RbacAutoConfigInitializer 测试类
 */
class RbacAutoConfigInitializerTest {

    @Test
    void testConfigurationResult() {
        RbacAutoConfigInitializer.ConfigurationResult result = new RbacAutoConfigInitializer.ConfigurationResult();
        
        // 测试初始值
        assertEquals(0, result.getProcessedControllers());
        assertEquals(0, result.getCreatedGroups());
        assertEquals(0, result.getCreatedResources());
        
        // 测试增量方法
        result.incrementProcessedControllers();
        result.incrementCreatedGroups();
        result.incrementCreatedResources();
        
        assertEquals(1, result.getProcessedControllers());
        assertEquals(1, result.getCreatedGroups());
        assertEquals(1, result.getCreatedResources());
    }

    @Test
    void testControllerMethodMappingValidation() {
        // 测试有效的映射
        RbacAutoConfigInitializer.ControllerMethodMapping validMapping = 
            RbacAutoConfigInitializer.ControllerMethodMapping.builder()
                .tag("TestController")
                .requestMethod("POST")
                .methodValue("/test")
                .build();
        
        assertTrue(validMapping.isValid());
        
        // 测试无效的映射 - 缺少tag
        RbacAutoConfigInitializer.ControllerMethodMapping invalidMapping1 = 
            RbacAutoConfigInitializer.ControllerMethodMapping.builder()
                .requestMethod("POST")
                .methodValue("/test")
                .build();
        
        assertFalse(invalidMapping1.isValid());
        
        // 测试无效的映射 - 缺少requestMethod
        RbacAutoConfigInitializer.ControllerMethodMapping invalidMapping2 = 
            RbacAutoConfigInitializer.ControllerMethodMapping.builder()
                .tag("TestController")
                .methodValue("/test")
                .build();
        
        assertFalse(invalidMapping2.isValid());
        
        // 测试无效的映射 - 缺少methodValue
        RbacAutoConfigInitializer.ControllerMethodMapping invalidMapping3 = 
            RbacAutoConfigInitializer.ControllerMethodMapping.builder()
                .tag("TestController")
                .requestMethod("POST")
                .build();
        
        assertFalse(invalidMapping3.isValid());
    }
}
