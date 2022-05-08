package com.itheima.reje.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reje.entity.User;
import com.itheima.reje.mapper.UserMapper;
import com.itheima.reje.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService{
}
