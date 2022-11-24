package com.example.nocv.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.nocv.entity.NocvNews;

import java.util.List;

public interface NocvNewsService extends IService<NocvNews> {
    List<NocvNews> listNewsLimit5();
}
