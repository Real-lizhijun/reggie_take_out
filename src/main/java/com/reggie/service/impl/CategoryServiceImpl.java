package com.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.reggie.exception.CustomException;
import com.reggie.mapper.CategoryMapper;
import com.reggie.pojo.Category;
import com.reggie.pojo.Dish;
import com.reggie.pojo.Setmeal;
import com.reggie.service.CategoryService;
import com.reggie.service.DishService;
import com.reggie.service.SetmealService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private DishService dishService;

    @Resource
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前要进行判断
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        //查询当前分类是否已经关联了菜品，如果已经关联，那么就不能删除，抛出一个业务异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        long count1 = dishService.count(dishLambdaQueryWrapper);

        if (count1 > 0) {
            //当前分类已经关联了菜品，无法删除，抛出业务异常
            throw new CustomException("当前分类已经关联了菜品，无法删除");
        }

        //查询当前分类是否已经关联了套餐，如果已经关联，那么就不能删除，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        long count2 = setmealService.count(setmealLambdaQueryWrapper);

        if (count2 > 0) {
            //当前分类已经关联了套餐，无法删除，抛出业务异常
            throw new CustomException("当前分类已经关联了套餐，无法删除");
        }

        //如果没有关联任何菜品和套餐，那么就可以执行删除
        super.removeById(id);
    }
}
