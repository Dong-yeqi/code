package com.example.realestatetracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 毕设专用安全配置（无鉴权 + 允许跨域 + 不拦截后端接口）
 * 适用于：前后端分离、前端 Vue 直接访问 Spring Boot API。
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 1. 关闭 csrf，否则前端 POST/PUT/DELETE 会报 403
        http.csrf(csrf -> csrf.disable());

        // 2. 允许跨域（跟 WebMvcConfigurer 配合）
        http.cors(cors -> {});

        // 3. 放行全部请求（适合毕设，不做真正权限管理）
        http.authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
        );

        // 4. 禁用 Basic 登录弹框
        http.httpBasic(httpBasic -> httpBasic.disable());

        // 5. 禁用表单登录
        http.formLogin(form -> form.disable());

        return http.build();
    }
}
