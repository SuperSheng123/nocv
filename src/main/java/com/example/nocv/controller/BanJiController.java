package com.example.nocv.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nocv.entity.BanJi;
import com.example.nocv.entity.XueYuan;
import com.example.nocv.service.BanJiService;
import com.example.nocv.service.XueYuanService;
import com.example.nocv.vo.BanJiVo;
import com.example.nocv.vo.DataView;
import com.example.nocv.vo.XueYuanVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/banji")
public class BanJiController {
    @Autowired
    private BanJiService banJiService;




    @GetMapping("/banji/listBanJi")
    public DataView listNews(BanJiVo banJiVo){
        QueryWrapper<BanJi> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(banJiVo.getName()),"name",banJiVo.getName());
        IPage<BanJi> page = new Page<>(banJiVo.getPage(),banJiVo.getLimit());
        IPage<BanJi> page1 = banJiService.page(page, queryWrapper);
        return new DataView(page.getTotal(),page.getRecords());
    }

    @DeleteMapping("/banji/deleteById")
    public DataView deleteById(Integer id){
        banJiService.removeById(id);
        DataView dataView = new DataView();
        dataView.setMsg("删除成功");
        dataView.setCode(200);
        return dataView;
    }

    /*
    * 新增或修改
    *
    * */
    @PostMapping ("/banji/addOrUpdateBanJi")
    public DataView addOrUpdateNews(BanJi banJi){
        banJiService.saveOrUpdate(banJi);
        DataView dataView  = new DataView();
        dataView.setCode(200);
        dataView.setMsg("新增或者修改成功");
        return dataView;
    }


}
