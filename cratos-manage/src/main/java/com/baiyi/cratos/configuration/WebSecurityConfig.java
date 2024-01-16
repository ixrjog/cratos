package com.baiyi.cratos.configuration;

import com.baiyi.cratos.secutity.JasyptPasswordEncoder;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author baiyi
 * @Date 2024/1/15 10:02
 * @Version 1.0
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Resource
    private JasyptPasswordEncoder jasyptPasswordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return jasyptPasswordEncoder;
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withDefaultPasswordEncoder().username("user").password("password").roles("USER").build());
//        return manager;
//    }

}
