package com.abo.programmerliveshealthy.controller;

import cn.hutool.crypto.SecureUtil;
import com.abo.programmerliveshealthy.entities.User;
import com.abo.programmerliveshealthy.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author abo
 * @date 2020/6/28 15:18
 * @remarks
 **/
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(){
        return "login-social";
    }

    @PostMapping("/login")
    public String login(@RequestParam(value = "email",required = false) String email,
                        @RequestParam(value = "password",required = false) String password,
                        @RequestParam(value = "flag", required = false) Boolean flag,
                        HttpServletResponse response,
                        HttpSession session,
                        Map<String, Object> map,
                        Model model){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        String md5Password = SecureUtil.md5(password);
        queryWrapper.eq("password", md5Password);
        User user = userService.getBaseMapper().selectOne(queryWrapper);
        if(user != null){
            if(flag != null && flag){
                Cookie emailCookie = new Cookie("email", email);
                Cookie passCookie = new Cookie("token", md5Password);
                emailCookie.setMaxAge(60 * 60 * 24 * 7);
                passCookie.setMaxAge(60 * 60 * 24 * 7);
                response.addCookie(emailCookie);
                response.addCookie(passCookie);
            }
            session.setAttribute("loginId", user.getId());
            return "redirect:/";
        }else{
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("email", email);
            queryWrapper.eq("password",password);
            user = userService.getBaseMapper().selectOne(queryWrapper);
            if(user != null){
                session.setAttribute("loginId", user.getId());
                return "redirect:/";
            }else {
                map.put("msg", "用户名或密码错误");
                return "login-social";
            }
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("loginId");
        return "login-social";
    }
}
