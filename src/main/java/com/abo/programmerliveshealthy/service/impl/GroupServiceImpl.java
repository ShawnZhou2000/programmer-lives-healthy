package com.abo.programmerliveshealthy.service.impl;

import com.abo.programmerliveshealthy.entities.Group;
import com.abo.programmerliveshealthy.entities.User;
import com.abo.programmerliveshealthy.mapper.GroupMapper;
import com.abo.programmerliveshealthy.mapper.UserMapper;
import com.abo.programmerliveshealthy.service.GroupService;
import com.abo.programmerliveshealthy.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author abo
 * @date 2020/6/30 13:56
 * @remarks
 **/
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group> implements GroupService {
}
