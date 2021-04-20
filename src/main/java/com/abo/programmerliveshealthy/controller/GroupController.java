package com.abo.programmerliveshealthy.controller;

import com.abo.programmerliveshealthy.dto.CommentsDTO;
import com.abo.programmerliveshealthy.dto.GroupDTO;
import com.abo.programmerliveshealthy.dto.MessageDTO;
import com.abo.programmerliveshealthy.dto.UserStatisticsDTO;
import com.abo.programmerliveshealthy.entities.Comment;
import com.abo.programmerliveshealthy.entities.Group;
import com.abo.programmerliveshealthy.entities.Ug;
import com.abo.programmerliveshealthy.entities.User;
import com.abo.programmerliveshealthy.service.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author abo
 * @date 2020/6/28 17:26
 * @remarks
 **/
@Controller
public class GroupController {

    @Autowired
    private UserService userService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private UgService ugService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CommentService commentService;
    private static final int CODE_LENGTH = 8;

    @GetMapping("/group/add")
    public String addGroup(){
        return "multi-tenant-select";
    }

    @GetMapping("/group/create")
    public String toCreateGroup(){
        return "multi-tenant-create";
    }

    @GetMapping("/group/join")
    public String toJoinGroup(){
        return "multi-tenant-join";
    }

    @GetMapping("/group")
    private String group(@PathParam("id") Integer id,
                         HttpSession session,
                         Model model) throws ParseException {
        Integer clientId = (Integer) session.getAttribute("loginId");
        User user = userService.getBaseMapper().selectById(clientId);
        Group group = groupService.getBaseMapper().selectById(id);
        User groupLeader = userService.getBaseMapper().selectById(group.getCreator());
        QueryWrapper<Ug> ugQueryWrapper = new QueryWrapper<Ug>();
        ugQueryWrapper.eq("group_id", id);
        List<Ug> ugList = ugService.getBaseMapper().selectList(ugQueryWrapper);
        List<UserStatisticsDTO> statList = new ArrayList<UserStatisticsDTO>();
        List<MessageDTO> commonMessageList = messageService.getCommonMessageList(clientId);
        List<MessageDTO> adminMessageList = messageService.getAdminMessageList(clientId);
        for(Ug ug:ugList){
            UserStatisticsDTO tempDTO = new UserStatisticsDTO();
            tempDTO.setId(ug.getUserId());
            tempDTO.setTotalDay(ug.getTotalDay());
            tempDTO.setTotalMoney(ug.getTotalMoney());
            tempDTO.setSign(ug.getSign());
            tempDTO.setSignTime(ug.getSignTime());
            User tempUser = userService.getBaseMapper().selectById(ug.getUserId());
            tempDTO.setUsername(tempUser.getUsername());
            tempDTO.setAvatar(tempUser.getAvatar());
            statList.add(tempDTO);
        }
        Collections.sort(statList, (Comparator) (o1, o2) -> {
            UserStatisticsDTO s1 = (UserStatisticsDTO) o1;
            UserStatisticsDTO s2 = (UserStatisticsDTO) o2;
            return s2.getTotalDay().compareTo(s1.getTotalDay());
        });
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<Comment>();
        commentQueryWrapper.eq("group_id", group.getId());
        List<Comment> commentList = commentService.getBaseMapper().selectList(commentQueryWrapper);
        List<CommentsDTO> commentsList = new ArrayList<CommentsDTO>();
        for(Comment comment: commentList){
            Date nowDate = new Date(System.currentTimeMillis());
            Date tempDate = new Date(comment.getGmtCreate());
            if(getDayDiffer(nowDate, tempDate) == 0){
                CommentsDTO tempDTO = new CommentsDTO();
                tempDTO.setImg(comment.getImg());
                tempDTO.setContent(comment.getContent());
                tempDTO.setGmtCreate(comment.getGmtCreate());
                User tempUser = userService.getBaseMapper().selectById(comment.getCommentator());
                tempDTO.setUser(tempUser);
                commentsList.add(tempDTO);
            }
        }

        model.addAttribute("commonMessageList", commonMessageList);
        model.addAttribute("adminMessageList", adminMessageList);
        model.addAttribute("statList", statList);
        model.addAttribute("groupLeader", groupLeader);
        model.addAttribute("group", group);
        model.addAttribute("user", user);
        model.addAttribute("commentsList", commentsList);
        return "group";
    }

