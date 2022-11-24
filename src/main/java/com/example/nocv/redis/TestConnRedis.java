package com.example.nocv.redis;

import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class TestConnRedis {
    public static void main(String[] args) {
        //连接到redis服务
        Jedis jedis = new Jedis("192.168.157.128");
        //如果Redis服务设置了密码，需要下面这行，没有就不需要
        //jedis.auth("123456")
//        System.out.println("连接成功");
        //查看服务是否运行
        System.out.println("服务正在运行"+jedis.ping());

        //String
//        jedis.set("nocv","zszs");
//        jedis.get("nocv");


       /* //list
        jedis.lpush("sitr-list","1");
        jedis.lpush("sitr-list","2");
        jedis.lpush("sitr-list","3");
        //获取存储的数据并输出
        List<String> list = jedis.lrange("sitr-list", 0, 2);
        for (int i = 0; i < list.size(); i++) {
            System.out.println("列表项为："+list.get(i));
        }*/

/*        //无序集合
        jedis.sadd("nocvSet","111");
        jedis.sadd("nocvSet","222");
        jedis.sadd("nocvSet","333");
        jedis.sadd("nocvSet","333");

        Set<String> list = jedis.smembers("nocvSet");
        for (String s : list) {
            System.out.println(s);
        }*/

        //SortedSet 有序集合-排名，排序，排序吗】
       /* jedis.zadd("nocvZset",102,"111");
        jedis.zadd("nocvZset",201,"222");
        jedis.zadd("nocvZset",32,"333");
        jedis.zadd("nocvZset",42,"444");
        jedis.zadd("nocvZset",52,"555");

        Set<String> nocvZset = jedis.zrange("nocvZset", 0, -1);
        for (String s : nocvZset) {
            System.out.println(s);
        }

        Long nocvZset1 = jedis.zrank("nocvZset", "333");
        System.out.println(nocvZset1);
        System.out.println("---------");
        //范围指定分数区间内元素个数,然后将这些数据在zset中删除。
        Long nocvZset2 = jedis.zremrangeByScore("nocvZset", 40, 100);
        System.out.println(nocvZset2);

        //返回有序集中，成员的分数值
        Double nocvZset3 = jedis.zscore("nocvZset", "333");
        System.out.println(nocvZset3);*/
    }
}
