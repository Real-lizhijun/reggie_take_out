package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.dto.DishDto;
import com.reggie.pojo.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {

    //新增菜品，同时保存菜品对应的口味数据，操作两张表dish，dish_flavor
    void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品及口味信息
    DishDto getByIdWithFlavor(Long id);

    //修改菜品信息
    void updateWithFlavor(DishDto dishDto);

    //删除菜品以及口味信息
    void deleteWithFlavor(List<Long> ids);
}
