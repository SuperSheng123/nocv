package com.example.nocv.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nocv.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {


    @Select("select * from user where username=#{username} and #{password} = password")
    User login(String username, String password);

    void deleteRoleUserByUid(Integer uid);
}
