package com.ifoods.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.JedisPoolConfig;

/**
 * 
 * @author zhenghui.li
 * @date 2018年5月12日
 */

@Configuration
public class RedisSessionConfig {

    @Value("${redis-session.host}")
    private String host;

    @Value("${redis-session.port}")
    private Integer port;

    @Value("${redis-session.db}")
    private Integer db;

    @Value("${redis-session.pool.max-active}")
    private Integer maxActive;

    @Value("${redis-session.pool.max-wait}")
    private Integer maxWait;

    @Value("${redis-session.pool.max-idle}")
    private Integer maxIdle;

    @Value("${redis-session.pool.min-idle}")
    private Integer minIdle;

    @Bean(destroyMethod = "destroy")
    public RedisClient redisSession() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxTotal(maxActive);
        return new RedisClient(host,port,null,config,db);
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setMaxTotal(maxActive);
        factory.setHostName(host);
        factory.setPoolConfig(config);
        factory.setDatabase(db);
        factory.setPort(port);
        factory.setTimeout(10000); //设置连接超时时间
        
        redisTemplate(factory);
        
        return factory;
    }
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        setSerializer(template); //设置序列化工具
        template.afterPropertiesSet();
        return template;
    }
    
    /*@Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        // Number of seconds before expiration. Defaults to unlimited (0)
        cacheManager.setDefaultExpiration(1000); //设置key-value超时时间
        return cacheManager;
    }*/

    private void setSerializer(StringRedisTemplate template) {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
    }

}
