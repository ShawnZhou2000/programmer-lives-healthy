package com.abo.programmerliveshealthy.controller;

import com.abo.programmerliveshealthy.dto.ResultDTO;
import com.abo.programmerliveshealthy.entities.EmailCode;
import com.abo.programmerliveshealthy.entities.User;
import com.abo.programmerliveshealthy.service.EmailCodeService;
import com.abo.programmerliveshealthy.service.UserService;
import com.abo.programmerliveshealthy.util.MyMailUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * @author abo
 * @date 2020/7/3 16:47
 * @remarks
 **/
@Controller
@Slf4j
public class MailController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailCodeService emailCodeService;

    @ResponseBody
    @GetMapping("/getMailCode")
    public ResultDTO getMailCode(@PathParam("email") String email,
                                 @PathParam("type") Integer type,
                                 Model model){
        MyMailUtil myMailUtil = new MyMailUtil();
        if(type == 1){
            if(emailCodeService.isEmailExist(email)){
                model.addAttribute("msg", "该邮箱已经被注册过了,请换一个。");
                return new ResultDTO(4001, "该邮箱已经被注册过了。", null);
            }
        }
        try{
            String code = myMailUtil.generateVerificationCode();
            EmailCode emailCode = new EmailCode();
            emailCode.setIsUsed(0);
            emailCode.setCode(code);
            emailCode.setGmtCreate(System.currentTimeMillis());
            emailCodeService.getBaseMapper().insert(emailCode);
            myMailUtil.sendMail(email, code);
            return new ResultDTO(200, "邮件发送成功!", null);
        }catch (Exception e){
            log.info(e.toString());
            return new ResultDTO(1004, "邮件发送失败!", null);
        }
    }


}