    @PostMapping("/group/create")
    public String createGroup(@RequestParam(value = "groupName") String groupName,
                              @RequestParam(value = "intro") String intro,
                              @RequestParam(value = "notice") String notice,
                              @RequestParam(value = "money") Integer money,
                              HttpSession session,
                              Model model){
        if(groupName == null || intro == null || notice == null || money == null ||
        "".equals(groupName) || "".equals(intro) || "".equals(notice)){
            model.addAttribute("msg", "群组信息不能为空!");
            return "multi-tenant-create";
        }
        Integer id = (Integer) session.getAttribute("loginId");
        User user = userService.getBaseMapper().selectById(id);
        Group group = new Group();
        Ug ug = new Ug();
        String code = UUID.randomUUID().toString().substring(0, 8);

        group.setCreator(id);
        group.setGroupName(groupName);
        group.setIntro(intro);
        group.setNotice(notice);
        group.setCode(code);
        group.setMoney(money);

        groupService.getBaseMapper().insert(group);
        QueryWrapper<Group> queryWrapper = new QueryWrapper<Group>();
        queryWrapper.eq("code", code);
        Group newGroup = groupService.getBaseMapper().selectOne(queryWrapper);
        ug.setGroupId(newGroup.getId());
        ug.setUserId(id);
        ug.setSign(0);
        ug.setTotalMoney(0);
        ug.setTotalDay(0);
        ugService.getBaseMapper().insert(ug);

        model.addAttribute("user", user);
        return "redirect:/";
    }

    @PostMapping("/group/join")
    public String joinGroup(@RequestParam(value = "code") String code,
                            HttpSession session,
                            Model model){
        if(code.length() != CODE_LENGTH){
            model.addAttribute("msg", "邀请码长度不正确,请检查!");
            return "multi-tenant-join";
        }
        Integer id = (Integer) session.getAttribute("loginId");
        User user = userService.getBaseMapper().selectById(id);
        Ug ug = new Ug();

        QueryWrapper<Group> groupQueryWrapper = new QueryWrapper<Group>();
        groupQueryWrapper.eq("code", code);
        Group group = groupService.getBaseMapper().selectOne(groupQueryWrapper);
        if(group == null){
            model.addAttribute("msg", "群组不存在!");
            return "multi-tenant-join";
        }
        QueryWrapper<Ug> ugQueryWrapper = new QueryWrapper<Ug>();
        ugQueryWrapper.eq("group_id", group.getId());
        ugQueryWrapper.eq("user_id", id);
        Ug tempUg = ugService.getBaseMapper().selectOne(ugQueryWrapper);

        if(tempUg == null){
            ug.setGroupId(group.getId());
            ug.setUserId(id);
            ug.setSign(0);
            ug.setTotalMoney(0);
            ug.setTotalDay(0);
            ugService.getBaseMapper().insert(ug);
        }else{
            model.addAttribute("msg", "您已加入该群组。");
        }

        model.addAttribute("user", user);
        return "redirect:/";
    }

    @GetMapping("/group/manage")
    public String manageGroup(HttpSession session,
                              Model model){
        Integer id = (Integer) session.getAttribute("loginId");
        User user = userService.getBaseMapper().selectById(id);
        List<GroupDTO> groupList = new ArrayList<GroupDTO>();

        QueryWrapper<Ug> ugQueryWrapper = new QueryWrapper<Ug>();
        ugQueryWrapper.eq("user_id", id);
        List<Ug> ugs = ugService.getBaseMapper().selectList(ugQueryWrapper);
        for(Ug ug: ugs){
            Integer tempId = ug.getGroupId();
            Group tempGroup = groupService.getBaseMapper().selectById(tempId);
            GroupDTO tempGroupDTO = new GroupDTO();
            User tempUser = userService.getBaseMapper().selectById(tempGroup.getCreator());
            tempGroupDTO.setCreator(tempUser);
            tempGroupDTO.setCode(tempGroup.getCode());
            tempGroupDTO.setGroupName(tempGroup.getGroupName());
            tempGroupDTO.setId(tempGroup.getId());
            tempGroupDTO.setIntro(tempGroup.getIntro());
            tempGroupDTO.setNotice(tempGroup.getNotice());
            tempGroupDTO.setMoney(tempGroup.getMoney());
            groupList.add(tempGroupDTO);
        }
        List<MessageDTO> commonMessageList = messageService.getCommonMessageList(id);
        List<MessageDTO> adminMessageList = messageService.getAdminMessageList(id);

        model.addAttribute("commonMessageList", commonMessageList);
        model.addAttribute("adminMessageList", adminMessageList);
        model.addAttribute("groups", groupList);
        model.addAttribute("user", user);
        return "managegroup";
    }

    @GetMapping("/toInvite")
    public String toInvite(@PathParam("groupId") Integer groupId,
                           Model model) {
        Group group = groupService.getBaseMapper().selectById(groupId);
        model.addAttribute("group", group);
        return "invite";
    }

