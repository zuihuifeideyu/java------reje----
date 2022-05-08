package com.itheima.reje.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reje.common.CustomException;
import com.itheima.reje.entity.Category;
import com.itheima.reje.entity.Dish;
import com.itheima.reje.entity.Setmeal;
import com.itheima.reje.mapper.CategoryMapper;
import com.itheima.reje.service.CategoryService;
import com.itheima.reje.service.DishService;
import com.itheima.reje.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: reje
 * @description: 菜品新增服务实现类
 * @author: 作者名称
 * @date: 2022-05-05 10:46
 **/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    // 利用 DishService 和 SetmealService 查看当前的菜品分类id在别的表中是否存在菜品等
    // 如果存在就不能直接删除，并抛出一个异常
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        //获取含有该分类id的菜品的数量
        int count1 = (int) dishService.count(dishLambdaQueryWrapper);

        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        if(count1 > 0){
            //已经关联菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        //获取含有该分类id的套餐的数量
        int count2 = (int) setmealService.count();
        if(count2 > 0){
            //已经关联套餐，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        // 正常删除分类
        // 调用父类的方法
        super.removeById(id);
    }
}
