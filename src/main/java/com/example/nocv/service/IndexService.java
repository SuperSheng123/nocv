package com.example.nocv.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.nocv.entity.LineTrend;
import com.example.nocv.entity.NocvData;

import java.util.List;

public interface IndexService extends IService<NocvData> {
    List<LineTrend> findSevenData();

    List<NocvData> listOrderByIdLimit34();
}