    @GetMapping("/toUpdateGroup")
    public String toUpdateGroup(@PathParam("groupId") Integer groupId,
                                Model model){
        Group group = groupService.getBaseMapper().selectById(groupId);
        model.addAttribute("group", group);
        return "editgroup";
    }

    @PostMapping("/group/update")
    public String updateGroup(@RequestParam("groupId") Integer groupId,
                              @RequestParam("groupName") String groupName,
                              @RequestParam("groupMoney") Integer groupMoney,
                              @RequestParam("groupIntro") String groupIntro,
                              @RequestParam("groupNotice") String groupNotice,
                              HttpSession session,
                              Model model){
        if(groupName == null || "".equals(groupName) || groupMoney == null || "".equals(groupMoney) ||
        groupIntro == null || "".equals(groupIntro) || groupNotice == null || "".equals(groupNotice)){
            model.addAttribute("msg", "群组各项信息不能为空!");
            Group group = groupService.getBaseMapper().selectById(groupId);
            model.addAttribute("group", group);
            return "editgroup";
        }
        Group oldGroup = groupService.getBaseMapper().selectById(groupId);
        Group newGroup = new Group();
        newGroup.setCode(oldGroup.getCode());
        newGroup.setCreator(oldGroup.getCreator());
        newGroup.setId(oldGroup.getId());
        newGroup.setGroupName(groupName);
        newGroup.setMoney(groupMoney);
        newGroup.setIntro(groupIntro);
        newGroup.setNotice(groupNotice);
        groupService.getBaseMapper().updateById(newGroup);

        Integer id = (Integer) session.getAttribute("loginId");
        User loginUser = userService.getBaseMapper().selectById(id);
        List<GroupDTO> groupList = new ArrayList<GroupDTO>();

        QueryWrapper<Ug> ugQueryWrapper = new QueryWrapper<Ug>();
        ugQueryWrapper.eq("user_id", id);
        List<Ug> ugs = ugService.getBaseMapper().selectList(ugQueryWrapper);
        for(Ug tempUg: ugs){
            Integer tempId = tempUg.getGroupId();
            Group tempGroup = groupService.getBaseMapper().selectById(tempId);
            GroupDTO tempGroupDTO = new GroupDTO();
            User tempUser = userService.getBaseMapper().selectById(tempGroup.getCreator());
            tempGroupDTO.setCreator(tempUser);
            tempGroupDTO.setCode(tempGroup.getCode());
            tempGroupDTO.setGroupName(tempGroup.getGroupName());
            tempGroupDTO.setId(tempGroup.getId());
            tempGroupDTO.setIntro(tempGroup.getIntro());
            tempGroupDTO.setNotice(tempGroup.getNotice());
            tempGroupDTO.setMoney(tempGroup.getMoney());
            groupList.add(tempGroupDTO);
        }
        List<MessageDTO> commonMessageList = messageService.getCommonMessageList(id);
        List<MessageDTO> adminMessageList = messageService.getAdminMessageList(id);

        model.addAttribute("commonMessageList", commonMessageList);
        model.addAttribute("adminMessageList", adminMessageList);
        model.addAttribute("groups", groupList);
        model.addAttribute("user", loginUser);
        model.addAttribute("msg", "修改群组信息成功。");
        return "managegroup";
    }

