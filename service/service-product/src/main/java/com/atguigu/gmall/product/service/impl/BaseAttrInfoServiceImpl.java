package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.mapper.BaseAttrValueMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.mapper.BaseAttrInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author mengxueshong
* @description 针对表【base_attr_info(属性表)】的数据库操作Service实现
* @createDate 2022-08-23 10:13:46
*/
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo>
    implements BaseAttrInfoService{

     @Autowired
     BaseAttrInfoMapper baseAttrInfoMapper;
     @Autowired
     BaseAttrValueMapper baseAttrValueMapper;
    /**
     * 2:新增平台属性 和修改 平台属性 属性值
     * 涉及2个表：属性名 和属性值表
     * @param info
     */
    @Override
    public void insertAttrInfo(BaseAttrInfo info) {
        Long attrId = info.getId();

        if (attrId==null){
            //:1平台属性id为空就新增
        insertAttr(info);
        }else {
            updateAttr(info);


        }
    }
        //:修改属性
    private void updateAttr(BaseAttrInfo info) {
        //：2：平台属性中id不为空就是修改
        //修改中涉及到属性值的下新增值名，修改值 修改值名，删除值名
        //2.1 先删除
        //select * from value where attr_id=? and not in (范围内的)
        //3:修改属性名：
        baseAttrInfoMapper.updateById(info);
        //4：收集属性值删除id
        List<BaseAttrValue> valueList = info.getAttrValueList();

        List vid =new ArrayList();
        for (BaseAttrValue element : valueList) {
            Long id = element.getId();
            if (id != null){
                vid.add(id);
            }
        }
        //4.删除：
        if (vid.size()>0){
            QueryWrapper<BaseAttrValue> wrapper=new QueryWrapper<>();
            wrapper.eq("attr_id", info.getId());
            wrapper.notIn("id",vid);
            baseAttrValueMapper.delete(wrapper);
        }else {
            QueryWrapper<BaseAttrValue> wrapper=new QueryWrapper<>();
            wrapper.eq("attr_id", info.getId());
            baseAttrValueMapper.delete(wrapper);
        }

        //：5新增 修改
        for (BaseAttrValue element : valueList) {
            if (element.getId() !=null){
                //平台属性值id不为空就是修改
                baseAttrValueMapper.updateById(element);
            }else if (element.getId() == null){
                //  否则就是新增
                element.setAttrId(info.getId());
                baseAttrValueMapper.insert(element);
            }
        }
    }


    //:新增属性
    private void insertAttr(BaseAttrInfo info) {
        //1：新增属性名
        baseAttrInfoMapper.insert(info);
        Long id = info.getId();
        //2：新增属性值
        List<BaseAttrValue> valueList = info.getAttrValueList();
        for (BaseAttrValue element : valueList) {
            //获取属名的id
            element.setAttrId(id);
           baseAttrValueMapper.insert(element);
        }
    }

    /**
     * 1：根据分类id获取平台属性
     * @param c1Id
     * @param c2Id
     * @param c3Id
     * @return
     */
    @Override
    public List<BaseAttrInfo> getAttrInfoList(Long c1Id, Long c2Id, Long c3Id) {
        List<BaseAttrInfo>      attrinfo  =baseAttrInfoMapper.selectAttrinfo(c1Id,c2Id,c3Id);
        return attrinfo;
    }

}




