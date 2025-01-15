package com.baiyi.cratos.common.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2024/1/23 11:24
 * @Version 1.0
 */
@Configuration
@EnableCaching
public class CachingConfiguration {

    public interface RepositoryName {
        String LONG_TERM = "CR30d:";
        String TEMPORARY = "CR5s:";
        String VERY_SHORT = "CR10m:";
        String SHORT_TERM = "CR1h:";
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CacheRepository {
        public static final CacheRepository LONG_TERM = CacheRepository.builder()
                .name(RepositoryName.LONG_TERM)
                .ttl((Duration.ofDays(30)))
                .build();
        public static final CacheRepository SHORT_TERM = CacheRepository.builder()
                .name(RepositoryName.SHORT_TERM)
                .ttl((Duration.ofHours(1)))
                .build();
        public static final CacheRepository TEMPORARY = CacheRepository.builder()
                .name(RepositoryName.TEMPORARY)
                .ttl((Duration.ofSeconds(10)))
                .build();
        public static final CacheRepository VERY_SHORT = CacheRepository.builder()
                .name(RepositoryName.VERY_SHORT)
                .ttl((Duration.ofMinutes(10)))
                .build();
        private String name;
        private Duration ttl;
    }

    private static final List<CacheRepository> REPOS = Lists.newArrayList(CacheRepository.LONG_TERM,
            CacheRepository.TEMPORARY, CacheRepository.VERY_SHORT, CacheRepository.SHORT_TERM);

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // 设置一个初始化的缓存空间set集合
        Set<String> cacheNames = REPOS.stream()
                .map(CacheRepository::getName)
                .collect(Collectors.toSet());
        // 使用自定义的缓存配置初始化一个cacheManager
        return RedisCacheManager.builder(factory)
                // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，
                // 再初始化相关的配置
                .initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(getConfigMap())
                .build();
    }

    private Map<String, RedisCacheConfiguration> getConfigMap() {
        Map<String, RedisCacheConfiguration> configMap = Maps.newHashMap();
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        // 设置缓存的默认过期时间，也是使用Duration设置
        config.entryTtl(Duration.ofMinutes(1))
                // 不缓存空值
                .disableCachingNullValues();
        REPOS.forEach(e -> configMap.put(e.getName(), config.entryTtl(e.getTtl())));
        return configMap;
    }

    // ---------------自定义配置项---------------

    /**
     * retemplate相关配置
     *
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);

        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(om,
                Object.class);

        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());
        // 值采用json序列化
        template.setValueSerializer(jackson2JsonRedisSerializer);

        // 设置hash key 和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();

        return template;
    }

    /**
     * 对hash类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 对redis字符串类型数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 对链表类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 对无序集合类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

}
