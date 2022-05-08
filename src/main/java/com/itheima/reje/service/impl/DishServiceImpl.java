package com.itheima.reje.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reje.dto.DishDto;
import com.itheima.reje.entity.Dish;
import com.itheima.reje.entity.DishFlavor;
import com.itheima.reje.mapper.DishMapper;
import com.itheima.reje.service.DishFlavorService;
import com.itheima.reje.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     */
    @Override
    @Transactional
    // 涉及到两个表的操作时需要进行事务管理，防止中途出现异常导致无法回滚
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish，会自动过滤多余的信息
        this.save(dishDto);

        //菜品id，save过后自动生成了dishId
        Long dishId = dishDto.getId();

        //菜品口味
        // 获取所有的菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        // 用map表达式处理每个flavors，为每一个设置dishId
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList()); // 重新转换为list数据

        //保存菜品口味数据到菜品口味表dish_flavor，Batch是批量保存
        dishFlavorService.saveBatch(flavors);

    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        //填装口味数据
        dishDto.setFlavors(flavors);

        // 最后返回的都是dishDto数据
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);

        //清理当前菜品对应的所有口味数据---dish_flavor表的delete操作
        //因为一个菜品对应多个口味数据，所以不能直接使用update方法
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        //添加当前提交过来的口味数据---dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}
