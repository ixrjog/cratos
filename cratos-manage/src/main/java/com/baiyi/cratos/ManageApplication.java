package com.baiyi.cratos;


import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.slf4j.Logger;


/**
 * {@code @Author} baiyi
 * {@code @Date} 2023/3/31 14:39
 * {@code @Version} 1.0
 */
@EnableTransactionManagement
@SpringBootApplication(exclude = { SecurityFilterAutoConfiguration.class})
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableScheduling
//@EnableSchedulerLock(defaultLockAtMostFor = "10m")
@EnableCaching
@EnableAsync
@EnableRetry
@ComponentScan(basePackages = "com.baiyi")
public class ManageApplication {

    private static final Logger log = LoggerFactory.getLogger(ManageApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ManageApplication.class, args);
        log.info("Cratos <Spring Boot {}>", SpringBootVersion.getVersion());
      //  System.setProperty("druid.mysql.usePingMethod", "false");
    }

}