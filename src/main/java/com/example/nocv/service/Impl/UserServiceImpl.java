package com.example.nocv.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nocv.dao.RoleMapper;
import com.example.nocv.dao.UserMapper;
import com.example.nocv.entity.User;
import com.example.nocv.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleMapper roleMapper;


    @Override
    public User login(String username, String password) {
        return userMapper.login(username,password);
    }

    @Override
    public void saveUserRole(Integer uid, Integer[] ids) {
        roleMapper.deleteRoleUserByUid(uid);
        if (ids!=null && ids.length>0){
            for (Integer rid:ids){
                roleMapper.savaUserRole(uid,rid);
            }
        }
    }
}
