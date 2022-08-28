package com.atguigu.gmall.product.mapper;


import com.atguigu.gmall.model.product.SkuInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author mengxueshong
 * @description 针对表【sku_info(库存单元表)】的数据库操作Mapper
 * @createDate 2022-08-23 10:13:46
 * @Entity com.atguigu.gmall.product.domain.SkuInfo
 */
public interface SkuInfoMapper extends BaseMapper<SkuInfo> {
    /**
     * 2:下架sku
     *
     * @param skuId
     */
    void updateState0(@Param("skuId") Long skuId);

    /**
     * 1:上架sku
     *
     * @param skuId
     */
    void updateState1(@Param("skuId") Long skuId);

    BigDecimal selectPrice(@Param("skuId") Long skuId);
}




