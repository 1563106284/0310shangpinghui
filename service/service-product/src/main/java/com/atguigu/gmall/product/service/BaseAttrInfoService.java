package com.atguigu.gmall.product.service;


import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author mengxueshong
* @description 针对表【base_attr_info(属性表)】的数据库操作Service
* @createDate 2022-08-23 10:13:46
*/
public interface BaseAttrInfoService extends IService<BaseAttrInfo> {


    void insertAttrInfo(BaseAttrInfo info);

    List<BaseAttrInfo> getAttrInfoList(Long c1Id, Long c2Id, Long c3Id);


}
