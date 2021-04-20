package com.abo.programmerliveshealthy;

import cn.hutool.core.lang.UUID;
import cn.hutool.crypto.SecureUtil;
import com.abo.programmerliveshealthy.entities.User;
import com.abo.programmerliveshealthy.service.UserService;
import com.abo.programmerliveshealthy.util.MyMailUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ProgrammerliveshealthyApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        QueryWrapper queryWrapper = new QueryWrapper();
        List<User> users = userService.getBaseMapper().selectList(queryWrapper);
        for(User user : users){
            System.out.println(SecureUtil.md5(user.getPassword()));
        }
    }

}
