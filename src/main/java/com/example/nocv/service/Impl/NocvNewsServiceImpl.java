package com.example.nocv.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.nocv.dao.NocvNewsMapper;
import com.example.nocv.entity.NocvNews;
import com.example.nocv.service.NocvNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NocvNewsServiceImpl extends ServiceImpl<NocvNewsMapper, NocvNews> implements NocvNewsService {

    @Autowired
    NocvNewsMapper nocvNewsMapper;
    @Override
    public List<NocvNews> listNewsLimit5() {
        return nocvNewsMapper.listNewsLimit5();
    }
}
