package com.atguigu.gulimall.pms.dao;

import com.atguigu.gulimall.pms.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author thw
 * @email thwjava@163.com
 * @date 2019-08-02 08:28:55
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
