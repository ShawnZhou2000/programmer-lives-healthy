package com.abo.programmerliveshealthy.service.impl;

import com.abo.programmerliveshealthy.entities.User;
import com.abo.programmerliveshealthy.mapper.UserMapper;
import com.abo.programmerliveshealthy.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author abo
 * @date 2020/6/28 13:08
 * @remarks
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
