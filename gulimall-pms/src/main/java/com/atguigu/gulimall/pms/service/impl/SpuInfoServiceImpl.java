package com.atguigu.gulimall.pms.service.impl;

import com.atguigu.gulimall.commons.to.SkuSaleInfoTo;
import com.atguigu.gulimall.commons.utils.AppUtils;
import com.atguigu.gulimall.pms.dao.*;
import com.atguigu.gulimall.pms.entity.*;
import com.atguigu.gulimall.pms.feign.SmsSkuSaleInfoFeignService;
import com.atguigu.gulimall.pms.vo.BaseAttrVo;
import com.atguigu.gulimall.pms.vo.SaleAttrVo;
import com.atguigu.gulimall.pms.vo.SkuVo;
import com.atguigu.gulimall.pms.vo.SpuAllSaveVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.Query;
import com.atguigu.gulimall.commons.bean.QueryCondition;

import com.atguigu.gulimall.pms.service.SpuInfoService;

@Slf4j
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    SmsSkuSaleInfoFeignService smsSkuSaleInfoFeignService;
    @Autowired
    SpuInfoDao spuInfoDao;
    @Autowired
    SpuInfoDescDao spuInfoDescDao;
    @Autowired
    ProductAttrValueDao productAttrValueDao;
    @Autowired
    SkuInfoDao skuInfoDao;
    @Autowired
    SkuImagesDao skuImagesDao;
    @Autowired
    AttrDao attrDao;
    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;

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

        if (catId != 0) { //不等于0 就是查询本类

            wrapper.eq("catalog_id", catId);
        }
        //先查的是三级分类eq  并且and 名字  或者or ID
        if (!StringUtils.isEmpty(queryCondition.getKey())) {
            /*  逻辑有误 wrapper.like("spu_name",queryCondition.getKey()).
                    or().like("id",queryCondition.getKey());*/

            wrapper.and(obj -> {
                obj.like("spu_name", queryCondition.getKey());
                obj.or().like("id", queryCondition.getKey());
                return obj;

            });


        }

        //2、封装翻页条件

        Query<SpuInfoEntity> query = new Query<>();
        IPage<SpuInfoEntity> page = query.getPage(queryCondition);

        //3、去数据库查询
        IPage<SpuInfoEntity> data = this.page(page, wrapper);

        //data.getRecords  是查到真实数据
        //PageVo pageVo = new PageVo(data.getRecords(), data.getTotal(), data.getSize(), data.getCurrent());

        PageVo vo = new PageVo(data);
        return vo;
    }

    @Override
    public void spuBigSaveAll(SpuAllSaveVo spuInfo) {
        //1、保存spu的基本信息 spu、的信息都在SpuInfoEntity中
        Long spuId = this.saveSpuBaseInfo(spuInfo);
        //1.2 保存spu的所有图片信息
        this.saveSpuInfoImages(spuId, spuInfo.getSpuImages());

        //2、保存sku的基本属性信息
        //用这个方法 把当前商品的所有属性全部保存起来
        List<BaseAttrVo> baseAttrs = spuInfo.getBaseAttrs();
        this.saveSpuBaseAttrs(spuId, baseAttrs);


        //3、保存sku以及sku的营销相关信息
        this.saveSkuInfo(spuId, spuInfo.getSkuVos());


    }

    @Override
    public Long saveSpuBaseInfo(SpuAllSaveVo spuInfo) {
        //此方法用于组装操作  你把你的Vo给我 我service负责解析数据做出相应的业务 数据存到什么地方 ，发送到什么地方   controller负责收集数据


        //1、保存spu的基本信息 spu、的信息都在SpuInfoEntity中
        //spuInfoEntity 这个对象里的数据 要保存要从前端接收过来的数据保存到数据库中
        // 所有要把数据进行拷贝 这个spuInfo 就是从前端传递过来的数据
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity(); //创建初始化 还没有数据信息
        //对拷属性
        BeanUtils.copyProperties(spuInfo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUodateTime(new Date());
        //要这个方法 是处理完成返回来一个id
        //Long spuId = this.saveSpuBaseInfo(supInfoEntity);
        //要这个方法 是处理完成返回来一个id
        spuInfoDao.insert(spuInfoEntity);

        return spuInfoEntity.getId();


    }

    @Override
    public void saveSpuInfoImages(Long spuId, String[] spuImages) {
        //把spuID  中 存入spuImages 些图片
        StringBuffer urls = new StringBuffer();
        for (String spuImage : spuImages) {
            urls.append(spuImage);
            urls.append(",");
        }

        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setDecript(urls.toString());
        descEntity.setSpuId(spuId);
        //spuInfoDescDao.insert(descEntity);
        spuInfoDescDao.insertInfo(descEntity);
    }

    @Override
    public void saveSpuBaseAttrs(Long spuId, List<BaseAttrVo> baseAttrs) {
        // 包数据保存到这个表中   ProductAttrValueEntity   把商品的基本属性名 和值 都保存

        // 用集合保存 批量保存 插入
        ArrayList<ProductAttrValueEntity> allSave = new ArrayList<>();

        if (baseAttrs != null && baseAttrs.size() > 0) {
            for (BaseAttrVo baseAttr : baseAttrs) {
                ProductAttrValueEntity entity = new ProductAttrValueEntity();
                entity.setAttrId(baseAttr.getAttrId());
                entity.setAttrName(baseAttr.getAttrName());
                String[] selected = baseAttr.getValueSelected();
                entity.setAttrValue(AppUtils.arrayToStringWithSeprator(selected, ","));

                entity.setAttrSort(0);
                entity.setSpuId(spuId);
                entity.setQuickShow(1);
                allSave.add(entity);
            }
        }
        productAttrValueDao.insertBatch(allSave);

    }

    @Override
    public void saveSkuInfo(Long spuId, List<SkuVo> skuVos) {

        SpuInfoEntity spuInfo = spuInfoDao.selectById(spuId);
//Tos 封装
        List<SkuSaleInfoTo> tos = new ArrayList<SkuSaleInfoTo>();

        for (SkuVo skuVo : skuVos) {
            String[] images = skuVo.getImages();
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            skuInfoEntity.setBrandId(spuInfo.getBrandId());
            skuInfoEntity.setCatalogId(spuInfo.getCatalogId());
            skuInfoEntity.setPrice(skuVo.getPrice());
            skuInfoEntity.setSkuCode(UUID.randomUUID().toString().substring(0, 5).toUpperCase());
            if (images != null && images.length > 0) {
                skuInfoEntity.setSkuDefaultImg(skuVo.getImages()[0]);

            }
            skuInfoEntity.setSkuDesc(skuVo.getSkuDesc());
            skuInfoEntity.setSkuName(skuVo.getSkuName());
            skuInfoEntity.setSkuSubtitle(skuVo.getSkuSubtitle());
            skuInfoEntity.setWeight(skuVo.getWeight());
            skuInfoEntity.setSpuId(spuId);
            //保存sku的基本信息
            skuInfoDao.insert(skuInfoEntity);

            Long skuId = skuInfoEntity.getSkuId();

            //2.保存sku的所有对应图片
            for (int i = 0; i < images.length; i++) {
                SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                skuImagesEntity.setDefaultImg(i == 0 ? 1 : 0);
                skuImagesEntity.setSkuId(skuId);
                skuImagesEntity.setImgSort(0);
                skuImagesEntity.setImgUrl(images[i]);
                skuImagesDao.insert(skuImagesEntity);

            }
            //3、当前sku的所有销售属性组合保存起来
            List<SaleAttrVo> saleAttrs = skuVo.getSaleAttrs();
            for (SaleAttrVo attrVo : saleAttrs) {
                //查询当前属性的信息

                SkuSaleAttrValueEntity entity = new SkuSaleAttrValueEntity();
                entity.setAttrId(attrVo.getAttrId());
                //查出这个属性的真正信息
                AttrEntity attrEntity = attrDao.selectById(attrVo.getAttrId());
                entity.setAttrSort(0);
                entity.setAttrValue(attrVo.getAttrValue());
                entity.setAttrName(attrEntity.getAttrName());
                entity.setSkuId(skuId);

                //sku与销售属性的关联关系
                skuSaleAttrValueDao.insert(entity);

            }
            SkuSaleInfoTo info = new SkuSaleInfoTo();
            BeanUtils.copyProperties(skuVo,info);
            info.setSkuId(skuId);


            tos.add(info);



        }
//2、发给sms，让他去处理。我们不管
        log.info("pms准备给sms发出数据...{}",tos);
        smsSkuSaleInfoFeignService.saveSkuSaleInfos(tos);
        log.info("pms给sms发出数据完成...");



    }


}