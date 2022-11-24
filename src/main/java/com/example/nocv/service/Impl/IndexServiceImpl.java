package com.example.nocv.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nocv.dao.IndexMapper;
import com.example.nocv.entity.LineTrend;
import com.example.nocv.entity.NocvData;
import com.example.nocv.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexServiceImpl extends ServiceImpl<IndexMapper, NocvData> implements IndexService {


    @Autowired
    private IndexMapper indexMapper;
    @Override
    public List<LineTrend> findSevenData() {
        List<LineTrend> list = indexMapper.findSevenData();
        return list;
    }

    @Override
    public List<NocvData> listOrderByIdLimit34() {
        return indexMapper.listOrderByIdLimit34();
    }
}
