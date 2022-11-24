package com.example.nocv.controller;
/*
* 专门跳转页面
* */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApiController {

    //跳转页面
    @RequestMapping("/toChinaManager")
    public String toChinaManager() {
        return "admin/chinadatamanger";
    }

    @RequestMapping("/banji/toBanJi")
    public String toXueYuan(){
        return "banji/banji";
    }
}
