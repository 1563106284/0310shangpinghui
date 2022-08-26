package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseTrademark;
import com.atguigu.gmall.product.service.BaseTrademarkService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * trademark:
 * 品牌接口
 */
@RestController
@RequestMapping("/admin/product/baseTrademark")
public class BaseTrademarkController {

    @Autowired
    BaseTrademarkService baseTrademarkService;
    /**
     * 6:获取所有品牌trademark
     */
    @GetMapping("/getTrademarkList")
    public Result getTrademarkList(){
        List<BaseTrademark> list = baseTrademarkService.list();
        return Result.ok(list);
    }




    /**
     * 5:删除品牌：
     */
    @DeleteMapping("remove/{id}")
    public Result deleteTrademark(@PathVariable("id")Long id){
        baseTrademarkService.removeById(id);
        return Result.ok();
    }

    /**
     * 4：修改品牌：
     */
    @PutMapping("/update")
    public Result updateTrademark(@RequestBody BaseTrademark baseTrademark){
         baseTrademarkService.updateById(baseTrademark);
        return Result.ok();
    }

    /**
     * 3:修改前的数据回显
     * 根基trademark id 查询当前品牌信息
     */
    @GetMapping("/get/{id}")
    public Result selectTrademarkById(@PathVariable("id")Long id){
        BaseTrademark trademark = baseTrademarkService.getById(id);

        return Result.ok(trademark);
    }

    /**
     *  2：新增品牌
     */
     @PostMapping("/save")
     public Result insertTrademark(@RequestBody BaseTrademark baseTrademark){
         baseTrademarkService.save(baseTrademark);
         return Result.ok();
     }
    /**
     * 1：品牌分页接口
     * page:有多少页
     * limit：有几条
     */
    @RequestMapping("/{page}/{limit}")
    public Result trademarkPage(@PathVariable("page")Long page,@PathVariable("limit")Long limit){
        Page<BaseTrademark> trademarkPage = new Page<>(page, limit);
        Page<BaseTrademark> baseTrademarkPage = baseTrademarkService.page(trademarkPage);

        return Result.ok(baseTrademarkPage);
    }




}
