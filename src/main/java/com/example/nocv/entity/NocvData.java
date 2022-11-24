package com.example.nocv.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

//提供对外访问的方法get set
@Data
//映射表的字段
@TableName("nocv_data")
public class NocvData {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private  String name;
    private Integer value;
    private Date updateTime;
}
