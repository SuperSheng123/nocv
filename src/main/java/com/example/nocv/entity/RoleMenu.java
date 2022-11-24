package com.example.nocv.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("role_menu")
public class RoleMenu {
    private Integer rid;//角色id
    private Integer mid;//菜单id
}
