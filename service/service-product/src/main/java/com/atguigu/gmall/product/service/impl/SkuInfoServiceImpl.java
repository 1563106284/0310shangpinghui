package com.atguigu.gmall.product.service.impl;
import com.atguigu.gamll.feign.search.SearchFeignClient;
import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.list.SearchAttr;
import com.atguigu.gmall.product.mapper.BaseTrademarkMapper;
import com.google.common.collect.Lists;
import java.util.Date;

import com.atguigu.gmall.common.constant.StyRedisConst;
import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.product.mapper.BaseCategory3Mapper;
import com.atguigu.gmall.product.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author mengxueshong
 * @description 针对表【sku_info(库存单元表)】的数据库操作Service实现
 * @createDate 2022-08-23 10:13:46
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
        implements SkuInfoService {
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
    @Autowired
    BaseCategory3Mapper baseCategory3Mapper;
    @Autowired
    SpuSaleAttrService spuSaleAttrService;
    @Autowired
    RedissonClient redissonClient;

    @Autowired
    BaseTrademarkService trademarkService;

    @Autowired
    SearchFeignClient searchFeignClient;

    /**
     *   9:根据 skuid查询查询goods信息
     * @param skuId
     * @return
     */
    @Override
    public Goods getGoodsBySkuId(Long skuId) {
         // 1:通过id查询当前goods 得信息
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        // 2:创建 goods对象 准备摄入值
        Goods goods = new Goods();
        //   sku 得相关属性
        goods.setId(skuId);
        goods.setDefaultImg(skuInfo.getSkuDefaultImg());
        goods.setTitle(skuInfo.getSkuName());
        goods.setPrice(skuInfo.getPrice().doubleValue());
        goods.setCreateTime(new Date());
         // 3 ：品牌 相关属性
        goods.setTmId(skuInfo.getTmId());
        BaseTrademark trademark = trademarkService.getById(skuInfo.getTmId());
        goods.setTmName(trademark.getTmName());
        goods.setTmLogoUrl(trademark.getLogoUrl());


        //  4； 分类信息
        CategoryViewTo view = baseCategory3Mapper.getCategoryView(skuInfo.getCategory3Id());
        goods.setCategory1Id(view.getCategory1Id());
        goods.setCategory1Name(view.getCategory1Name());
        goods.setCategory2Id(view.getCategory2Id());
        goods.setCategory2Name(view.getCategory2Name());
        goods.setCategory3Id(view.getCategory3Id());
        goods.setCategory3Name(view.getCategory3Name());
        goods.setHotScore(0L);

        // 5：根据sku_id 查询出平台属性的名 和值名
       List<SearchAttr> attrs =skuAttrValueService.getSkuAttrNameAndValue(skuId);
        goods.setAttrs(attrs);

        return goods;
    }

    /**
     * 8：查询所有布隆需要的商品id
     *
     * @return
     */
    @Override
    public List<Long> findAllSkuId() {
        // 如果是大量数据 ：使用分页查询

        return skuInfoMapper.getAllSkuId();
    }

    /**
     * 7:大接口优化
     * 获取sku 图片
     *
     * @param skuId
     * @return
     */
    @Override
    public List<SkuImage> getDetailSkuImage(Long skuId) {
        //4:查询sku 的图片
        List<SkuImage> images = skuImageService.getImages(skuId);

        return images;
    }


    /**
     * 6；大接口优化：
     * 1：item info信息
     *
     * @param skuId
     * @return
     */
    @Override
    public SkuInfo getDetailSkuInfo(Long skuId) {
        //1:根据sku id查询出所有skuinfo数据，得到category 3级id
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);
        return skuInfo;
    }


    /**
     * 5:查询实时价格
     *
     * @param skuId
     * @return
     */
    @Override
    public BigDecimal get1010Price(Long skuId) {
        BigDecimal price = skuInfoMapper.selectPrice(skuId);
        return price;
    }


    /**
     * 4：优化前：查询前端 item页面所有需要的数据
     *
     * @param skuId
     * @return
     */
    @Deprecated
    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        SkuDetailTo skuDetailTo = new SkuDetailTo();

        //1:根据sku id查询出所有skuinfo数据，得到category 3级id
        SkuInfo skuInfo = skuInfoMapper.selectById(skuId);

        //2:根据当前category id查询item页面：当前产品所有分类的信息
        CategoryViewTo categoryView = baseCategory3Mapper.getCategoryView(skuInfo.getCategory3Id());
        skuDetailTo.setCategoryView(categoryView);

        //3:查询出 sku info 数据
        skuDetailTo.setSkuInfo(skuInfo);

        //4:查询sku 的图片
        List<SkuImage> imageList = skuImageService.getImages(skuId);
        skuInfo.setSkuImageList(imageList);

        //5:查询item 实时价格
        BigDecimal price = get1010Price(skuId);
        skuDetailTo.setPrice(price);

        //6:查询item：spu的销售属性
        List<SpuSaleAttr> saleList = spuSaleAttrService.getItemSaleAttrAndValueSku(skuInfo.getSpuId(), skuId);

        skuDetailTo.setSpuSaleAttrList(saleList);

        //7:sku所有兄弟销售属性名和属性值关系查出来并封装

        Long spuId = skuInfo.getSpuId();
        String valueJson = spuSaleAttrService.getAllSkuSaleAttrValueJson(spuId);
        skuDetailTo.setValuesSkuJson(valueJson);

        return skuDetailTo;
    }


    /**
     * 3:后台新增sku数据大保存
     *
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
        for (SkuImage skuImage : skuImageList) {
            skuImage.setSkuId(skuInfo.getId());
        }
        skuImageService.saveBatch(skuImageList);
        //4:保存销售属性
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue value : skuSaleAttrValueList) {
            value.setSpuId(skuInfo.getId());
            value.setSpuId(skuInfo.getSpuId());
        }
        saleAttrValueService.saveBatch(skuSaleAttrValueList);

        // 5: 商品id 放入到 Bloom过滤器中
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter(StyRedisConst.BLOOM_SKUID);
        // 5.1:存进去
        bloomFilter.add(skuInfo.getId());
    }


    /**
     * 2：下架sku方法
     *
     * @param skuId
     */
    @Override
    public void cancelSale(Long skuId) {
        skuInfoMapper.updateState0(skuId);

        // 2:elastic中 删除商品的
       searchFeignClient.deleteGoods(skuId);

    }



    /**
     * 1：上架sku方法
     *
     * @param skuId
     */
    @Override
    public void updateState(Long skuId) {

        skuInfoMapper.updateState1(skuId);

        // TODO: 2022/9/6  上架sku商品存入elastic思路：
        Goods goods =getGoodsBySkuId(skuId);
           // 2:调用保存到elastic 商品
         searchFeignClient.saveGoods(goods);


    }

}




