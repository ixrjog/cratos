package com.baiyi.cratos.configuration;

import com.baiyi.cratos.secutity.JasyptPasswordEncoder;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                        // 明确允许静态资源访问
                        .requestMatchers("/", "/index.html", "/static/**", "/*.html", "/*.css", "/*.js", "/*.png", "/*.jpg", "/*.gif", "/*.ico")
                        .permitAll()
                        .requestMatchers(new AntPathRequestMatcher("**"))
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .cors(Customizer.withDefaults())
                //.csrf(csrf -> csrf.disable()) // 禁用CSRF以便测试
                .authenticationManager(authManager);
        return http.build();
    }

}
