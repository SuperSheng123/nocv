package com.example.nocv.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("hesuan")
public class HeSuan {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    private String name;
    private Integer age;
    private String phone;
    private String card;

    private String type;//混检 单件 咽喉 鼻式 ...
    private String result; //核算结果【未出结果，阴性，阳性】

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;//核酸检测时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;//检测结果出来时间

}
