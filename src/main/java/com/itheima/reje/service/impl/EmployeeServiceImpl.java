package com.itheima.reje.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reje.entity.Employee;
import com.itheima.reje.mapper.EmployeeMapper;
import com.itheima.reje.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @program: reje
 * @description:
 * @author: 作者名称
 * @date: 2022-05-03 15:25
 **/

@Service
// 实现对应的 service 接口
// 继承MybatisPlus给予的父类 ServiceImpl<mapper类, 实体类> 简化开发
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService{
}
