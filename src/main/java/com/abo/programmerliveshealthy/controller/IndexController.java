package com.abo.programmerliveshealthy.controller;

import com.abo.programmerliveshealthy.dto.MessageDTO;
import com.abo.programmerliveshealthy.dto.UserStatisticsDTO;
import com.abo.programmerliveshealthy.entities.Group;
import com.abo.programmerliveshealthy.entities.Message;
import com.abo.programmerliveshealthy.entities.Ug;
import com.abo.programmerliveshealthy.entities.User;
import com.abo.programmerliveshealthy.service.GroupService;
import com.abo.programmerliveshealthy.service.MessageService;
import com.abo.programmerliveshealthy.service.UgService;
import com.abo.programmerliveshealthy.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author abo
 * @date 2020/6/28 14:07
 * @remarks
 **/
@Controller
public class IndexController {

    @Autowired
    private UserService userService;
    @Autowired
    private UgService ugService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private MessageService messageService;

    @GetMapping("/")
    public String index(HttpSession session,
                        Model model){
        Integer id = (Integer) session.getAttribute("loginId");
        User user = userService.getBaseMapper().selectById(id);
        List<Group> groupList = new ArrayList<Group>();

        QueryWrapper<Ug> ugQueryWrapper = new QueryWrapper<Ug>();
        ugQueryWrapper.eq("user_id", id);
        List<Ug> ugs = ugService.getBaseMapper().selectList(ugQueryWrapper);
        for(Ug ug: ugs){
            Integer tempId = ug.getGroupId();
            Group tempGroup = groupService.getBaseMapper().selectById(tempId);
            groupList.add(tempGroup);
        }
        List<MessageDTO> commonMessageList = messageService.getCommonMessageList(id);
        List<MessageDTO> adminMessageList = messageService.getAdminMessageList(id);

        model.addAttribute("commonMessageList", commonMessageList);
        model.addAttribute("adminMessageList", adminMessageList);
        model.addAttribute("groups", groupList);
        model.addAttribute("user", user);
        return "index";
    }
}
