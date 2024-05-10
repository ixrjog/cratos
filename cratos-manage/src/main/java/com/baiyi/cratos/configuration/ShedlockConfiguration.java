package com.baiyi.cratos.configuration;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/10 上午9:50
 * &#064;Version 1.0
 */
@Configuration
@EnableSchedulerLock(defaultLockAtMostFor = "10M")
public class ShedlockConfiguration {

    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(
                JdbcTemplateLockProvider.Configuration.builder()
                        .withJdbcTemplate(new JdbcTemplate(dataSource))
                        // Works on Postgres, MySQL, MariaDb, MS SQL, Oracle, DB2, HSQL and H2
                        .usingDbTime()
                        .build()
        );
    }

}