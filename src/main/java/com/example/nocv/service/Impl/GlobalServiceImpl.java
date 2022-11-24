package com.example.nocv.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nocv.dao.GlobalDataMapper;
import com.example.nocv.entity.NocvGlobalData;
import com.example.nocv.service.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GlobalServiceImpl extends ServiceImpl<GlobalDataMapper, NocvGlobalData> implements GlobalService {

    @Autowired
    private GlobalDataMapper globalDataMapper;

}
