package com.atguigu.gulimall.pms.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;

import com.atguigu.gulimall.pms.dao.SpuInfoDao;
import com.atguigu.gulimall.pms.entity.SpuInfoEntity;
import com.atguigu.gulimall.pms.service.SpuInfoService;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo QueryPageByCatId(QueryCondition queryCondition, Long catId) {

        //1、封装查询条件
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();

        if(catId != 0 ){ //不等于0 就是查询本类

            wrapper.eq("catalog_id",catId);
        }
        //先查的是三级分类eq  并且and 名字  或者or ID
        if(!StringUtils.isEmpty(queryCondition.getKey())){
            /*  逻辑有误 wrapper.like("spu_name",queryCondition.getKey()).
                    or().like("id",queryCondition.getKey());*/

            wrapper.and(obj->{
                obj.like("spu_name",queryCondition.getKey());
                obj.or().like("id",queryCondition.getKey());
                return obj;

            });


        }

        //2、封装翻页条件

        Query<SpuInfoEntity> query = new Query<>();
        IPage<SpuInfoEntity> page = query.getPage(queryCondition);

        //3、去数据库查询
        IPage<SpuInfoEntity> data = this.page(page, wrapper);

        //data.getRecords  是查到真实数据
        PageVo pageVo = new PageVo(data.getRecords(), data.getTotal(), data.getSize(), data.getCurrent());

        return pageVo;
    }




}