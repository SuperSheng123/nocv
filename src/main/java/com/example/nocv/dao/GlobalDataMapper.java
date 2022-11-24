package com.example.nocv.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nocv.entity.NocvGlobalData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GlobalDataMapper extends BaseMapper<NocvGlobalData> {
}
