package com.atguigu.gmall.product.controller;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.product.service.BaseCategory1Service;
import com.atguigu.gmall.product.service.BaseCategory2Service;
import com.atguigu.gmall.product.service.BaseCategory3Service;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.commons.pool2.impl.BaseGenericObjectPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 1：controller+ responsebody=@RestController
 * 2:当前是分类接口
 */

@RequestMapping("/admin/product")
@RestController
public class CategroyController {

   @Autowired
  BaseCategory1Service baseCategory1Service;
   @Autowired
    BaseCategory2Service baseCategory2Service;
   @Autowired
    BaseCategory3Service baseCategory3Service;

    /**
     * 3级分类
     * @return
     */
   @GetMapping("/getCategory3/{c2id}")
   public Result method(@PathVariable("c2id")String c2id){
     List<BaseCategory3>  list =baseCategory3Service.getCategory2Child(c2id);
       return Result.ok(list);
   }


    /**
     * 2:根据一级分类id查询出2级分类id
     *
     */
     @GetMapping("/getCategory2/{c1id}")
     public Result getCategory2(@PathVariable("c1id")String c1id){
       List<BaseCategory2>  category2s   = baseCategory2Service.getCategory1Child(c1id);
         return Result.ok(category2s);
     }





    /**
     *   1:第一个接口：
     *   实现功能：查询1级分类
     *
      */
    @GetMapping("/getCategory1")
    public Result getCategory1(){

        List<BaseCategory1> list = baseCategory1Service.list();
        return Result.ok(list);
    }


}
