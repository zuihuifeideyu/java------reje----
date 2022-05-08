package com.itheima.reje.dto;

import com.itheima.reje.entity.Setmeal;
import com.itheima.reje.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    //套餐和菜品关系列表数据
    //套餐中有哪些菜品，每个菜品数量是多少
    private List<SetmealDish> setmealDishes;

    //套餐属于的套餐分类名称
    private String categoryName;
}
