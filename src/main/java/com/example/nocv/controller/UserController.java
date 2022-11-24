package com.example.nocv.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nocv.entity.BanJi;
import com.example.nocv.entity.User;
import com.example.nocv.entity.XueYuan;
import com.example.nocv.service.BanJiService;
import com.example.nocv.service.RoleService;
import com.example.nocv.service.UserService;
import com.example.nocv.service.XueYuanService;
import com.example.nocv.vo.DataView;
import com.example.nocv.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BanJiService banJiService;

    @Autowired
    private XueYuanService xueYuanService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("toUser")
    public String toUser(){
        return "user/user";
    }

    @RequestMapping("/toChangePassword")
    public String toChangePassword(){
        return "user/changepassword";
    }

    @RequestMapping("/toUserInfo")
    public String toUserInfo(){
        return "user/userInfo";
    }

    /*
    * 分页查询所有的数据
    * */
    @RequestMapping("/loadAllUser")
    @ResponseBody
    public DataView getAllUser(UserVo userVo){
        //1.第一种方法
        /*if (StringUtils.isNotBlank(userVo.getUsername())) {
            userService.loadUserByLeftJoin(userVo.getUsername(),userVo.getPage(),userVo.getLimit());
        }*/
        //2.mapper
        //@Select("select)




        //2.第二种方法
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        IPage<User> page = new Page<>(userVo.getPage(),userVo.getLimit());
        queryWrapper.like(StringUtils.isNotBlank(userVo.getUsername()),"username",userVo.getUsername());
        queryWrapper.eq(StringUtils.isNotBlank(userVo.getPhone()),"phone",userVo.getPhone());
        IPage<User> page1 = userService.page(page, queryWrapper);
        //对班级名称进行赋值
        for (User user:page1.getRecords()){
            if (user.getBanJiId()!=null){
                //班级service查库
                BanJi banJi = banJiService.getById(user.getBanJiId());
                user.setBanJiName(banJi.getName());
            }
            if (user.getXueYuanId()!=null){
                //给学院名称赋值
                XueYuan xueYuan = xueYuanService.getById(user.getXueYuanId());
                user.setXueYuanName(xueYuan.getName());
            }
            if (user.getTeacherId()!=null){
                User user1 = userService.getById(user.getTeacherId());
                user.setTeacherName(user1.getUsername());
            }
        }


        return new DataView(page1.getTotal(),page1.getRecords());
    }

    /*
    * 新增用户
    *
    * */

    @RequestMapping("/addUser")
    @ResponseBody
    public DataView addUser(User user){
        userService.save(user);
        DataView dataView = new DataView();
        dataView.setMsg("添加用户成功");
        dataView.setCode(200);
        return dataView;
    }

    /*
    * 初始化下拉列表的数据【班级】
    *
    * */

    @RequestMapping("/listAllBanJi")
    @ResponseBody
    public List<BanJi> listAllBanJi(){
        List<BanJi> list = banJiService.list();
        return list;
    }


    /*
     * 初始化下拉列表的数据【班级】
     *
     * */
    @RequestMapping("/listAllXueYuan")
    @ResponseBody
    public List<XueYuan> listAllXueYuan(){
        List<XueYuan> list = xueYuanService.list();
        return list;
    }

    /*
    * 修改用户
    * */

    @RequestMapping("/updateUser")
    @ResponseBody
    public DataView updateUser(User user){
        boolean b = userService.updateById(user);
        DataView dataView = new DataView();
        if (b){
            dataView.setCode(200);
            dataView.setMsg("修改成功");
            return dataView;
        }
        dataView.setCode(100);
        dataView.setMsg("修改失败");
        return dataView;

    }

    /*
    * 删除用户
    * */

    @RequestMapping("/deleteUser/{id}")
    @ResponseBody
    public DataView deleteUser(@PathVariable("id") Integer id){
        DataView dataView = new DataView();
        if (id!=null){
            userService.removeById(id);
            dataView.setMsg("删除成功");
            dataView.setCode(200);
            return dataView;
        }
        dataView.setMsg("删除失败");
        dataView.setCode(100);
        return dataView;
    }

    /*
    * 重置密码
    *
    * */
    @RequestMapping("/resetPwd/{id}")
    @ResponseBody
    public DataView resetPwd(@PathVariable("id") Integer id){
            User user = new User();
            user.setPassword("123456");
            user.setId(id);
            userService.updateById(user);
            DataView  dataView = new DataView();
            dataView.setMsg("重置密码成功！");
            dataView.setCode(200);
            return dataView;

    }

    /*
    * 点击分配 初始化用户的角色
    * 根据id查询所拥有的角色
    *
    * */
    @RequestMapping("/initRoleByUserId")
    @ResponseBody
    public DataView initRoleByUserId(Integer id){
        //1.查询所有角色
        List<Map<String, Object>> listMaps = roleService.listMaps();
        //2.查询当前登录用户所拥有的角色
        List<Integer> currentUserRoleIds = roleService.queryUserRoleById(id);
        //3.让你的前端 变为选择状态
        for (Map<String,Object> map : listMaps){
            Boolean LAY_CHECKED = false;
            Integer roleId = (Integer) map.get("id");
            for (Integer rid:currentUserRoleIds){
                if (rid.equals(roleId)){
                    LAY_CHECKED=true;
                    break;
                }
            }
            map.put("LAY_CHECKED",LAY_CHECKED);
        }
        return  new DataView(Long.valueOf(listMaps.size()),listMaps);

    }

    /*
    * 保存用户
    *先删除在保存关系
    * */

    @RequestMapping("/saveUserRole")
    @ResponseBody
    public DataView saveUserRole(Integer uid,Integer[] ids){
        userService.saveUserRole(uid,ids);
        DataView dataView = new DataView();
        dataView.setCode(200);
        dataView.setMsg("用户的角色分配成功");
        return dataView;
    }



}
