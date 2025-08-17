package com.example.logindemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.logindemo.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select * from user where phone = #{phone}")
    User findByPhone(String phone);
}
