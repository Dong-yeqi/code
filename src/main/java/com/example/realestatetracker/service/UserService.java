package com.example.realestatetracker.service;

import com.example.realestatetracker.entity.User;

public interface UserService {

    User getByUsername(String username);
}
