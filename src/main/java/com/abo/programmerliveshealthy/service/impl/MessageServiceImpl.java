package com.abo.programmerliveshealthy.service.impl;

import com.abo.programmerliveshealthy.dto.MessageDTO;
import com.abo.programmerliveshealthy.entities.Message;
import com.abo.programmerliveshealthy.entities.User;
import com.abo.programmerliveshealthy.mapper.MessageMapper;
import com.abo.programmerliveshealthy.service.MessageService;
import com.abo.programmerliveshealthy.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author abo
 * @date 2020/7/5 9:59
 * @remarks
 **/
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private UserService userService;

    @Override
    public List<MessageDTO> getCommonMessageList(Integer toId) {
        List<MessageDTO> commonMessageList = new ArrayList<MessageDTO>();
        QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<Message>();
        messageQueryWrapper.eq("to_user", toId);
        List<Message> messages = this.getBaseMapper().selectList(messageQueryWrapper);
        Collections.sort(messages, (Comparator) (o1, o2) -> {
            Message m1 = (Message) o1;
            Message m2 = (Message) o2;
            return m2.getGmtCreate().compareTo(m1.getGmtCreate());
        });
        for(Message message : messages){
            if(commonMessageList.size() == 3){
                break;
            }
            if(message.getType() == 0){
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setId(message.getId());
                messageDTO.setContent(message.getContent());
                messageDTO.setGmtCreate(message.getGmtCreate());
                messageDTO.setReadFlag(message.getReadFlag());
                User tempUser = userService.getBaseMapper().selectById(message.getFromUser());
                messageDTO.setFromUser(tempUser);
                messageDTO.setType(0);
                commonMessageList.add(messageDTO);
            }
        }
        return commonMessageList;
    }

    @Override
    public List<MessageDTO> getAdminMessageList(Integer toId) {
        List<MessageDTO> adminMessageList = new ArrayList<MessageDTO>();
        QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<Message>();
        messageQueryWrapper.eq("to_user", toId);
        List<Message> messages = this.getBaseMapper().selectList(messageQueryWrapper);
        Collections.sort(messages, (Comparator) (o1, o2) -> {
            Message m1 = (Message) o1;
            Message m2 = (Message) o2;
            return m2.getGmtCreate().compareTo(m1.getGmtCreate());
        });
        for(Message message : messages){
            if(adminMessageList.size() == 3){
                break;
            }
            if(message.getType() == 1){
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setType(1);
                messageDTO.setId(message.getId());
                messageDTO.setContent(message.getContent());
                messageDTO.setGmtCreate(message.getGmtCreate());
                messageDTO.setReadFlag(message.getReadFlag());
                User tempUser = userService.getBaseMapper().selectById(message.getFromUser());
                messageDTO.setFromUser(tempUser);
                adminMessageList.add(messageDTO);
            }
        }
        return adminMessageList;
    }
}
