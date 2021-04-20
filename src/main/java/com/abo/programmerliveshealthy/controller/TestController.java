package com.abo.programmerliveshealthy.controller;

import com.abo.programmerliveshealthy.entities.User;
import com.abo.programmerliveshealthy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class TestController {
    @Autowired
    private UserService userService;

//    @GetMapping("/message")
//    public String message(HttpSession session,
//                          Model model) {
//        Integer id = (Integer) session.getAttribute("loginId");
//        User user = userService.getBaseMapper().selectById(id);
//        model.addAttribute("user", user);
//        return "message";
//    }

//    @GetMapping("/managegroup")
//    public String manageGroup(HttpSession session, Model model) {
//        Integer id = (Integer) session.getAttribute("loginId");
//        User user = userService.getBaseMapper().selectById(id);
//        model.addAttribute("user", user);
//        return "managegroup";
//    }



}
