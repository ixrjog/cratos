package com.baiyi.cratos.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Spring Boot集成版本的ID生成器服务
 * 
 * 功能特性:
 * - 生成8位长度的随机ID
 * - 字符集包含数字(0-9)和小写字母(a-z)，共36个字符
 * - 支持高并发场景
 * - 提供统计信息和监控能力
 * - 集成Spring Boot生态
 * 
 * @author cratos
 * @version 1.0
 */
@Slf4j
@Service
public class IdGeneratorService {
    
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
     * 安全随机数生成器
     */
    private final SecureRandom secureRandom = new SecureRandom();
    
    /**
     * 生成计数器
     */
    private final AtomicLong generateCounter = new AtomicLong(0);
    
    /**
     * 安全生成计数器
     */
    private final AtomicLong secureGenerateCounter = new AtomicLong(0);
    
    /**
     * 服务初始化
     */
    @PostConstruct
    public void init() {
        log.info("IdGeneratorService initialized - Character set: {}, ID length: {}, Total combinations: {}", 
                CHARACTERS, ID_LENGTH, getTotalCombinations());
    }
    
    /**
     * 生成8位随机ID（快速模式）
     * 
     * @return 8位随机ID字符串
     */
    public String generateId() {
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        
        for (int i = 0; i < ID_LENGTH; i++) {
            int index = random.nextInt(CHARSET_SIZE);
            sb.append(CHARACTERS.charAt(index));
        }
        
        String id = sb.toString();
        generateCounter.incrementAndGet();
        
        if (log.isDebugEnabled()) {
            log.debug("Generated ID: {}", id);
        }
        
        return id;
    }
    
    /**
     * 生成8位随机ID（安全模式）
     * 
     * @return 8位随机ID字符串
     */
    public String generateSecureId() {
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        
        for (int i = 0; i < ID_LENGTH; i++) {
            int index = secureRandom.nextInt(CHARSET_SIZE);
            sb.append(CHARACTERS.charAt(index));
        }
        
        String id = sb.toString();
        secureGenerateCounter.incrementAndGet();
        
        if (log.isDebugEnabled()) {
            log.debug("Generated secure ID: {}", id);
        }
        
        return id;
    }
    
    /**
     * 批量生成8位随机ID
     * 
     * @param count 生成数量
     * @return ID数组
     */
    public String[] generateIds(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }
        
        String[] ids = new String[count];
        for (int i = 0; i < count; i++) {
            ids[i] = generateId();
        }
        
        log.info("Batch generated {} IDs", count);
        return ids;
    }
    
    /**
     * 批量生成8位随机ID（安全模式）
     * 
     * @param count 生成数量
     * @return ID数组
     */
    public String[] generateSecureIds(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Count must be positive");
        }
        
        String[] ids = new String[count];
        for (int i = 0; i < count; i++) {
            ids[i] = generateSecureId();
        }
        
        log.info("Batch generated {} secure IDs", count);
        return ids;
    }
    
    /**
     * 验证ID格式是否正确
     * 
     * @param id 待验证的ID
     * @return true表示格式正确，false表示格式错误
     */
    public boolean isValidId(String id) {
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
     * 获取生成统计信息
     * 
     * @return 统计信息对象
     */
    public GenerationStats getStats() {
        return GenerationStats.builder()
                .totalGenerated(generateCounter.get())
                .totalSecureGenerated(secureGenerateCounter.get())
                .characterSet(CHARACTERS)
                .idLength(ID_LENGTH)
                .totalCombinations(getTotalCombinations())
                .build();
    }
    
    /**
     * 重置统计计数器
     */
    public void resetStats() {
        generateCounter.set(0);
        secureGenerateCounter.set(0);
        log.info("Generation statistics reset");
    }
    
    /**
     * 获取字符集信息
     * 
     * @return 字符集字符串
     */
    public String getCharacterSet() {
        return CHARACTERS;
    }
    
    /**
     * 获取ID长度
     * 
     * @return ID长度
     */
    public int getIdLength() {
        return ID_LENGTH;
    }
    
    /**
     * 获取理论上的总组合数
     * 
     * @return 总组合数 (36^8)
     */
    public long getTotalCombinations() {
        return (long) Math.pow(CHARSET_SIZE, ID_LENGTH);
    }
    
    /**
     * 生成统计信息类
     */
    public static class GenerationStats {
        private final long totalGenerated;
        private final long totalSecureGenerated;
        private final String characterSet;
        private final int idLength;
        private final long totalCombinations;
        
        private GenerationStats(Builder builder) {
            this.totalGenerated = builder.totalGenerated;
            this.totalSecureGenerated = builder.totalSecureGenerated;
            this.characterSet = builder.characterSet;
            this.idLength = builder.idLength;
            this.totalCombinations = builder.totalCombinations;
        }
        
        public static Builder builder() {
            return new Builder();
        }
        
        // Getters
        public long getTotalGenerated() { return totalGenerated; }
        public long getTotalSecureGenerated() { return totalSecureGenerated; }
        public String getCharacterSet() { return characterSet; }
        public int getIdLength() { return idLength; }
        public long getTotalCombinations() { return totalCombinations; }
        public long getGrandTotal() { return totalGenerated + totalSecureGenerated; }
        
        @Override
        public String toString() {
            return String.format("GenerationStats{total=%d, secure=%d, grandTotal=%d, charset='%s', length=%d, combinations=%d}",
                    totalGenerated, totalSecureGenerated, getGrandTotal(), characterSet, idLength, totalCombinations);
        }
        
        public static class Builder {
            private long totalGenerated;
            private long totalSecureGenerated;
            private String characterSet;
            private int idLength;
            private long totalCombinations;
            
            public Builder totalGenerated(long totalGenerated) {
                this.totalGenerated = totalGenerated;
                return this;
            }
            
            public Builder totalSecureGenerated(long totalSecureGenerated) {
                this.totalSecureGenerated = totalSecureGenerated;
                return this;
            }
            
            public Builder characterSet(String characterSet) {
                this.characterSet = characterSet;
                return this;
            }
            
            public Builder idLength(int idLength) {
                this.idLength = idLength;
                return this;
            }
            
            public Builder totalCombinations(long totalCombinations) {
                this.totalCombinations = totalCombinations;
                return this;
            }
            
            public GenerationStats build() {
                return new GenerationStats(this);
            }
        }
    }
}
