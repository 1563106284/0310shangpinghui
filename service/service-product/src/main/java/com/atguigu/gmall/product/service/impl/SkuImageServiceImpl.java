package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.SkuImage;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.mapper.SkuImageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author mengxueshong
* @description 针对表【sku_image(库存单元图片表)】的数据库操作Service实现
* @createDate 2022-08-23 10:13:46
*/
@Service
public class SkuImageServiceImpl extends ServiceImpl<SkuImageMapper, SkuImage>
    implements SkuImageService{
    @Autowired
    SkuImageMapper skuImageMapper;

    /**
     * 1：查询item首页信息中
     * sku 图片
     * @param skuId
     * @return
     */
    @Override
    public List<SkuImage> getImages(Long skuId) {
        QueryWrapper<SkuImage> wrapper=new QueryWrapper<>();
        wrapper.eq("sku_id", skuId);
        List<SkuImage> imageList = skuImageMapper.selectList(wrapper);
        return imageList;
    }
}




