package com.abo.programmerliveshealthy.service.impl;

import com.abo.programmerliveshealthy.entities.EmailCode;
import com.abo.programmerliveshealthy.entities.Group;
import com.abo.programmerliveshealthy.entities.User;
import com.abo.programmerliveshealthy.mapper.EmailCodeMapper;
import com.abo.programmerliveshealthy.mapper.GroupMapper;
import com.abo.programmerliveshealthy.service.EmailCodeService;
import com.abo.programmerliveshealthy.service.GroupService;
import com.abo.programmerliveshealthy.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author abo
 * @date 2020/7/4 2:47
 * @remarks
 **/
@Service
public class EmailCodeServiceImpl extends ServiceImpl<EmailCodeMapper, EmailCode> implements EmailCodeService {

    @Autowired
    private UserService userService;

    @Override
    public Boolean isEmailExist(String email) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>();
        userQueryWrapper.eq("email", email);
        User user = userService.getBaseMapper().selectOne(userQueryWrapper);
        if(user != null){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Boolean isEmailCodeExist(String code) {
        QueryWrapper<EmailCode> emailCodeQueryWrapper = new QueryWrapper<EmailCode>();
        emailCodeQueryWrapper.eq("code", code);
        List<EmailCode> emailCodeList = this.getBaseMapper().selectList(emailCodeQueryWrapper);
        if(emailCodeList.isEmpty()){
            return false;
        }else{
            return true;
        }
    }
}
