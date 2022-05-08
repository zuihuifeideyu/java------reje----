package com.itheima.reje.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reje.common.R;
import com.itheima.reje.entity.Employee;
import com.itheima.reje.service.EmployeeService;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @program: reje
 * @description:
 * @author: 作者名称
 * @date: 2022-05-03 15:28
 **/


@Controller
@RestController
// 使用restful风格开发
@RequestMapping("/employee")
// 请求的资源位置
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    // 员工登陆
    @PostMapping("/login")
    // post请求映射，"/login"对应url
    // 从前端传来的数据是json格式的
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {


        // 将前端提交过来的明文密码进行MD5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes()); // 对前端传入的密码进行md5加密，用来与数据库中的密码进行匹配

        // 根据用户名查询数据库，构建查询语句
        // 利用MP进行数据库的查询
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        // Employee::getUsername表明是Employee的哪个属性，对应数据库中要查询的表的属性名，employee.getUsername()和eq构成查询条件

        // 调用service利用构建的查询语句进行查询
        // 用户是唯一的所以可以使用getOne
        Employee emp = employeeService.getOne(queryWrapper);

        // 检验是否查询到对应数据以及密码是否正确
        if(emp == null) {
            return R.error("登陆失败");
        }

        if(!emp.getPassword().equals(password)) {
            return R.error("密码错误登陆失败");
        }

        // 查看员工状态是否是可用（可以理解为是否离职或者今天是否上班）
        if(emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        // 登陆成功，将员工id存入session（为了不需要每次登陆页面的时候都需要重新登陆）
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }



    @PostMapping("/logout")
    // 用户退出请求的响应
    public R<String> logout(HttpServletRequest request) {

        // 清理session中保留的员工id
        request.getSession().removeAttribute("employee");
        return R.success("推出成功");

    }


    // 新增员工
    @PostMapping
    // 前端传入的是JSON格式的数据，所以要用 @RequestBody 进行封装
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {

        log.info(employee.toString());

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now()); // 创建时间
        employee.setUpdateTime(LocalDateTime.now()); // 更新时间

        // 通过session获取当前登陆用户的id，设置创建新用户的管理员的id
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        employeeService.save(employee);


        return R.success("新增员工成功");
    }



    @GetMapping("/page")
    // 前端传入的数据不是以JSON形式传入的，而是p-value
    public R<Page> page(int page, int pageSize, String name) {
        // Page对象是Mybatis-plus提供的

        log.info("page {}pageSize {}name {}", page, pageSize, name);

        // 构造分页构造器
        Page pageInfo = new Page(page, pageSize);


        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        // 添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);


        // service层处理服务
        employeeService.page(pageInfo, queryWrapper);
        // 不需要接收值，会自动处理存储到pageInfo中

        return R.success(pageInfo);

    }


    // 通用的update方法，对于所有的update操作都通过这里进行，包括用户状态的修改和用户信息的修改
    @PutMapping
    // 修改用户的状态（是否禁用）
    // js在处理服务器返回的数据时，Long类型的数据会产生丢失的可能，js只能最多处理10位的数据，
    // 所以需要在服务器提交给客户端之前先将Long类型数据转换为String类型数据，防止精度丢失
    public R<String> update(HttpServletRequest servletRequest, @RequestBody Employee employee) {

        //log.info("用户当前的状态是{}", employee.getStatus());

        Long empId = (Long) servletRequest.getSession().getAttribute("employee");
        // 通过session，获取当前登陆用户作为修改信息的用户并存储
        employee.setUpdateUser(empId);

        // 设置修改时间
        employee.setUpdateTime(LocalDateTime.now());

        // 更新数据
        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }



    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    // 路径变量作为参数
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if(employee != null){
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }
}
