package com.itheima.reje.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reje.entity.OrderDetail;
import com.itheima.reje.mapper.OrderDetailMapper;
import com.itheima.reje.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}