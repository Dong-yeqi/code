package com.example.realestatetracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 安全配置
 * 
 * ⚠️ 注意：当前配置为开发/毕设环境配置，允许所有请求访问，未实现真正的权限控制。
 * 
 * 生产环境建议修改：
 * 1. 启用 JWT 或 Session 认证机制
 * 2. 配置具体的权限规则，例如：
 *    - /api/admin/** 需要 ADMIN 角色
 *    - /api/public/** 允许匿名访问
 *    - 其他接口需要认证
 * 3. 启用 CSRF 保护（如果使用 Session）
 * 4. 配置 HTTPS 强制重定向
 * 5. 添加请求频率限制
 * 6. 配置安全的 CORS 策略（指定允许的域名）
 * 
 * 示例生产环境配置：
 * <pre>
 * http.authorizeHttpRequests(auth -> auth
 *     .requestMatchers("/api/admin/**").hasRole("ADMIN")
 *     .requestMatchers("/api/public/**").permitAll()
 *     .anyRequest().authenticated()
 * )
 * .oauth2ResourceServer(oauth2 -> oauth2.jwt())
 * .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
 * </pre>
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 1. 关闭 csrf，否则前端 POST/PUT/DELETE 会报 403
        // ⚠️ 生产环境：如果使用 Session，应启用 CSRF 保护
        http.csrf(csrf -> csrf.disable());

        // 2. 允许跨域（跟 WebMvcConfigurer 配合）
        // ⚠️ 生产环境：应配置具体的允许域名，避免使用 *
        http.cors(cors -> {});

        // 3. 放行全部请求（适合毕设，不做真正权限管理）
        // ⚠️ 生产环境：必须配置具体的权限规则，不允许所有请求
        http.authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
        );

        // 4. 禁用 Basic 登录弹框
        http.httpBasic(httpBasic -> httpBasic.disable());

        // 5. 禁用表单登录
        // ⚠️ 生产环境：如果使用表单登录，应启用并配置登录页面
        http.formLogin(form -> form.disable());

        return http.build();
    }
}
