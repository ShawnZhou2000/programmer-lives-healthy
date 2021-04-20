package com.abo.programmerliveshealthy.service;

import com.abo.programmerliveshealthy.dto.MessageDTO;
import com.abo.programmerliveshealthy.entities.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author abo
 * @date 2020/7/5 9:58
 * @remarks
 **/
public interface MessageService extends IService<Message> {

    List<MessageDTO> getCommonMessageList(Integer toId);
    List<MessageDTO> getAdminMessageList(Integer toId);
}
