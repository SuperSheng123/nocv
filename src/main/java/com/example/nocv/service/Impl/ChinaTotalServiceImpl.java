package com.example.nocv.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nocv.dao.ChinaTotalMapper;
import com.example.nocv.entity.ChinaTotal;
import com.example.nocv.service.ChinaTotalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChinaTotalServiceImpl extends ServiceImpl<ChinaTotalMapper, ChinaTotal> implements ChinaTotalService {

    @Autowired
    private ChinaTotalMapper chinaTotalMapper;
    @Override
    public Integer maxID() {
        return chinaTotalMapper.maxID();
    }
}
