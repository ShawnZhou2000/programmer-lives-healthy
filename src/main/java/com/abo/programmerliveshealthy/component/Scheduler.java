package com.abo.programmerliveshealthy.component;

import com.abo.programmerliveshealthy.entities.*;
import com.abo.programmerliveshealthy.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author abo
 * @date 2020/7/1 11:57
 * @remarks
 **/
@Component
@Slf4j
public class Scheduler {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private UgService ugService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private EmailCodeService emailCodeService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 0 0 ? * *")
    public void resetSignData(){
        QueryWrapper<Ug> ugQueryWrapper = new QueryWrapper<Ug>();
        List<Ug> ugList = ugService.getBaseMapper().selectList(ugQueryWrapper);
        for(Ug ug: ugList){
            if(ug.getSign() == 0){
                Group group = groupService.getBaseMapper().selectById(ug.getGroupId());
                ug.setTotalMoney(ug.getTotalMoney() + group.getMoney());
                ugService.getBaseMapper().updateById(ug);
            }
        }
        ugService.resetSign();
        log.info("红包数据已更新，打卡数据已经清除,时间: " + dateFormat.format(new Date()));
    }

    @Scheduled(fixedRate = 1000*60*60)
    public void resetEmailCodeData(){
        QueryWrapper<EmailCode> queryWrapper = new QueryWrapper<EmailCode>();
        emailCodeService.getBaseMapper().delete(queryWrapper);
        log.info("验证码已清除");
    }

    @Scheduled(cron = "0 0 12 ? * *")
    public void sendSystemMessage(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        List<User> users = userService.getBaseMapper().selectList(queryWrapper);
        for(User user : users){
            Message message = new Message();
            message.setImg("");
            message.setFromUser(1);
            message.setToUser(user.getId());
            message.setContent("今天不要忘记打卡哦!祝你渡过充实愉快的一天。");
            message.setGmtCreate(System.currentTimeMillis());
            //系统通知
            message.setType(1);
            //未读
            message.setReadFlag(0);
            messageService.getBaseMapper().insert(message);
        }
        log.info("发送系统通知成功");
    }
}
