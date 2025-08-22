package com.baiyi.cratos.common.util;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * ID生成器REST API控制器
 * 
 * 提供HTTP接口用于生成和管理8位随机ID
 * 
 * @author cratos
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/id-generator")
@RequiredArgsConstructor
@Tag(name = "ID生成器", description = "8位随机ID生成相关接口")
public class IdGeneratorController {
    
    private final IdGeneratorService idGeneratorService;
    
    /**
     * 生成单个8位随机ID
     * 
     * @return 生成的ID
     */
    @GetMapping("/generate")
    @Operation(summary = "生成单个ID", description = "生成一个8位随机ID（数字+小写字母）")
    public ResponseEntity<Map<String, Object>> generateId() {
        String id = idGeneratorService.generateId();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("id", id);
        response.put("length", id.length());
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 生成单个8位安全随机ID
     * 
     * @return 生成的安全ID
     */
    @GetMapping("/generate/secure")
    @Operation(summary = "生成安全ID", description = "生成一个8位安全随机ID（使用SecureRandom）")
    public ResponseEntity<Map<String, Object>> generateSecureId() {
        String id = idGeneratorService.generateSecureId();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("id", id);
        response.put("length", id.length());
        response.put("secure", true);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 批量生成8位随机ID
     * 
     * @param count 生成数量
     * @return 生成的ID数组
     */
    @GetMapping("/generate/batch")
    @Operation(summary = "批量生成ID", description = "批量生成指定数量的8位随机ID")
    public ResponseEntity<Map<String, Object>> generateIds(
            @Parameter(description = "生成数量", example = "10")
            @RequestParam(defaultValue = "10") int count) {
        
        if (count <= 0 || count > 1000) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Count must be between 1 and 1000");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        String[] ids = idGeneratorService.generateIds(count);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("ids", ids);
        response.put("count", ids.length);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 批量生成8位安全随机ID
     * 
     * @param count 生成数量
     * @return 生成的安全ID数组
     */
    @GetMapping("/generate/batch/secure")
    @Operation(summary = "批量生成安全ID", description = "批量生成指定数量的8位安全随机ID")
    public ResponseEntity<Map<String, Object>> generateSecureIds(
            @Parameter(description = "生成数量", example = "10")
            @RequestParam(defaultValue = "10") int count) {
        
        if (count <= 0 || count > 1000) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", "Count must be between 1 and 1000");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
        String[] ids = idGeneratorService.generateSecureIds(count);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("ids", ids);
        response.put("count", ids.length);
        response.put("secure", true);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 验证ID格式
     * 
     * @param id 待验证的ID
     * @return 验证结果
     */
    @GetMapping("/validate")
    @Operation(summary = "验证ID格式", description = "验证给定字符串是否为有效的8位ID格式")
    public ResponseEntity<Map<String, Object>> validateId(
            @Parameter(description = "待验证的ID", example = "abc12345")
            @RequestParam String id) {
        
        boolean isValid = idGeneratorService.isValidId(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("id", id);
        response.put("valid", isValid);
        response.put("length", id != null ? id.length() : 0);
        response.put("expectedLength", idGeneratorService.getIdLength());
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取生成统计信息
     * 
     * @return 统计信息
     */
    @GetMapping("/stats")
    @Operation(summary = "获取统计信息", description = "获取ID生成器的统计信息")
    public ResponseEntity<Map<String, Object>> getStats() {
        IdGeneratorService.GenerationStats stats = idGeneratorService.getStats();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("stats", Map.of(
                "totalGenerated", stats.getTotalGenerated(),
                "totalSecureGenerated", stats.getTotalSecureGenerated(),
                "grandTotal", stats.getGrandTotal(),
                "characterSet", stats.getCharacterSet(),
                "characterSetLength", stats.getCharacterSet().length(),
                "idLength", stats.getIdLength(),
                "totalCombinations", stats.getTotalCombinations()
        ));
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 重置统计信息
     * 
     * @return 重置结果
     */
    @PostMapping("/stats/reset")
    @Operation(summary = "重置统计信息", description = "重置ID生成器的统计计数器")
    public ResponseEntity<Map<String, Object>> resetStats() {
        idGeneratorService.resetStats();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Statistics reset successfully");
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取工具信息
     * 
     * @return 工具信息
     */
    @GetMapping("/info")
    @Operation(summary = "获取工具信息", description = "获取ID生成器的基本信息")
    public ResponseEntity<Map<String, Object>> getInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("info", Map.of(
                "name", "IdGenerator",
                "version", "1.0",
                "description", "8位随机ID生成工具",
                "characterSet", idGeneratorService.getCharacterSet(),
                "characterSetLength", idGeneratorService.getCharacterSet().length(),
                "idLength", idGeneratorService.getIdLength(),
                "totalCombinations", idGeneratorService.getTotalCombinations(),
                "features", new String[]{
                        "快速生成模式",
                        "安全生成模式", 
                        "批量生成",
                        "格式验证",
                        "统计监控"
                }
        ));
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
}
