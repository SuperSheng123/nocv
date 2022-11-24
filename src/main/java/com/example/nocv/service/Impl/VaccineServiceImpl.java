package com.example.nocv.service.Impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nocv.dao.VaccineMapper;
import com.example.nocv.entity.Vaccine;
import com.example.nocv.service.VaccineService;
import org.springframework.stereotype.Service;

@Service
public class VaccineServiceImpl extends ServiceImpl<VaccineMapper,Vaccine> implements VaccineService {
}
