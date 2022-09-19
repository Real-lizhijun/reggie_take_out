package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.dto.SetmealDto;
import com.reggie.pojo.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     *
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 根据id查询套餐信息
     *
     * @param id
     * @return
     */
    SetmealDto getWithDish(Long id);

    /**
     * 修改套餐信息
     *
     * @param setmealDto
     */
    void updateWithDish(SetmealDto setmealDto);

    /**
     * 根据ids删除套餐相关信息
     * @param ids
     */
    void removeWithDish(List<Long> ids);
}
