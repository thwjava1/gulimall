package com.atguigu.gulimall.pms.controller;

import java.util.Arrays;
import java.util.Map;


import com.atguigu.gulimall.commons.bean.PageVo;
import com.atguigu.gulimall.commons.bean.QueryCondition;
import com.atguigu.gulimall.commons.bean.Resp;
import com.atguigu.gulimall.pms.entity.AttrGroupEntity;
import com.atguigu.gulimall.pms.service.AttrGroupService;
import com.atguigu.gulimall.pms.vo.AttrGroupWithAttrsVo;
import com.atguigu.gulimall.pms.vo.AttrSaveVo;
import com.atguigu.gulimall.pms.vo.AttrWithGroupVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.pms.entity.AttrEntity;
import com.atguigu.gulimall.pms.service.AttrService;


/**
 * 商品属性
 *
 * @author thw
 * @email thwjava@163.com
 * @date 2019-08-02 08:28:55
 */
@Api(tags = "商品属性 管理")
@RestController
@RequestMapping("pms/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrGroupService attrGroupService;

    @ApiOperation("查看某个三级分类下的所有销售信息")
    @GetMapping("/sale/{catId}")
    public Resp<PageVo> getCatelogSaleAttrs(
            QueryCondition queryCondition,
            @PathVariable("catId") Long catId
    ) {

        //在controller中 可以手动 写值 到service的 method的 param中
        PageVo vo = attrService.queryPageSaleAttrs(queryCondition, catId, 0);

        return Resp.ok(vo);
    }


    @ApiOperation("查看某个三级分类下的所有基本信息")
    @GetMapping("/base/{catId}")
    public Resp<PageVo> getCatelogBaseAttrs(
            QueryCondition queryCondition,
            @PathVariable("catId") Long catId) {

        PageVo vo = attrService.queryPageSaleAttrs(queryCondition, catId, 1);

        return Resp.ok(vo);
    }

    /**
     * 列表
     */
    @ApiOperation("分页查询(排序)")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('pms:attr:list')")
    public Resp<PageVo> list(QueryCondition queryCondition) {

        PageVo page = attrService.queryPage(queryCondition);
        return Resp.ok(page);
    }


    /**
     * 信息
     */
    @ApiOperation("详情查询")
    @GetMapping("/info/{attrId}")
    @PreAuthorize("hasAuthority('pms:attr:info')")
    public Resp<AttrWithGroupVo> info(@PathVariable("attrId") Long attrId) {
        //1、查出属性信息
        AttrWithGroupVo attrWithGroupVo = new AttrWithGroupVo();
        AttrEntity attr = attrService.getById(attrId);
        BeanUtils.copyProperties("attr",attrWithGroupVo);

        //2、查出分组信息
       AttrGroupEntity attrGroupEntity = attrGroupService.getGroupInfoByAttrId(attrId);
        attrWithGroupVo.setGroup(attrGroupEntity);


        return Resp.ok(attrWithGroupVo);
    }

    /**
     * 保存
     */
    @ApiOperation("保存")
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('pms:attr:save')")
    public Resp<Object> save(@RequestBody AttrSaveVo attr) {
        attrService.saveAttrAndRelation(attr);

        return Resp.ok(null);
    }

    /**
     * 修改
     */
    @ApiOperation("修改")
    @PostMapping("/update")
    @PreAuthorize("hasAuthority('pms:attr:update')")
    public Resp<Object> update(@RequestBody AttrEntity attr) {
        attrService.updateById(attr);

        return Resp.ok(null);
    }

    /**
     * 删除
     */
    @ApiOperation("删除")
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('pms:attr:delete')")
    public Resp<Object> delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));

        return Resp.ok(null);
    }

}
