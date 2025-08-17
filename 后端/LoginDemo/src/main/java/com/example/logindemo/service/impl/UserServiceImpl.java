package com.example.logindemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.logindemo.domain.User;
import com.example.logindemo.mapper.UserMapper;
import com.example.logindemo.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User findByPhone(String phone) {
        return baseMapper.findByPhone(phone);
    }

    @Override
    public User register(String phone) {
        // 检查用户是否已存在
        User existingUser = findByPhone(phone);
        if (existingUser != null) {
            return existingUser;
        }
        //创建新用户
        User user = new User();
        user.setPhone(phone);
        user.setNickname("用户" + phone);//默认昵称
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setStatus(1);
        baseMapper.insert(user);
        return user;
    }
}
