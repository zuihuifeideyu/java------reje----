package com.itheima.reje.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reje.common.R;
import com.itheima.reje.entity.Category;
import com.itheima.reje.entity.Employee;
import com.itheima.reje.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.List;

/**
 * @program: reje
 * @description: 菜品新增controller类
 * @author: 作者名称
 * @date: 2022-05-05 10:50
 **/

@Controller
@RestController

@RequestMapping("/category")
// 服务请求匹配，对应url
@Slf4j

public class CategoryController {

    // 获取Category服务类调用服务
    @Autowired
    private CategoryService categoryService;


    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    // 获取前端发送来的JSON数据，解析并加入数据库
    public R<String> save(@RequestBody Category category) {
        log.info(category.toString());

        categoryService.save(category);

        return R.success("添加成功");
    }


    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    // 菜品分类查询
    public R<Page> page(int page, int pageSize) {

        // 分页构造器
        Page<Category> categoryPage = new Page<>(page, pageSize);

        // 查询条件构造
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 根据sort进行排序
        queryWrapper.orderByAsc(Category::getSort);

        // 分页查询
        // 查询到的信息会自动填充到 categoryPage（Page对象）中
        categoryService.page(categoryPage, queryWrapper);

        return R.success(categoryPage);
    }



    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long id){
        log.info("删除分类，id为：{}",id);

        //categoryService.removeById(id);

        // 考虑到表之间的关联自定义删除方法
        categoryService.remove(id);


        return R.success("分类信息删除成功");
    }

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息：{}",category);

        categoryService.updateById(category);

        return R.success("修改分类信息成功");
    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    // 为下拉框提供数据显示
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }


}
