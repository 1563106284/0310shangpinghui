package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.list.SearchAttr;
import com.atguigu.gmall.model.product.SkuAttrValue;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.SkuAttrValueService;
import com.atguigu.gmall.product.mapper.SkuAttrValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author mengxueshong
* @description 针对表【sku_attr_value(sku平台属性值关联表)】的数据库操作Service实现
* @createDate 2022-08-23 10:13:46
*/
@Service
public class SkuAttrValueServiceImpl extends ServiceImpl<SkuAttrValueMapper, SkuAttrValue>
    implements SkuAttrValueService{

   @Autowired
   SkuAttrValueMapper skuAttrValueMapper;
    /**
     *  1： 根据sku 的id 查询出属性名 和属性值的名
     *   提供给elastic：使用
     * @param skuId
     * @return
     */
    @Override
    public List<SearchAttr> getSkuAttrNameAndValue(Long skuId) {

        List<SearchAttr>  list=skuAttrValueMapper.getSkuAttrNameAndValue(skuId);
        return list;
    }
}




