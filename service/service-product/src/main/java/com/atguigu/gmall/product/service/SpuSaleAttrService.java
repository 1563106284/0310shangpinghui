package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author mengxueshong
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Service
* @createDate 2022-08-23 10:13:46
*/
public interface SpuSaleAttrService extends IService<SpuSaleAttr> {

    List<SpuSaleAttr> getSaleList(Long spuId);

    List<SpuSaleAttr> getItemSaleAttrAndValueSku(Long spuId, Long skuId);

    String getAllSkuSaleAttrValueJson(Long spuId);
}
