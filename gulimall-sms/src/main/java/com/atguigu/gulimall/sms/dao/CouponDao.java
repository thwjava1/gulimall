package com.atguigu.gulimall.sms.dao;

import com.atguigu.gulimall.sms.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author thw
 * @email thwjava@163.com
 * @date 2019-08-05 11:49:09
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
