package com.example.nocv.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nocv.entity.NocvNews;
import com.example.nocv.entity.XueYuan;
import com.example.nocv.service.NocvNewsService;
import com.example.nocv.service.XueYuanService;
import com.example.nocv.vo.DataView;
import com.example.nocv.vo.NocvNewsVo;
import com.example.nocv.vo.XueYuanVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/xueyuan")
public class XueYuanController {
    @Autowired
    private XueYuanService xueYuanService;

    @RequestMapping("/toXueYuan")
    public String toXueYuan(){
        return "xueyuan/xueyuan";
    }


    @RequestMapping("/listXueYuan")
    @ResponseBody
    public DataView listNews(XueYuanVo xueYuanVo){
        QueryWrapper<XueYuan> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(xueYuanVo.getName()),"name",xueYuanVo.getName());
        IPage<XueYuan> page = new Page<>(xueYuanVo.getPage(),xueYuanVo.getLimit());
        IPage<XueYuan> page1 = xueYuanService.page(page, queryWrapper);
        return new DataView(page.getTotal(),page.getRecords());
    }

    @RequestMapping("/deleteById")
    @ResponseBody
    public DataView deleteById(Integer id){
        xueYuanService.removeById(id);
        DataView dataView = new DataView();
        dataView.setMsg("删除成功");
        dataView.setCode(200);
        return dataView;
    }

    /*
    * 新增或修改
    *
    * */
    @RequestMapping("/addOrUpdateXueYuan")
    @ResponseBody
    public DataView addOrUpdateNews(XueYuan xueYuan){
        xueYuanService.saveOrUpdate(xueYuan);
        DataView dataView  = new DataView();
        dataView.setCode(200);
        dataView.setMsg("新增或者修改成功");
        return dataView;
    }


}
