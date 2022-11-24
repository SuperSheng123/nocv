package com.example.nocv.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nocv.entity.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    @Select("select mid from role_menu where rid = #{roleId}")
    List<Integer> queryMidByRid(Integer roleId);

    //分配菜单栏之前删除所有的数据
    @Delete("delete from role_menu where rid = #{rid}")
    void deleteRoleByRid(Integer rid);

    //保存分配，角色与菜单的关系
    @Insert("insert into role_menu(rid,mid) values(#{rid},#{mid})")
    void savaRoleMenu(Integer rid, Integer mid);

    //1.根据用户id查询所有的角色
    @Select("select rid from user_role where uid = #{id}")
    List<Integer> queryUserRoleById(Integer id);

    /*
    * 先删除用户与角色之间的关系
    * */
    @Delete("delete from user_role where uid = #{uid}")
    void deleteRoleUserByUid(Integer uid);

    /*
    * 保存新关系
    * */
    @Insert("insert into user_role(uid,rid) values(#{uid},#{rid})")
    void savaUserRole(Integer uid, Integer rid);


}
