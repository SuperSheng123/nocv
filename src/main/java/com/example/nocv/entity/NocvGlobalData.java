package com.example.nocv.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("nocv_global_data")
public class NocvGlobalData {
/*
    @TableId(value = "id")
    private Integer id;*/
    private  String name;
    private Integer value;
}
