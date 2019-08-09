package com.atguigu.gulimall.pms.vo;

import com.atguigu.gulimall.pms.entity.SpuInfoEntity;
import lombok.Data;

import java.util.List;

@Data
public class SpuAllSaveVo extends SpuInfoEntity {
//spu的详情图
    private String[]  spuImages;
//当前spu的所有基本属性名 值  对
    private List<BaseAttrVo> baseAttrs;
//当前spu 对应的sku所有信息
    private List<SkuVo> skuVos;

}
