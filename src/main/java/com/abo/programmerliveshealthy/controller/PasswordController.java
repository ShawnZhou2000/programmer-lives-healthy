package com.abo.programmerliveshealthy.controller;

import cn.hutool.crypto.SecureUtil;
import com.abo.programmerliveshealthy.entities.EmailCode;
import com.abo.programmerliveshealthy.entities.User;
import com.abo.programmerliveshealthy.service.EmailCodeService;
import com.abo.programmerliveshealthy.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author abo
 * @date 2020/7/4 16:58
 * @remarks
 **/
@Controller
public class PasswordController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailCodeService emailCodeService;

    @GetMapping("/resetPassword")
    public String toResetPassword(){
        return "password-social";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam(value = "email") String email,
                                @RequestParam(value = "newPassword") String newPassword,
                                @RequestParam(value = "verificationCode") String code,
                                Model model){
        if(email == null || "".equals(email) || newPassword == null || "".equals(newPassword) || code == null || "".equals(code)){
            model.addAttribute("msg", "信息不能为空,请填写完整。");
            return "password-social";
        }
        if(!emailCodeService.isEmailExist(email)){
            model.addAttribute("msg", "该邮箱还未注册,请先注册。");
            return "password-social";
        }
        if(!emailCodeService.isEmailCodeExist(code)){
            model.addAttribute("msg", "验证码错误,请重试。");
            return "password-social";
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("email", email);
        User user = userService.getBaseMapper().selectOne(queryWrapper);
        user.setPassword(SecureUtil.md5(newPassword));
        userService.getBaseMapper().updateById(user);
        model.addAttribute("msg", "密码修改成功,请登录。");
        return "login-social";
    }

}
