package com.itheima.reje.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reje.common.CustomException;
import com.itheima.reje.dto.SetmealDto;
import com.itheima.reje.entity.Setmeal;
import com.itheima.reje.entity.SetmealDish;
import com.itheima.reje.mapper.SetmealMapper;
import com.itheima.reje.service.SetmealDishService;
import com.itheima.reje.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper,Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息，操作setmeal，执行insert操作
        this.save(setmealDto);

        //获取前端传来的套餐中菜品信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        //对套餐中的菜品信息进行处理，添加这些属于哪个套餐id
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId()); // 获取生成的套餐的id并赋值到套餐内菜品的SetmealId
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联信息（套餐中有哪些菜，每道菜数量等信息），操作setmeal_dish,执行insert操作
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐，同时需要删除套餐和菜品的关联数据
     * @param ids
     */
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in (1,2,3) and status = 1
        //删除一些套餐
        //查询套餐状态，确定是否可用删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = (int) this.count(queryWrapper);
        if(count > 0){
            //如果不能删除，抛出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        //如果可以删除，先删除套餐表中的数据---setmeal
        //删除指定的这些id的套餐
        this.removeByIds(ids);

        //delete from setmeal_dish where setmeal_id in (1,2,3)
        //套餐表关联的表中的一些数据同样进行删除
        //查询条件构造
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        //删除关系表中的数据----setmeal_dish
        setmealDishService.remove(lambdaQueryWrapper);
        //setmealDishService.removeByIds(ids);
        //不能直接用removeByIds，因为setmealId不是setmealDish的主键
    }
}
