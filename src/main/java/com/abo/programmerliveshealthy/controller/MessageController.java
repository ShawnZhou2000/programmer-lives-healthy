package com.abo.programmerliveshealthy.controller;

import com.abo.programmerliveshealthy.dto.MessageDTO;
import com.abo.programmerliveshealthy.dto.ResultDTO;
import com.abo.programmerliveshealthy.entities.Message;
import com.abo.programmerliveshealthy.entities.User;
import com.abo.programmerliveshealthy.service.MessageService;
import com.abo.programmerliveshealthy.service.UserService;
import com.abo.programmerliveshealthy.util.MyFileUpload;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author abo
 * @date 2020/7/5 10:00
 * @remarks
 **/
@Controller
@Slf4j
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private MyFileUpload myFileUpload;

    @ResponseBody
    @PostMapping(value = "/sendMessage")
    public ResultDTO sendMessage(@RequestParam(value = "fromId") Integer fromId,
                                 @RequestParam(value = "toId") Integer toId,
                                 @RequestParam(value = "content") String content,
                                 @RequestParam(value = "img",required = false) MultipartFile img) throws IOException {
        try{
            Message message = new Message();
            if (img != null && !img.isEmpty()) {
                String imgName = myFileUpload.uploadImg(img);
                message.setImg(imgName);
            }else{
                message.setImg("");
            }
            message.setFromUser(fromId);
            message.setToUser(toId);
            message.setContent(content);
            message.setGmtCreate(System.currentTimeMillis());
            //非系统通知
            message.setType(0);
            //未读
            message.setReadFlag(0);
            messageService.getBaseMapper().insert(message);
            return new ResultDTO(200, "发送消息成功。", null);
        }catch (Exception e){
            log.info("发送消息异常" + e.toString());
            return new ResultDTO(1004, "发送消息失败。", null);
        }
    }

    @GetMapping("/message")
    public String getMessage(HttpSession session,
                             Model model){
        Integer userId = (Integer)session.getAttribute("loginId");
        User user = userService.getBaseMapper().selectById(userId);
        QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<Message>();
        messageQueryWrapper.eq("to_user", userId);
        List<Message> messages = messageService.getBaseMapper().selectList(messageQueryWrapper);
        List<MessageDTO> messageList = new ArrayList<MessageDTO>();
        List<MessageDTO> commonMessageList = messageService.getCommonMessageList(userId);
        List<MessageDTO> adminMessageList = messageService.getAdminMessageList(userId);
        for(Message message : messages){
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setId(message.getId());
            messageDTO.setContent(message.getContent());
            messageDTO.setGmtCreate(message.getGmtCreate());
            messageDTO.setType(message.getType());
            messageDTO.setReadFlag(message.getReadFlag());
            messageDTO.setToUser(userId);
            User tempUser = userService.getBaseMapper().selectById(message.getFromUser());
            messageDTO.setFromUser(tempUser);
            messageList.add(messageDTO);
        }
        Collections.sort(messageList, (Comparator) (o1, o2) -> {
            MessageDTO m1 = (MessageDTO) o1;
            MessageDTO m2 = (MessageDTO) o2;
            return m2.getGmtCreate().compareTo(m1.getGmtCreate());
        });
        if(messageList.isEmpty()){
            model.addAttribute("messageDTO", null);
        }else{
            model.addAttribute("messageDTO", messageList.get(0));
        }
        model.addAttribute("commonMessageList", commonMessageList);
        model.addAttribute("adminMessageList", adminMessageList);
        model.addAttribute("user", user);
        model.addAttribute("messageList", messageList);
        return "message";
    }

    @GetMapping("/loadMessage")
    public String loadMessage(@PathParam("messageId") Integer messageId,
                              Model model){
        MessageDTO messageDTO = new MessageDTO();
        Message message = messageService.getBaseMapper().selectById(messageId);
        User tempUser = userService.getBaseMapper().selectById(message.getFromUser());
        messageDTO.setFromUser(tempUser);
        messageDTO.setContent(message.getContent());
        if("".equals(message.getImg())){
            messageDTO.setImg(null);
        }else{
            messageDTO.setImg(message.getImg());
        }
        messageDTO.setId(message.getId());
        messageDTO.setGmtCreate(message.getGmtCreate());
        messageDTO.setType(message.getType());
        messageDTO.setReadFlag(message.getReadFlag());
        messageDTO.setToUser(message.getToUser());

        model.addAttribute("messageDTO", messageDTO);
        return "message::reply";
    }
}
