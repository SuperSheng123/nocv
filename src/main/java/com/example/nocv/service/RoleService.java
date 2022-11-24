package com.example.nocv.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.nocv.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role>  {
    List<Integer> queryAllPermissionById(Integer roleId);

    void deleteRoleByRid(Integer rid);

    void savaRoleMenu(Integer rid, Integer mid);

    List<Integer> queryUserRoleById(Integer id);
}
