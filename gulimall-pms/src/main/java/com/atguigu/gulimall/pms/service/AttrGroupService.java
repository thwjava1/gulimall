package com.atguigu.gulimall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.pms.entity.AttrGroupEntity;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;


/**
 * 属性分组
 *
 * @author thw
 * @email thwjava@163.com
 * @date 2019-08-02 08:28:55
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageVo queryPage(QueryCondition params);


    PageVo queryPageAttrGroupsByCatId(QueryCondition queryCondition, Long catId);

    /**
     * 跟据属性的ID 找到他的分组信息
     * @param attrId
     * @return
     */
    AttrGroupEntity getGroupInfoByAttrId(Long attrId);
}

