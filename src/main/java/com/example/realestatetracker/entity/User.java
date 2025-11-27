package com.example.realestatetracker.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {

    private Long id;
    private String username;
    private String password;
    private String role;        // ADMIN / USER
    private Integer status;     // 1 启用 0 禁用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
