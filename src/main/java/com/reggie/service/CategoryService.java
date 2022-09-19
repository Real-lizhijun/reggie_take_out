package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.pojo.Category;
import org.springframework.stereotype.Service;

public interface CategoryService extends IService<Category> {

    void remove(Long id);
}
