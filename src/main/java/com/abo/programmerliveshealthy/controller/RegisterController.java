package com.abo.programmerliveshealthy.controller;

import cn.hutool.crypto.SecureUtil;
import com.abo.programmerliveshealthy.dto.UserDTO;
import com.abo.programmerliveshealthy.entities.User;
import com.abo.programmerliveshealthy.service.EmailCodeService;
import com.abo.programmerliveshealthy.service.UserService;
import com.abo.programmerliveshealthy.util.MyFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

/**
 * @author abo
 * @date 2020/6/28 12:54
 * @remarks
 **/
@Controller
public class RegisterController {

    @Autowired
    private UserService userService;
    @Autowired
    private EmailCodeService emailCodeService;
    @Autowired
    private MyFileUpload myFileUpload;

    @GetMapping("/register")
    public String register(){
        return "register-social";
    }

    @PostMapping(value = "/register")
    public String register(@ModelAttribute UserDTO userDTO,
                           Model model) throws IOException {
        if("".equals(userDTO.getEmail()) || "".equals(userDTO.getPassword()) || "".equals(userDTO.getQq())
        || "".equals(userDTO.getUsername()) || userDTO.getUsername() == null || userDTO.getQq() == null ||
        userDTO.getPassword() == null || userDTO.getEmail() == null){
            model.addAttribute("msg", "信息不能为空,请完善注册信息");
            return "register-social";
        }
        if(emailCodeService.isEmailExist(userDTO.getEmail())){
            model.addAttribute("msg", "该邮箱已经被注册过了,请换一个。");
            return "register-social";
        }
        if(!emailCodeService.isEmailCodeExist(userDTO.getVerificationCode())){
            model.addAttribute("msg", "验证码错误,请重试。");
            return "register-social";
        }
        if(!userDTO.getAvatar().isEmpty()){
            String imgName = myFileUpload.uploadImg(userDTO.getAvatar());
            User user = new User();
            user.setBio("这个人很懒，什么都没留下。");
            user.setEmail(userDTO.getEmail());
            user.setPassword(SecureUtil.md5(userDTO.getPassword()));
            user.setQq(userDTO.getQq());
            user.setUsername(userDTO.getUsername());
            user.setAvatar(imgName);
            userService.getBaseMapper().insert(user);
            return "login-social";
        }else{
            model.addAttribute("msg", "图片不能为空");
            return "register-social";
        }
    }


}
