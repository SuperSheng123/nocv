package com.example.nocv.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.nocv.entity.ChinaTotal;
import org.springframework.stereotype.Service;


public interface ChinaTotalService extends IService<ChinaTotal> {
    Integer maxID();
}
