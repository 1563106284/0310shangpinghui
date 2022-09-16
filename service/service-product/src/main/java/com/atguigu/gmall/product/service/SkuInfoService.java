package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

/**
* @author mengxueshong
* @description 针对表【sku_info(库存单元表)】的数据库操作Service
* @createDate 2022-08-23 10:13:46
*/
public interface SkuInfoService extends IService<SkuInfo> {

  Goods getGoodsBySkuId(Long skuId);

    void updateState(Long skuId);

    void cancelSale(Long skuId);

    void saveSkuInfoList(SkuInfo skuInfo);

    SkuDetailTo getSkuDetail(Long skuId);

    BigDecimal get1010Price(Long skuId);

    SkuInfo getDetailSkuInfo(Long skuId);

    List<SkuImage> getDetailSkuImage(Long skuId);

    List<Long> findAllSkuId();
}
