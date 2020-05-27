package com.example.springbootredislock.springbootredislock.config;

/**
 * @Description: redisson 单节点配置
 * @Author: zhoufu
 * @Date: 14:07 2020/5/26
 */

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * Redisson配置类
 */
/**
 * 注解介绍：
 *  从Spring3.0，@Configuration用于定义配置类，可替换xml配置文件，被注解的类内部包含有一个或多个被@Bean注解的方法
 *  这些方法将会被AnnotationConfigApplicationContext或AnnotationConfigWebApplicationContext类进行扫描，
 *  并用于构建bean定义，初始化Spring容器。
 *  此注解标注在类上，相当于把该类作为spring的xml配置文件中的<beans>，作用是：配置spring容器(应用上下文)
 */
@Configuration
public class RedissonConfig {
    @Value("${spring.redis.host:}")
    private String host;

    @Value("${spring.redis.port:}")
    private String port;

    @Value("${spring.redis.password:}")
    private String password;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        //单节点
        config.useSingleServer().setAddress("redis://" + host + ":" + port);
        if (StringUtils.isEmpty(password)){
            config.useSingleServer().setPassword(null);
        }else {
            config.useSingleServer().setPassword(password);
        }

        //添加主从配置
//        config.useMasterSlaveServers().setMasterAddress("").setPassword("").addSlaveAddress(new String[]{"", ""});
        //集群模式配置  setScanInterval()扫描间隔时间，单位是毫秒，//可以用"rediss://"来启用SSL连接
//        config.useClusterServers().setScanInterval(2000).addNodeAddress("redis://172.0.0.1:7000", "redis://172.0.0.1:7001").addNodeAddress("redis://172.0.0.1:7002");
        return Redisson.create(config);
    }
}
