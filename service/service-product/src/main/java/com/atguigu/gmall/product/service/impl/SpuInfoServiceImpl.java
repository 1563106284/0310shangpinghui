package com.atguigu.gmall.product.service.impl;


import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.product.SpuSaleAttrValue;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.atguigu.gmall.product.mapper.SpuInfoMapper;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.service.SpuSaleAttrValueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author mengxueshong
* @description 针对表【spu_info(商品表)】的数据库操作Service实现
* @createDate 2022-08-23 10:13:46
*/
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo>
    implements SpuInfoService{

      @Autowired
      SpuInfoService spuInfoService;
      @Autowired
      SpuImageService spuImageService;
      @Autowired
      SpuSaleAttrService saleAttrServicel;
       @Autowired
      SpuSaleAttrValueService spuSaleAttrValueService;

      /**
     * 1:保存后台系统提交的spu信息
     * @param spuInfo
     */
    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        //1:保存info信息
        spuInfoService.save(spuInfo);

        //2:保存:spu图片
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
         //2.1回填spu id
        for (SpuImage element : spuImageList) {
          element.setSpuId(spuInfo.getId());
        }
        spuImageService.saveBatch(spuImageList);

        //3:保存 spu：销售属性名
        List<SpuSaleAttr> salelist = spuInfo.getSpuSaleAttrList();
            //4.1：拿到销售名bean

        for (SpuSaleAttr element : salelist) {
            element.setSpuId(spuInfo.getId());
            List<SpuSaleAttrValue> values = element.getSpuSaleAttrValueList();
            for (SpuSaleAttrValue value : values) {
                 //回填spu id
                value.setSpuId(spuInfo.getId());
                //回填 salevalue：salename
                String attrName = element.getSaleAttrName();
                value.setSaleAttrName(attrName);
            }

           //4:保存 spu：销售属性值
          spuSaleAttrValueService.saveBatch(values);
        }
        //  保存销售属性名：
        saleAttrServicel.saveBatch(salelist);


    }
}




