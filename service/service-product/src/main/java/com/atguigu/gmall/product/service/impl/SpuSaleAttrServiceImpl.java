package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.ValueSkuJson;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.mapper.SpuSaleAttrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author mengxueshong
* @description 针对表【spu_sale_attr(spu销售属性)】的数据库操作Service实现
* @createDate 2022-08-23 10:13:46
*/
@Service
public class SpuSaleAttrServiceImpl extends ServiceImpl<SpuSaleAttrMapper, SpuSaleAttr>
    implements SpuSaleAttrService{

     @Autowired
     SpuSaleAttrMapper spuSaleAttrMapper;

    /**
     * 3：查询出item前端 sku所有兄弟的销售属性
     * @param spuId
     * @return
     */
    @Override
    public String getAllSkuSaleAttrValueJson(Long spuId) {
           //1:查询出来数据
         List<ValueSkuJson> valueSkuJsons    =spuSaleAttrMapper.getAllSkuValueJson(spuId);
          //2:数据转换
        Map map =new HashMap();
        for (ValueSkuJson skuJson : valueSkuJsons) {
            String valueJson = skuJson.getValueJson();
            Long skuId = skuJson.getSkuId();
            map.put(valueJson,skuId);
        }
        String json = Jsons.toStr(map);
        return json;
    }

    /**
     * 2 :查询前端 item页面需要的sale属性
     * @param spuId
     * @param skuId
     * @return
     */
    @Override
    public List<SpuSaleAttr> getItemSaleAttrAndValueSku(Long spuId, Long skuId) {
        List<SpuSaleAttr>  spuSaleAttrList=spuSaleAttrMapper.getItemSaleAttrAndValueSku(spuId,skuId);
        return spuSaleAttrList;
    }

    /**
     * 1：根据spu id查询Sale属性名和值
     * @param spuId
     * @return
     */
    @Override
    public List<SpuSaleAttr> getSaleList(Long spuId) {
        List<SpuSaleAttr> list =spuSaleAttrMapper.selectSaleList(spuId);

        return list;
    }

}




