package com.example.nocv.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.example.nocv.entity.User;
import com.example.nocv.service.UserService;
import com.example.nocv.vo.DataView;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    /*
    * 验证码逻辑
    * */
    @RequestMapping("/login/getCode")
    public void getCode(HttpServletResponse response, HttpSession session) throws IOException {

        //1.验证码对象 HuTool定义图形验证码的长和宽，验证码的位数，干扰线的条数
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(116, 36, 4, 10);
        //2.放到session
        session.setAttribute("code",lineCaptcha.getCode());
        //3.输出验证码
        ServletOutputStream outputStream = response.getOutputStream();
        lineCaptcha.write(outputStream);
        //关闭输出流
        outputStream.close();
    }

    /*
    * 具体的登录逻辑
    * */

    @RequestMapping("/login/login")
    @ResponseBody
    public DataView login(String username,String password,String code,HttpSession session){

        DataView dataView = new DataView();

        //1.首先判断验证码
        String sessionCode = (String)session.getAttribute("code");
        if (code!=null && sessionCode.equals(code)){
            //2.session普通登录逻辑
//            User user = userService.login(username,password);
            //shiro登录
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username,password);
            subject.login(token);
            User user = (User) subject.getPrincipal();
            //3.判断
            if (user!=null){
                dataView.setCode(200);
                dataView.setMsg("登录成功");
                session.setAttribute("user",user);
                return dataView;
            }else {
                dataView.setCode(100);
                dataView.setMsg("用户名或者密码错误，登录失败！");
                return dataView;
            }
        }
        dataView.setCode(100);
        dataView.setMsg("验证码错误！");
        return dataView;
    }

}
