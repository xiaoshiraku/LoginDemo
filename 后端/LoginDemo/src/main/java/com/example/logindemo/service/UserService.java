package com.example.logindemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.logindemo.domain.User;

public interface UserService extends IService<User> {
    User findByPhone(String phone);

    User register(String phone);
}
