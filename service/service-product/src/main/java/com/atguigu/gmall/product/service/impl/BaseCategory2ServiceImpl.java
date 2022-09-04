package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.BaseCategory2Service;
import com.atguigu.gmall.product.mapper.BaseCategory2Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author mengxueshong
* @description 针对表【base_category2(二级分类表)】的数据库操作Service实现
* @createDate 2022-08-23 07:44:12
*/
@Service
public class BaseCategory2ServiceImpl extends ServiceImpl<BaseCategory2Mapper, BaseCategory2>
    implements BaseCategory2Service{


     @Autowired
    BaseCategory2Mapper   baseCategory2Mapper;
    private String c1id;



    /**
     * 2：查询index：
     * 首页需要的树形分类数据
     * @return
     */
    @Override
    public List<CategoryTreeTo> getAllCategoryWithTree() {
        List<CategoryTreeTo>  list= baseCategory2Mapper.getAllCategoryWithTree();
        return list;
    }

    /**
     *
     * @param c1id
     * @return
     */
    @Override
    public List<BaseCategory2> getCategory1Child(String c1id) {
        this.c1id = c1id;
        QueryWrapper<BaseCategory2> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category1_id", c1id);
        List<BaseCategory2> list = baseCategory2Mapper.selectList(queryWrapper);

        return list;
    }

}




