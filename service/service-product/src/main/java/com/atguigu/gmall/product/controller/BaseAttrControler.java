package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.model.product.BaseCategory1;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.AttributeView;
import java.util.List;
import java.util.function.LongSupplier;

/**
 * 平台属性相关API
 */
@RequestMapping("/admin/product")
@RestController
public class BaseAttrControler {
    @Autowired
    BaseAttrInfoService baseAttrInfoService;
    @Autowired
    BaseAttrValueService baseAttrValueService;

    /**
     * 3：修改属性前的回显：
     * 根据属性值的id获取属性信息
     */
    @GetMapping("/getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable("attrId")Long attrid){
           List<BaseAttrValue>  list   =baseAttrValueService.selectAttrValueInfo(attrid);
           return Result.ok(list);
    }

    /**
     * 2：新增属性信息 和 修改属性 2合一
     * 503 服务不可用
     */
   @PostMapping("/saveAttrInfo")
   public Result saveAttrInfo(@RequestBody BaseAttrInfo info) {

       baseAttrInfoService.insertAttrInfo(info);
       return Result.ok();
   }


    /**
     * 1:根据分类id查询获取平台属性
     */
     @GetMapping("/attrInfoList/{category1Id}/{category2Id}/{category3Id}")
     public Result getAttrInfoList(@PathVariable("category1Id")Long c1Id,
                                   @PathVariable("category2Id")Long c2Id,
                                   @PathVariable("category3Id")Long c3Id){
         List<BaseAttrInfo> list=baseAttrInfoService.getAttrInfoList(c1Id,c2Id,c3Id);

         return Result.ok(list);
     }

}
