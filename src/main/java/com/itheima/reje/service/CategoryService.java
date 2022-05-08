package com.itheima.reje.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reje.entity.Category;
import com.itheima.reje.mapper.CategoryMapper;

/**
 * @program: reje
 * @description:
 * @author: 作者名称
 * @date: 2022-05-05 10:46
 **/

// IService提供的是最基本的服务语句
public interface CategoryService extends IService<Category> {

    // 针对跨表之间的删除操作
    public void remove(Long id);

}
