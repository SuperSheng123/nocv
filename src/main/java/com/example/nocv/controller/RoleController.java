package com.example.nocv.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nocv.entity.Menu;
import com.example.nocv.entity.Role;
import com.example.nocv.service.MenuService;
import com.example.nocv.service.RoleService;
import com.example.nocv.utlis.TreeNode;
import com.example.nocv.vo.DataView;
import com.example.nocv.vo.RoleVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @RequestMapping("/toRole")
    public String toRole(){
        return "role/role";
    }

    /*
    * 查询所有的角色，带有分页
    * */

    @RequestMapping("loadAllRole")
    @ResponseBody
    public DataView load(RoleVo roleVo){
        IPage<Role> page = new Page<>(roleVo.getPage(),roleVo.getLimit());
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.like(StringUtils.isNotBlank(roleVo.getName()),"name",roleVo.getName());
        queryWrapper.like(StringUtils.isNotBlank(roleVo.getRemark()),"remark",roleVo.getRemark());
        roleService.page(page,queryWrapper);
        return new DataView(page.getTotal(),page.getRecords());
    }

    /*
    * 添加角色
    * */

    @RequestMapping("/addRole")
    @ResponseBody
    public DataView addRole(Role role){
        boolean save = roleService.save(role);
        DataView dataView = new DataView();
        if (save){
            dataView.setMsg("添加成功");
            dataView.setCode(200);
            return dataView;
        }
        dataView.setMsg("添加失败");
        dataView.setCode(100);
        return dataView;
    }

    @RequestMapping("/deleteRole")
    @ResponseBody
    public DataView deleteRole(Role role){
        boolean save = roleService.removeById(role.getId());
        DataView dataView = new DataView();
        if (save){
            dataView.setMsg("删除成功");
            dataView.setCode(200);
            return dataView;
        }
        dataView.setMsg("删除失败");
        dataView.setCode(100);
        return dataView;
    }

    @RequestMapping("/updateRole")
    @ResponseBody
    public DataView updateRole(Role role){
        boolean save = roleService.saveOrUpdate(role);
        DataView dataView = new DataView();
        if (save){
            dataView.setMsg("修改成功");
            dataView.setCode(200);
            return dataView;
        }
        dataView.setMsg("修改失败");
        dataView.setCode(100);
        return dataView;
    }

    /*
    *
    * 初始化下拉列表的权限
    * */

    @RequestMapping("/initPermissionByRoleId")
    @ResponseBody
    public DataView initPermissionByRoleId(Integer roleId){
//        1.把所有的菜单权限查出来
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        List<Menu> allPermissions = menuService.list();
//        2.根据角色id查询所有的菜单
        List<Integer> currentRolePermissions = roleService.queryAllPermissionById(roleId);
//        3.rid mid  所有的的id，去查询菜单和角色的数据
        List<Menu> menus = null;
//        4.查询到 mid rid
        if (currentRolePermissions.size()>0){
            queryWrapper.in("id",currentRolePermissions);
            menus = menuService.list(queryWrapper);
        }else {
            menus = new ArrayList<>();
        }
        //5.返回前台的格式，带有层级关系 TreeNode
        List<TreeNode> nodes = new ArrayList<>();
        for (Menu allPermission:allPermissions){
            String checkArr = "0";
            for (Menu currentPermission : menus){
                if (allPermission.getId().equals(currentPermission.getId())){
                    checkArr="1";
                    break;
                }
            }
            boolean spread = (allPermission.getOpen()==null || allPermission.getOpen() == 1) ? true : false;
            nodes.add(new TreeNode(allPermission.getId(),allPermission.getPid(),allPermission.getTitle(),spread,checkArr));
        }

        return new DataView(nodes);

    }

    /*
    *点击确认分配权限的时候
    *插入数据库表
    * */

    @RequestMapping("/saveRolePermission")
    @ResponseBody
    public DataView saveRolePermission(Integer rid,Integer[] mids){
        //1.根据rid删除之前的权限
        roleService.deleteRoleByRid(rid);
        if (mids!=null && mids.length>0){
            for (Integer mid:mids){
                roleService.savaRoleMenu(rid,mid);
            }
        }
        DataView dataView = new DataView();
        dataView.setCode(200);
        dataView.setMsg("权限分配成功");

        return dataView;
    }

}
