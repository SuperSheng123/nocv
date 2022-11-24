package com.example.nocv.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("user")
@Data
public class User {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private String salt;

    private String sex;
    private Integer age;
    private String address;
    private String img;
    private String phone;
    private String card;

    /*
    * 作为外键使用
    * */
    private Integer banJiId;
    private Integer xueYuanId;
    private Integer teacherId;

    /*
    * 加这个注解表示非数据库列
    * */
    @TableField(exist = false)
    private String banJiName;
    @TableField(exist = false)
    private String xueYuanName;
    @TableField(exist = false)
    private String teacherName;
}

