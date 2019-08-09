package com.atguigu.gulimall.pms.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuVo {

    //mingzi
    private String skuName;
    //miaoshu
    private String skuDesc;
    //biaoti
    private String skuTitle;
    //sku副标题
    private String skuSubtitle;
    //zhongliang
    private BigDecimal weight;
    //shangpin jiage
    private BigDecimal price;
    //shupian shi  shuzu
    private String[] images;
    //当前sku 对应的 销售属性组合
    private List<SaleAttrVo> saleAttrs;

    private BigDecimal growBounds;
    private BigDecimal buyBounds;


    //积分设置信息
    private Integer[] work;

    //阶梯价格的信息
    private Integer fullCount;
    private BigDecimal discount;
    private Integer ladderAddOther;
    //满减优惠
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer fullAddOther;










}
