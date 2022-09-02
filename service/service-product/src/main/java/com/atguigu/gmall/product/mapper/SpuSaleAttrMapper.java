package com.atguigu.gmall.product.mapper;

import com.atguigu.gmall.model.product.SpuSaleAttr;

import com.atguigu.gmall.model.to.ValueSkuJson;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author mengxueshong
 * @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Mapper
 * @createDate 2022-08-23 10:13:46
 * @Entity com.atguigu.gmall.product.domain.SpuSaleAttr
 */
public interface SpuSaleAttrMapper extends BaseMapper<SpuSaleAttr> {


    List<SpuSaleAttr> selectSaleList(@Param("spuId") Long spuId);

    List<SpuSaleAttr> getItemSaleAttrAndValueSku(@Param("spuId") Long spuId, @Param("skuId") Long skuId);

    List<ValueSkuJson> getAllSkuValueJson(@Param("spuId") Long spuId);
}




