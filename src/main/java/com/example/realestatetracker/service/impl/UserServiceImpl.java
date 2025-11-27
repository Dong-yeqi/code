package com.example.realestatetracker.service.impl;

import com.example.realestatetracker.entity.User;
import com.example.realestatetracker.mapper.UserMapper;
import com.example.realestatetracker.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
}
