package com.atguigu.gulimall.pms.service.impl;

import com.atguigu.gulimall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.pms.entity.AttrAttrgroupRelationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;

import com.atguigu.gulimall.pms.dao.AttrGroupDao;
import com.atguigu.gulimall.pms.entity.AttrGroupEntity;
import com.atguigu.gulimall.pms.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrAttrgroupRelationDao relationDao;
    @Autowired
    private AttrGroupDao attrGroupDao;

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageVo(page);
    }


    @Override
    public PageVo queryPageAttrGroupsByCatId(QueryCondition queryCondition,Long catId) {

        //获取分页条件
        IPage<AttrGroupEntity> page = new Query<AttrGroupEntity>().getPage(queryCondition);

        //获取查询条件
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
       //怎么分组的  是分类Id == 指定的值
        wrapper.eq("catelog_id",catId);

        //这个方法重写了 自己的方法
        IPage<AttrGroupEntity> data = this.page(page, wrapper);

        return new PageVo(data);//封装的vo类
    }

    @Override
    public AttrGroupEntity getGroupInfoByAttrId(Long attrId) {
        // 根据attrId 去关联关系表 找到在哪个组 关联关系查询就注入 关联关系的dao
        AttrGroupEntity attrGroupEntity=null;
        AttrAttrgroupRelationEntity one = relationDao.selectOne(
                new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_id", attrId));

        //根据分组的id查到 分组的信息
        if(one != null){

            Long attrGroupId = one.getAttrGroupId();  //分组的ID
             attrGroupEntity = attrGroupDao.selectById(attrGroupId);  //跟据ID 查出这个组的全部信息
        }


        return attrGroupEntity;
    }


}