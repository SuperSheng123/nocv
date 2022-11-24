package com.example.nocv.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ban_ji")
public class BanJi {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer xueYuanId;
}
