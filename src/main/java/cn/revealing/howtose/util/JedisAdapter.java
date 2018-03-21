package cn.revealing.howtose.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by GJW on 2017/7/7.
 */
@Component
public class JedisAdapter implements InitializingBean {
    public static final Logger LOGGER = LoggerFactory.getLogger(JedisAdapter.class);
    public static final String host = "140.143.237.210";
    JedisPool pool = null;

    /*public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");
        jedis.set("foo", "bar");
        String value = jedis.get("foo");
    }*/


    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool(host);
    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            LOGGER.error("redis添加错误:" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }

        return 0;
    }



    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            LOGGER.error("redis添加错误:" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }

        return 0;
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            LOGGER.error("redisyi移除错误:" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }

        return 0;
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            LOGGER.error("redis添加错误:" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }

        return false;
    }


    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            LOGGER.error("redis添加错误:" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }

        return 0;
    }

    public List<String> lrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            LOGGER.error("redis添加错误:" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }

        return null;
    }

    public long zadd(String key, double score, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key, score, value);
        } catch (Exception e) {
            LOGGER.error("redis添加错误:" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }

        return 0;
    }

    public List<String>  brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(0, key);
        } catch (Exception e) {
            LOGGER.error("redis添加错误:" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }

        return null;
    }

    public Jedis getJedis() {
        return pool.getResource();
    }
    public Transaction multi(Jedis jedis){
        try {
            return jedis.multi();
        } catch (Exception e) {
            LOGGER.error("发生异常" + e.getMessage());
            return null;
        }
    }

    public List<Object> exec(Transaction tx, Jedis jedis) {
        try {
            return tx.exec();
        } catch (Exception e) {
            LOGGER.error("exec异常：" + e.getMessage());
        } finally {
            if (tx != null) {
                try {
                    tx.close();
                } catch (IOException ioe) {
                    LOGGER.error("io异常" + ioe.getMessage());
                }
            }
            if(jedis != null){
                jedis.close();
            }

        }
        return null;

    }

    public Set<String> zrange(String key, int start, int end){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrange(key, start, end);
        } catch (Exception e) {
            LOGGER.error("redis添加错误:" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }

        return null;
    }

    public Set<String> zrevrange(String key, int start, int end){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            LOGGER.error("redis添加错误:" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }

        return null;
    }

    public long zcard(String key){
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            LOGGER.error("redis添加错误:" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }

        return 0;
    }

    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key, member);
        } catch (Exception e) {
            LOGGER.error("redis添加错误:" + e.getMessage());
        } finally {
            if (jedis != null)
                jedis.close();
        }

        return null;
    }
}
