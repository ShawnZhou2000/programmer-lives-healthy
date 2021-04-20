package com.abo.programmerliveshealthy.service;

import com.abo.programmerliveshealthy.entities.EmailCode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author abo
 * @date 2020/7/4 2:46
 * @remarks
 **/
public interface EmailCodeService extends IService<EmailCode> {

    Boolean isEmailExist(String email);
    Boolean isEmailCodeExist(String code);
}
