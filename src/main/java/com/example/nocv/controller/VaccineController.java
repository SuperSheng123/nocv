package com.example.nocv.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nocv.entity.HeSuan;
import com.example.nocv.entity.Vaccine;
import com.example.nocv.service.VaccineService;
import com.example.nocv.vo.DataView;
import com.example.nocv.vo.HeSuanVo;
import com.example.nocv.vo.VaccineVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/vaccine")
public class VaccineController {

    @Autowired
    private VaccineService vaccineService;
    @RequestMapping("/toVaccine")
    public String toVaccine(){
        return "vaccine/vaccine";
    }

    @RequestMapping("/loadAllVaccine")
    @ResponseBody
    public DataView loadAllVaccine(VaccineVo vaccine){
        IPage<Vaccine> iPage = new Page<>(vaccine.getPage(),vaccine.getLimit());
        QueryWrapper<Vaccine> queryWrapper  = new QueryWrapper<>();
        vaccineService.page(iPage,queryWrapper);
        return new DataView(iPage.getTotal(),iPage.getRecords());
    }

    @RequestMapping("/addVaccine")
    @ResponseBody
    public DataView addVaccine(Vaccine vaccine){
        vaccineService.save(vaccine);
        DataView dataView = new DataView();
        dataView.setMsg("添加疫苗数据成功");
        dataView.setCode(200);
        return dataView;
    }

    @RequestMapping("/updateVaccine")
    @ResponseBody
    public DataView updateVaccine(Vaccine vaccine){
        vaccineService.updateById(vaccine);
        DataView dataView = new DataView();
        dataView.setCode(200);
        dataView.setMsg("修改疫苗成功！");
        return dataView;
    }

    @RequestMapping("/deleteVaccine")
    @ResponseBody
    public DataView deleteVaccine(Integer id){
        vaccineService.removeById(id);
        DataView dataView = new DataView();
        dataView.setCode(200);
        dataView.setMsg("添加疫苗成功！");
        return dataView;
    }
}
