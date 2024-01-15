package com.baiyi.cratos.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @Author baiyi
 * @Date 2024/1/15 10:02
 * @Version 1.0
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build());
//        return manager;
//    }

}
