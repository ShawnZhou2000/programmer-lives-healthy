package com.abo.programmerliveshealthy.service.impl;

import com.abo.programmerliveshealthy.entities.Ug;
import com.abo.programmerliveshealthy.entities.User;
import com.abo.programmerliveshealthy.mapper.UgMapper;
import com.abo.programmerliveshealthy.mapper.UserMapper;
import com.abo.programmerliveshealthy.service.UgService;
import com.abo.programmerliveshealthy.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author abo
 * @date 2020/6/30 13:57
 * @remarks
 **/
@Service
public class UgServiceImpl extends ServiceImpl<UgMapper, Ug> implements UgService {
    @Override
    public void resetSign() {
        this.getBaseMapper().resetSign();
    }
}
