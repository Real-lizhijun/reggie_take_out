package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.pojo.Orders;

public interface OrderService extends IService<Orders> {

    //用户下单
    void submit(Orders orders);
}
