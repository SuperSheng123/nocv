package com.example.nocv.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.nocv.entity.Vaccine;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VaccineMapper extends BaseMapper<Vaccine> {
}
