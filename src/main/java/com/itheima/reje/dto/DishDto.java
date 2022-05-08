package com.itheima.reje.dto;

import com.itheima.reje.entity.Dish;
import com.itheima.reje.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {

    // 存储一系列的菜品，口味等信息
    private List<DishFlavor> flavors = new ArrayList<>();

    // 存储菜品类别名称
    private String categoryName;

    // 几份
    private Integer copies;
}