    @PostMapping("/group/invite")
    public String invitePeople(@RequestParam("groupId") Integer groupId,
                               @RequestParam("email") String email,
                               HttpSession session,
                               Model model){
        Group group = groupService.getBaseMapper().selectById(groupId);
        if(email == null){
            model.addAttribute("msg", "邮箱不能为空!");
            model.addAttribute("group", group);
            return "invite";
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>();
        userQueryWrapper.eq("email", email);
        User user = userService.getBaseMapper().selectOne(userQueryWrapper);
        if(user == null){
            model.addAttribute("msg", "该用户不存在,请检查邮箱正误!");
            model.addAttribute("group", group);
            return "invite";
        }
        QueryWrapper<Ug> ugQueryWrapper = new QueryWrapper<Ug>();
        ugQueryWrapper.eq("group_id", groupId);
        ugQueryWrapper.eq("user_id", user.getId());
        Ug isExistUg = ugService.getBaseMapper().selectOne(ugQueryWrapper);
        if(isExistUg != null){
            model.addAttribute("msg", "该用户已加入该群组。");
            model.addAttribute("group", group);
            return "invite";
        }
        Ug ug = new Ug();
        ug.setUserId(user.getId());
        ug.setGroupId(groupId);
        ug.setTotalDay(0);
        ug.setSign(0);
        ug.setSignTime((long) 0);
        ug.setTotalMoney(0);
        ugService.getBaseMapper().insert(ug);

        Integer id = (Integer) session.getAttribute("loginId");
        User loginUser = userService.getBaseMapper().selectById(id);
        List<GroupDTO> groupList = new ArrayList<GroupDTO>();

        ugQueryWrapper = new QueryWrapper<Ug>();
        ugQueryWrapper.eq("user_id", id);
        List<Ug> ugs = ugService.getBaseMapper().selectList(ugQueryWrapper);
        for(Ug tempUg: ugs){
            Integer tempId = tempUg.getGroupId();
            Group tempGroup = groupService.getBaseMapper().selectById(tempId);
            GroupDTO tempGroupDTO = new GroupDTO();
            User tempUser = userService.getBaseMapper().selectById(tempGroup.getCreator());
            tempGroupDTO.setCreator(tempUser);
            tempGroupDTO.setCode(tempGroup.getCode());
            tempGroupDTO.setGroupName(tempGroup.getGroupName());
            tempGroupDTO.setId(tempGroup.getId());
            tempGroupDTO.setIntro(tempGroup.getIntro());
            tempGroupDTO.setNotice(tempGroup.getNotice());
            tempGroupDTO.setMoney(tempGroup.getMoney());
            groupList.add(tempGroupDTO);
        }
        List<MessageDTO> commonMessageList = messageService.getCommonMessageList(id);
        List<MessageDTO> adminMessageList = messageService.getAdminMessageList(id);

        model.addAttribute("commonMessageList", commonMessageList);
        model.addAttribute("adminMessageList", adminMessageList);
        model.addAttribute("groups", groupList);
        model.addAttribute("user", loginUser);
        model.addAttribute("msg", "成功邀请新成员: " + user.getUsername() + "加入群组: " + group.getGroupName() + "!");
        return "managegroup";
    }

    @GetMapping("/loadComment")
    public String loadComment(@PathParam("id") Integer id,
                              @PathParam("dayNumber") Integer dayNumber,
                              HttpSession session,
                              Model model) throws ParseException {
        Integer clientId = (Integer) session.getAttribute("loginId");
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<Comment>();
        commentQueryWrapper.eq("group_id", id);
        commentQueryWrapper.eq("commentator", clientId);
        List<Comment> commentList = commentService.getBaseMapper().selectList(commentQueryWrapper);
        List<CommentsDTO> commentsList = new ArrayList<CommentsDTO>();
        if(dayNumber != -1){
            for(Comment comment: commentList){
                Date nowDate = new Date(System.currentTimeMillis());
                Date tempDate = new Date(comment.getGmtCreate());
                if(Math.abs(getDayDiffer(nowDate, tempDate)) >= 1 && Math.abs(getDayDiffer(nowDate, tempDate)) <= dayNumber){
                    CommentsDTO tempDTO = new CommentsDTO();
                    tempDTO.setImg(comment.getImg());
                    tempDTO.setContent(comment.getContent());
                    tempDTO.setGmtCreate(comment.getGmtCreate());
                    User tempUser = userService.getBaseMapper().selectById(comment.getCommentator());
                    tempDTO.setUser(tempUser);
                    commentsList.add(tempDTO);
                }
            }
        }else{
            for(Comment comment: commentList){
                CommentsDTO tempDTO = new CommentsDTO();
                tempDTO.setImg(comment.getImg());
                tempDTO.setContent(comment.getContent());
                tempDTO.setGmtCreate(comment.getGmtCreate());
                User tempUser = userService.getBaseMapper().selectById(comment.getCommentator());
                tempDTO.setUser(tempUser);
                commentsList.add(tempDTO);
            }
        }
        model.addAttribute("commentsList", commentsList);
        return "group::comment";
    }

    public int getDayDiffer(Date startDate, Date endDate) throws ParseException {
        //判断是否跨年
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        String startYear = yearFormat.format(startDate);
        String endYear = yearFormat.format(endDate);
        if (startYear.equals(endYear)) {
            /*  使用Calendar跨年的情况会出现问题    */
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            int startDay = calendar.get(Calendar.DAY_OF_YEAR);
            calendar.setTime(endDate);
            int endDay = calendar.get(Calendar.DAY_OF_YEAR);
            return endDay - startDay;
        } else {
            /*  跨年不会出现问题，需要注意不满24小时情况（2016-03-18 11:59:59 和 2016-03-19 00:00:01的话差值为 0）  */
            //  只格式化日期，消除不满24小时影响
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            long startDateTime = dateFormat.parse(dateFormat.format(startDate)).getTime();
            long endDateTime = dateFormat.parse(dateFormat.format(endDate)).getTime();
            return (int) ((endDateTime - startDateTime) / (1000 * 3600 * 24));
        }
    }

}
