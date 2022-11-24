package com.example.nocv.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nocv.entity.HealthClock;
import com.example.nocv.service.HealthClockService;
import com.example.nocv.vo.DataView;
import com.example.nocv.vo.HealthClockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class HealthClockController {

    @Autowired
    HealthClockService healthClockService;


    /*
    * 跳转页面
    * */
    @RequestMapping("/toHealthClock")
    public String toHealthClock(){
        return "admin/healthclock";
    }


    /*
    * 查询所有的记录
    * */
    @RequestMapping("/listHealthClock")
    @ResponseBody
    public DataView listHealthClock(HealthClockVo healthClockVo){
//        查询所有带有模糊查询条件 带有分页
        System.out.println(healthClockVo.getUsername());
        System.out.println(healthClockVo.getPhone());

        IPage<HealthClock> page = new Page<>(healthClockVo.getPage(),healthClockVo.getLimit());

        QueryWrapper<HealthClock> queryWrapper =new QueryWrapper<>();

        queryWrapper.like(healthClockVo.getUsername()!=null,"username",healthClockVo.getUsername());

        queryWrapper.like(healthClockVo.getPhone()!=null,"phone",healthClockVo.getPhone());
        healthClockService.page(page,queryWrapper);
        DataView dataView = new DataView(page.getTotal(), page.getRecords());
        System.out.println(dataView);
        return dataView;
    }

    /*
    * 添加健康打卡记录数据
    * */

    @RequestMapping("/addHealthClock")
    @ResponseBody
    public DataView addHealthClock(HealthClock healthClock){

        System.out.println(healthClock.getId());
        System.out.println(healthClock.getPhone());
        System.out.println(healthClock.toString());

        healthClock.setCreateTime(new Date());
        boolean b = healthClockService.saveOrUpdate(healthClock);
        DataView dataView = new DataView();
        if (b){
            dataView.setCode(200);
            dataView.setMsg("操作成功");
            return dataView;
        }
        dataView.setCode(100);
        dataView.setMsg("操作成功");
        return dataView;
    }


    /*
    * 根据id删除记录
    * */
    @RequestMapping("/deleteHealthClockById")
    @ResponseBody
    public DataView deleteHealthClockById(Integer id){
        boolean b = healthClockService.removeById(id);
        DataView dataView = new DataView();
        if (b) {
            dataView.setCode(200);
            dataView.setMsg("删除成功");
            return dataView;
        }
        dataView.setCode(100);
        dataView.setMsg("删除异常");
        return dataView;
    }

    @RequestMapping("/health/deleteByIds")
    @ResponseBody
    public DataView healthDeleteByIds(@RequestParam(name = "ids[]") Integer[] ids){


        int count = 0;
        for (int i=0;i<ids.length;i++){
            boolean i1 = healthClockService.removeById(ids[i]);
            if (i1){
                ++count;
            }
        }

        DataView dataView = new DataView();

        if (count==ids.length){
            dataView.setCode(200);
            dataView.setMsg("全部删除成功");
            return dataView;
        }
        dataView.setMsg("删除出现异常");
        dataView.setCode(100);
        return dataView;
    }
}
