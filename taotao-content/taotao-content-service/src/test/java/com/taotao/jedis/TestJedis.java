package com.taotao.jedis;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TestJedis {

    @Test
    public void testJedis(){
        //创建jedis对象，需要指定Redis服务的IP和端口号
        Jedis jedis = new Jedis("192.168.173.148", 6379);
        //直接操作数据库
        jedis.set("jedis-key", "hello jedis!");
        //获取数据
        String result = jedis.get("jedis-key");
        System.out.println(result);
        //关闭jedis
        jedis.close();
    }

    /**
     * 上面的测试类方法由于每次都创建一个Jedis对象，这是不合理的，因为这样很耗资源，因此我们使用数据库连接池来处理，需要连接的时候从数据库连接池中去获取，用完连接记得关闭，这样连接池才能将资源回收。如下所示。
     */

    @Test
    public void testJedisPool(){
        //创建一个数据库连接池对象（单例，即一个系统共用一个连接池），需要指定服务的IP和端口号
        JedisPool jedisPool = new JedisPool("192.168.173.148", 6379);
        //从连接池中获得连接
        Jedis jedis = jedisPool.getResource();
        //使用jedis操作数据库（方法级别，就是说只是在该方法中使用，用完就关闭）
        String result = jedis.get("jedis-key");
        System.out.println(result);
        //用完之后关闭jedis连接
        jedis.close();
        //系统关闭前先关闭数据库连接池
        jedisPool.close();
    }


}

