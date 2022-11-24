package com.example.nocv.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.nocv.entity.Menu;
import com.example.nocv.entity.User;
import com.example.nocv.service.MenuService;
import com.example.nocv.service.RoleService;
import com.example.nocv.utlis.TreeNode;
import com.example.nocv.utlis.TreeNodeBuilder;
import com.example.nocv.vo.DataView;
import com.example.nocv.vo.MenuVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.data.GraphQlQueryByExampleAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("toMenu")
    public String toMenu(){
        return "menu/menu";
    }

    /*
    * 加载所有的菜单
    *
    * */
    @RequestMapping("/loadAllMenu")
    @ResponseBody
    public DataView loadAllMenu(MenuVo menuVo){
        IPage<Menu> page = new Page<>(menuVo.getPage(),menuVo.getLimit());
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(menuVo.getTitle()),"title",menuVo.getTitle());
        queryWrapper.orderByAsc("ordernum");
        menuService.page(page,queryWrapper);
        return new DataView(page.getTotal(),page.getRecords());
    }

    /*
    * 加载下拉菜单数据 和左侧数据dtree
    * */

    @RequestMapping("/loadMenuManagerLeftTreeJson")
    @ResponseBody
    public DataView loadMenuManagerLeftTreeJson(){
        List<Menu> list = menuService.list();
        List<TreeNode> treeNodeList = new ArrayList<>();
        for (Menu menu:list){
            Boolean open = menu.getOpen() == 1?true:false;
            treeNodeList.add(new TreeNode(menu.getId(),menu.getPid(),menu.getTitle(),open));
        }
        return new DataView(treeNodeList);
    }

    /*
    * 加载赋值最大的排序吗+1
    * 条件查询：倒叙排序 取一条数据 +1
    * */

    @RequestMapping("/loadMenuMaxOrderNum")
    @ResponseBody
    public Map<String,Object> loadMenuMaxOrderNum(){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("ordernum");
        IPage<Menu> page = new Page<>(1,1);
        List<Menu> list = menuService.page(page, queryWrapper).getRecords();
        map.put("value",list.get(0).getOrdernum()+1);
        return map;
    }

    /*
    * 新增菜单
    * */
    @RequestMapping("/addMenu")
    @ResponseBody
    public DataView addMenu(Menu menu){
        DataView dataView = new DataView();
        menu.setType("menu");
        boolean save = menuService.save(menu);
        if (!save){
            dataView.setMsg("数据插入失败");
            dataView.setCode(100);
        }
        dataView.setMsg("数据插入成功");
        dataView.setCode(200);
        return dataView;
    }

    /*
    * 更新菜单
    * */

    @RequestMapping("/updateMenu")
    @ResponseBody
    public DataView updateMenu(Menu menu){
        boolean b = menuService.updateById(menu);
        DataView dataView = new DataView();
        if (b){
            dataView.setCode(200);
            dataView.setMsg("更新菜单成功！");
            return dataView;
        }
        dataView.setCode(100);
        dataView.setMsg("更新菜单失败！");
        return dataView;

    }


    /*
    * 判断有没有子级菜单，如果有子级菜单是不能被删除的
    *
    * */
    @RequestMapping("/checkMenuHasChildrenNode")
    @ResponseBody
    public Map<String,Object> checkChildrenNode(Menu menu){

        Map<String,Object> map = new HashMap<>();

        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pid",menu.getId());
        List<Menu> list = menuService.list(queryWrapper);
        if (list.size()>0){
            map.put("value",true);
        }else {
            map.put("value",false);
        }
        return map;
    }

    /*
    *
    * 真正的删除
    * */

    @RequestMapping("/deleteMenu")
    @ResponseBody
    public DataView deleteMenu(Menu menu){
        DataView dataView = new DataView();
        boolean b = menuService.removeById(menu.getId());
        if (b){
            dataView.setMsg("删除成功");
            dataView.setCode(200);
            return dataView;
        }
        dataView.setMsg("删除失败");
        dataView.setCode(100);
        return dataView;
    }

    /*
    * 加载主页面index的菜单栏，带有层级关系
    * 【不同的用户登录看到不同的菜单栏】
    * */
    @RequestMapping("/loadIndexLeftMenuJson")
    @ResponseBody
    public DataView loadIndexLeftMenuJson(Menu menu,HttpSession session){

        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        //查询所有菜单栏
        List<Menu> list = null;
        //按照条件查询 [管理员 老师 学生 等等]
        User user = (User)session.getAttribute("user");
        Integer UserId = user.getId();

        if (user.getUsername().equals("admin")||StringUtils.equals(user.getUsername(),"admin")){
            list = menuService.list();
        }else {
            //根据用户id查询角色
            List<Integer> currentUserRoleIds = roleService.queryUserRoleById(UserId);
            //去重
            Set<Integer> mids = new HashSet<>();
            for (Integer rid : currentUserRoleIds) {
                //根据角色id查询菜单id
                List<Integer> permissionIds = roleService.queryAllPermissionById(rid);
                //菜单栏id和角色id去重
                mids.addAll(permissionIds);
            }
            //根据角色id查询菜单
            if (mids.size() > 0) {
                queryWrapper.in("id", mids);
                list = menuService.list(queryWrapper);
            }
        }
        //构造层级关系
        List<TreeNode> treeNodes = new ArrayList<>();
        for (Menu m:list) {
            Integer id = m.getId();
            Integer pid = m.getPid();
            String title = m.getTitle();
            String icon = m.getIcon();
            String href = m.getHref();
            Boolean open = m.getOpen().equals(1)?true:false;
            treeNodes.add(new TreeNode(id,pid,title,icon,href,open));
        }

        List<TreeNode> list1 = TreeNodeBuilder.build(treeNodes, 0);



        return new DataView(list1);
    }

}
