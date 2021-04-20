package com.abo.programmerliveshealthy.service;

import com.abo.programmerliveshealthy.entities.Ug;
import com.abo.programmerliveshealthy.entities.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author abo
 * @date 2020/6/30 13:56
 * @remarks
 **/
public interface UgService extends IService<Ug> {
    void resetSign();
}
