package com.example.nocv.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nocv.dao.RoleMapper;
import com.example.nocv.entity.Role;
import com.example.nocv.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    RoleMapper roleMapper;
    @Override
    public List<Integer> queryAllPermissionById(Integer roleId) {
        return roleMapper.queryMidByRid(roleId);
    }

    @Override
    public void savaRoleMenu(Integer rid, Integer mid) {
        roleMapper.savaRoleMenu(rid,mid);
    }

    @Override
    public void deleteRoleByRid(Integer rid) {
        roleMapper.deleteRoleByRid(rid);
    }

    @Override
    public List<Integer> queryUserRoleById(Integer id) {
        return roleMapper.queryUserRoleById(id);
    }
}
