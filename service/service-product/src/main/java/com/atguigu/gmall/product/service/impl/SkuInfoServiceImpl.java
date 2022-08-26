package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SkuAttrValue;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SkuSaleAttrValue;
import com.atguigu.gmall.product.service.SkuAttrValueService;
import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.service.SkuSaleAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author mengxueshong
* @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
* @createDate 2022-08-23 10:13:46
*/
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
    implements SkuInfoService{
      @Autowired
      SkuInfoMapper skuInfoMapper;
      @Autowired
      SkuInfoService skuInfoService;
      @Autowired
    SkuAttrValueService skuAttrValueService;
       @Autowired
    SkuImageService skuImageService;
       @Autowired
    SkuSaleAttrValueService saleAttrValueService;
    /**
     * 3:后台新增sku数据大保存
     * @param skuInfo
     */
    @Override
    public void saveSkuInfoList(SkuInfo skuInfo) {
        //1：保存sku info
        skuInfoService.save(skuInfo);

        //2:保存sku平台属性
        List<SkuAttrValue> values = skuInfo.getSkuAttrValueList();
               //2.1:回填skuid到attr表中
        for (SkuAttrValue value : values) {
            value.setSkuId(skuInfo.getId());

        }
        skuAttrValueService.saveBatch(values);
        //3；保存图片：数据 sku image
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();

        skuImageService.saveBatch(skuImageList);
        //4:保存销售属性
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue value : skuSaleAttrValueList) {
             value.setSpuId(skuInfo.getId());
             value.setSpuId(skuInfo.getSpuId());
        }
        saleAttrValueService.saveBatch(skuSaleAttrValueList);

    }
    /**
     * 2：下架sku方法
     * @param skuId
     */
    @Override
    public void cancelSale(Long skuId) {
        skuInfoMapper.updateState0(skuId);

    }


    /**
     *  1：上架sku方法
     * @param skuId
     */
    @Override
    public void updateState(Long skuId) {

        skuInfoMapper.updateState1(skuId);
    }

}




