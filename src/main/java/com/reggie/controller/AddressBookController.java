package com.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.reggie.common.BaseContext;
import com.reggie.common.Result;
import com.reggie.pojo.AddressBook;
import com.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Address;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 地址簿管理
 */
@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    @Resource
    private AddressBookService addressBookService;

    /**
     * 新增地址
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    public Result<AddressBook> save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info("addressBook: {}", addressBook);
        addressBookService.save(addressBook);
        return Result.success(addressBook);
    }

    /**
     * 设置默认地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    public Result<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("addressBook: {}", addressBook);
        LambdaUpdateWrapper<AddressBook> addressBookLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        addressBookLambdaUpdateWrapper.eq(AddressBook::getId, BaseContext.getCurrentId());
        addressBookLambdaUpdateWrapper.set(AddressBook::getIsDefault, 0);
        addressBookService.update(addressBookLambdaUpdateWrapper);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);

        return Result.success(addressBook);
    }

    /**
     * 根据id查询地址信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<AddressBook> get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return Result.success(addressBook);
        } else {
            return Result.error("没有找到该对象");
        }
    }

    /**
     * 查询默认地址
     *
     * @return
     */
    @GetMapping("/default")
    public Result<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId())
                .eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookService.getOne(addressBookLambdaQueryWrapper);
        if (addressBook == null) {
            return Result.error("没有找到该对象");
        } else {
            return Result.success(addressBook);
        }
    }

    /**
     * 查询某个用户的全部地址
     *
     * @param addressBook
     * @return
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info(addressBook.toString());

        LambdaQueryWrapper<AddressBook> addressBookLambdaQueryWrapper = new LambdaQueryWrapper<>();
        addressBookLambdaQueryWrapper.eq(addressBook.getUserId() != null, AddressBook::getUserId, addressBook.getUserId())
                .orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> list = addressBookService.list(addressBookLambdaQueryWrapper);
        return Result.success(list);
    }

}
