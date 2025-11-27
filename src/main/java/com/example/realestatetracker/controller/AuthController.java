package com.example.realestatetracker.controller;

import com.example.realestatetracker.common.api.CommonResult;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单登录接口示例：
 * 当前项目真正的权限控制还是用 Spring Security 的内存用户（admin/123456），
 * 这个接口主要是给前端 Login.vue 用，返回一个“假 token”，保证前后端联调不报错。
 */
@RestController
@RequestMapping("/api/admin")
public class AuthController {

    /**
     * 登录请求参数
     */
    public static class LoginRequest {

        @NotBlank(message = "用户名不能为空")
        private String username;

        @NotBlank(message = "密码不能为空")
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @PostMapping("/login")
    public CommonResult<Map<String, String>> login(@RequestBody LoginRequest req) {
        // 简单判断：用户名密码等于 admin/123456 就认为登录成功
        if ("admin".equals(req.getUsername()) && "123456".equals(req.getPassword())) {
            Map<String, String> data = new HashMap<>();
            data.put("token", "fake-token");
            data.put("username", req.getUsername());
            return CommonResult.success(data);
        }
        return CommonResult.failed("用户名或密码错误");
    }
}
