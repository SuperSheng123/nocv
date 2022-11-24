package com.example.nocv.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/*
* id create_time update_time
* */

@Data
@TableName("menu")
public class Menu {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer pid;
    private String type;
    private String title;
    private String permission;
    private String icon;
    private String href;
    private Integer open;
    private Integer ordernum;
    private Integer available;

}
