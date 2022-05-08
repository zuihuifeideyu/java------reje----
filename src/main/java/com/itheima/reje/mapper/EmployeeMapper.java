package com.itheima.reje.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reje.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @program: reje
 * @description:
 * @author: 作者名称
 * @date: 2022-05-03 15:21
 **/

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
