package com.atguigu.gulimall.sms.dao;

import com.atguigu.gulimall.sms.entity.CouponSpuRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券与产品关联
 * 
 * @author thw
 * @email thwjava@163.com
 * @date 2019-08-05 11:49:09
 */
@Mapper
public interface CouponSpuRelationDao extends BaseMapper<CouponSpuRelationEntity> {
	
}
