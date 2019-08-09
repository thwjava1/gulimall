package com.atguigu.gulimall.pms.dao;

import com.atguigu.gulimall.pms.entity.ProductAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

/**
 * spu属性值
 * 
 * @author thw
 * @email thwjava@163.com
 * @date 2019-08-02 08:28:55
 */
@Mapper
public interface ProductAttrValueDao extends BaseMapper<ProductAttrValueEntity> {
        //用  @Param("baseAttrs")  去集合中的 实体类 取个别名
    void insertBatch(@Param("baseAttrs") ArrayList<ProductAttrValueEntity> allSave);
}
