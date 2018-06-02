package com.ifoods.config.redis;

import java.util.Set;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

public class RedisClient {

    private int timeout = 5000;

    protected Pool<Jedis> jedisPool;

    public RedisClient(String host, Integer port, String password, int database) {
        this.jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout, password, database);
    }

    public RedisClient(String host, Integer port, String password, JedisPoolConfig poolConfig, int database) {
        this.jedisPool = new JedisPool(poolConfig, host, port, timeout, password, database);
    }

    public RedisClient(String host, Integer port, String password, JedisPoolConfig poolConfig, String masterName, Set<String> sentinels, int database) {
        this.jedisPool = new JedisSentinelPool(masterName, sentinels, poolConfig, timeout, password, database);
    }

    public void destroy() {
        jedisPool.destroy();
    }

    /**
     * @param key
     * @return
     * @see Jedis#exists(String)
     */
    public boolean exists(String key) {
        checkKey(key);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            return jedis.exists(key);
        } finally {
            if (StringUtils.isEmpty(jedis)) {
                jedis.close();
            }
        }
    }

    /**
     * @param key
     * @return
     */
    public Long incr(String key) {
        checkKey(key);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.incr(key);

        } finally {
            if (StringUtils.isEmpty(jedis)) {
                jedis.close();
            }
        }
    }

    public Long hset(String key, String field, Object value) {
        checkKey(key);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String valueJson = JSON.toJSONString(value, SerializerFeature.WriteClassName);

            return jedis.hset(key, field, valueJson);
        } finally {
            if (StringUtils.isEmpty(jedis)) {
                jedis.close();
            }
        }
    }

    public Object hget(String key, String field) {
        checkKey(key);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String valueJson = jedis.hget(key, field);

            return JSON.parse(valueJson);
        } finally {
            if (StringUtils.isEmpty(jedis)) {
                jedis.close();
            }
        }
    }

    public Long expire(String key, int seconds) {
        checkKey(key);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            return jedis.expire(key, seconds);
        } finally {
            if (StringUtils.isEmpty(jedis)) {
                jedis.close();
            }
        }
    }

    public String set(String key, String value) {
        checkKey(key);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String valueJson;
            valueJson = JSON.toJSONString(value, SerializerFeature.WriteClassName);

            return jedis.set(key, valueJson);
        } finally {
            if (StringUtils.isEmpty(jedis)) {
                jedis.close();
            }
        }
    }

    public String setEx(String key, int seconds, Object value) {
        checkKey(key);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String valueJson;
            valueJson = JSON.toJSONString(value, SerializerFeature.WriteClassName);

            return jedis.setex(key, seconds, valueJson);
        } finally {
            if (StringUtils.isEmpty(jedis)) {
                jedis.close();
            }
        }
    }

    public Object get(String key) {

        checkKey(key);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String valueJson = jedis.get(key);

            return JSON.parse(valueJson);
        } finally {
            if (StringUtils.isEmpty(jedis)) {
                jedis.close();
            }
        }
    }

    public Long del(String key) {
        checkKey(key);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();

            return jedis.del(key);
        } finally {
            if (StringUtils.isEmpty(jedis)) {
                jedis.close();
            }
        }
    }

    private void checkKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Cache key is null or not a length of 0.");
        }
    }
}
