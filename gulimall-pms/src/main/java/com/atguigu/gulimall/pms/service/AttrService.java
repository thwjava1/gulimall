package com.atguigu.gulimall.pms.service;

import com.atguigu.gulimall.pms.vo.AttrSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.pms.entity.AttrEntity;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;


/**
 * 商品属性
 *
 * @author thw
 * @email thwjava@163.com
 * @date 2019-08-02 08:28:55
 */

public interface AttrService extends IService<AttrEntity> {

    PageVo queryPage(QueryCondition params);

    // PageVo queryPageAttrsById(QueryCondition queryCondition, Long catId );
//变成可变type
    PageVo queryPageSaleAttrs(QueryCondition queryCondition, Long catId, Integer attrType);

    void saveAttrAndRelation(AttrSaveVo attr);
}

