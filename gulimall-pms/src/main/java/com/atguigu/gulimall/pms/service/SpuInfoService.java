package com.atguigu.gulimall.pms.service;

import com.atguigu.gulimall.pms.vo.BaseAttrVo;
import com.atguigu.gulimall.pms.vo.SkuVo;
import com.atguigu.gulimall.pms.vo.SpuAllSaveVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gulimall.pms.entity.SpuInfoEntity;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;

import java.util.List;


/**
 * spu信息
 *
 * @author thw
 * @email thwjava@163.com
 * @date 2019-08-02 08:28:55
 */

public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageVo queryPage(QueryCondition params);

    PageVo QueryPageByCatId(QueryCondition queryCondition, Long catId);

    void spuBigSaveAll(SpuAllSaveVo spuInfo);

    Long saveSpuBaseInfo(SpuAllSaveVo spuInfo);

    void saveSpuInfoImages(Long spuId, String[] spuImages);

    void saveSpuBaseAttrs(Long spuId, List<BaseAttrVo> baseAttrs);

    void saveSkuInfo(Long spuId, List<SkuVo> skuVos);
}


