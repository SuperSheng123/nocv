package com.example.nocv.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nocv.entity.LineTrend;
import com.example.nocv.entity.NocvData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface IndexMapper extends BaseMapper<NocvData> {

    /*
    * 接口：只有方法定义，写业务逻辑
    *
    * 1.实现类，谢你自己的业务逻辑
    * 2.xml mybatisplus 一种实现
    * 3.@Select
    * */
    @Select("select * from line_trend order by create_time limit 7")
    List<LineTrend> findSevenData();

    @Select("select * from nocv_data order by id desc limit 34")
    List<NocvData> listOrderByIdLimit34();
}
