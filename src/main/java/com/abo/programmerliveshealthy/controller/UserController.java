package com.abo.programmerliveshealthy.controller;

import com.abo.programmerliveshealthy.dto.MessageDTO;
import com.abo.programmerliveshealthy.entities.Ug;
import com.abo.programmerliveshealthy.entities.User;
import com.abo.programmerliveshealthy.service.MessageService;
import com.abo.programmerliveshealthy.service.UgService;
import com.abo.programmerliveshealthy.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author abo
 * @date 2020/7/4 0:03
 * @remarks
 **/
@Controller
public class UserController {

    @Value("${file.upload.path}")
    private String filePath;
    @Value("${file.upload.path.relative}")
    private String fileRelativePath;
    @Autowired
    private UserService userService;
    @Autowired
    private UgService ugService;
    @Autowired
    private MessageService messageService;

    @GetMapping("/userinfo")
    public String userInfo(@PathParam("id") Integer id,
                           HttpSession session,
                           Model model){
        User user = userService.getBaseMapper().selectById(id);
        Integer loginId = (Integer)session.getAttribute("loginId");
        User loginUser = userService.getBaseMapper().selectById(loginId);
        Boolean userType;
        Integer totalDay = 0;
        Integer totalMoney = 0;
        QueryWrapper<Ug> ugQueryWrapper = new QueryWrapper<Ug>();
        ugQueryWrapper.eq("user_id", id);
        List<Ug> ugList = ugService.getBaseMapper().selectList(ugQueryWrapper);
        List<MessageDTO> commonMessageList = messageService.getCommonMessageList(loginId);
        List<MessageDTO> adminMessageList = messageService.getAdminMessageList(loginId);
        for(Ug ug : ugList){
            totalDay += ug.getTotalDay();
            totalMoney += ug.getTotalMoney();
        }
        if(id.equals(loginId)){
            userType = true;
        }else{
            userType = false;
        }

        model.addAttribute("commonMessageList", commonMessageList);
        model.addAttribute("adminMessageList", adminMessageList);
        model.addAttribute("userInfo", user);
        model.addAttribute("user", loginUser);
        model.addAttribute("userType", userType);
        model.addAttribute("totalDay", totalDay);
        model.addAttribute("totalMoney", totalMoney);
        return "userinfo";
    }

    @PostMapping("/updateUserInfo")
    public String updateUserInfo(@RequestParam(value = "usrname", required = false) String username,
                                 @RequestParam(value = "usrqq", required = false) String qq,
                                 @RequestParam(value = "usrbio", required = false) String bio,
                                 @RequestParam(value = "updavatar", required = false)MultipartFile avatar,
                                 HttpSession session,
                                 Model model) throws IOException {
        Integer loginId = (Integer)session.getAttribute("loginId");
        User loginUser = userService.getBaseMapper().selectById(loginId);
        Integer totalDay = 0;
        Integer totalMoney = 0;
        if(username != null && !"".equals(username)){
            loginUser.setUsername(username);
        }
        if(qq != null && !"".equals(qq)){
            loginUser.setQq(qq);
        }
        if(bio != null && !"".equals(bio)){
            loginUser.setBio(bio);
        }
        if(!avatar.isEmpty()) {
            String path = filePath;
            String ipath = fileRelativePath.substring(0, 5);
            Long mills = +System.currentTimeMillis();
            String filename = mills.toString() + avatar.getOriginalFilename();
            File filepath = new File(path, filename);
            if (!filepath.getParentFile().exists()) {
                filepath.getParentFile().mkdirs();
            }
            avatar.transferTo(new File(path + File.separator + filename));
            loginUser.setAvatar(ipath + filename);
        }
        userService.getBaseMapper().updateById(loginUser);
        QueryWrapper<Ug> ugQueryWrapper = new QueryWrapper<>();
        ugQueryWrapper.eq("user_id", loginId);
        List<Ug> ugList = ugService.getBaseMapper().selectList(ugQueryWrapper);
        for(Ug ug : ugList){
            totalDay += ug.getTotalDay();
            totalMoney += ug.getTotalMoney();
        }
        model.addAttribute("userInfo", loginUser);
        model.addAttribute("user", loginUser);
        model.addAttribute("userType", true);
        model.addAttribute("totalDay", totalDay);
        model.addAttribute("totalMoney", totalMoney);
        return "userinfo";
    }
}
