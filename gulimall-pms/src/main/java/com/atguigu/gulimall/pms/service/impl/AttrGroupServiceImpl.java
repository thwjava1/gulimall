package com.atguigu.gulimall.pms.service.impl;

import com.atguigu.gulimall.pms.dao.AttrAttrgroupRelationDao;
import com.atguigu.gulimall.pms.dao.AttrDao;
import com.atguigu.gulimall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.pms.entity.AttrEntity;
import com.atguigu.gulimall.pms.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.BeanUtils;
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

import java.util.ArrayList;
import java.util.List;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrAttrgroupRelationDao relationDao;

    @Autowired
    private AttrGroupDao attrGroupDao;

    @Autowired
    AttrDao attrDao;

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

        //查出的所有分组
        List<AttrGroupEntity> records = data.getRecords();
        //查询每一个分组的所有属性  //分组的个数records.size()
        //将要返回出去的带分组信息以及他的属性的对象
        List<AttrGroupWithAttrsVo> groupWithAttrs = new ArrayList<AttrGroupWithAttrsVo>(records.size());

        for (AttrGroupEntity record : records) {//分组的信息
            //创建 一个attrGroupWithAttrsVo 准备收集所有数据
            AttrGroupWithAttrsVo attrGroupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties("record",attrGroupWithAttrsVo);//把分组的信息 拷贝到vo中


            //当前分组的ID
            Long groupId = record.getAttrGroupId();
            //获取当前分组的所有属性  怎么知道当前分组的所有属性是多少  分组和表有关系 查到
            //先用关联关系 查出哪些分组都关联了哪些属性  用 attr_group_id  与 查出的分组ID 进行对比
            List<AttrAttrgroupRelationEntity> relationEntities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", groupId));

            if (relationEntities != null && relationEntities.size()>0){


                //对这些属性 进行遍历 查出每个分组的id
                ArrayList<Long> attrIds = new ArrayList<>();
                for (AttrAttrgroupRelationEntity entity : relationEntities) {
                    attrIds.add(entity.getAttrId());
                }
                //查出当前分组所有真正的属性  in 把查到的主键 attr_id 放到  attrIds集合中  返回集合 attr的全部数据
                List<AttrEntity> attrEntities = attrDao.selectList(new QueryWrapper<AttrEntity>().in("attr_id", attrIds));

                //把attr的数据 放到 vo中
                attrGroupWithAttrsVo.setAttrEntities(attrEntities);
            }

            //把vo放到集合中
            groupWithAttrs.add(attrGroupWithAttrsVo);

        }

        return new PageVo(groupWithAttrs,data.getTotal(),data.getSize(),data.getCurrent());//封装的vo类
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