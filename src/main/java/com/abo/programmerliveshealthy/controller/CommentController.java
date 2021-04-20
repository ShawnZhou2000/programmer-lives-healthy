package com.abo.programmerliveshealthy.controller;

import com.abo.programmerliveshealthy.dto.ResultDTO;
import com.abo.programmerliveshealthy.entities.Comment;
import com.abo.programmerliveshealthy.entities.Ug;
import com.abo.programmerliveshealthy.service.CommentService;
import com.abo.programmerliveshealthy.service.UgService;
import com.abo.programmerliveshealthy.util.MyFileUpload;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import sun.java2d.pipe.SpanShapeRenderer;

import java.io.IOException;

/**
 * @author abo
 * @date 2020/6/30 16:10
 * @remarks
 **/
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private UgService ugService;
    @Autowired
    private MyFileUpload myFileUpload;
    private static final String SUCCESS_FLAG = "打卡";

    @ResponseBody
    @PostMapping(value = "/comment")
    public Object publishComment(@RequestParam(value = "groupId") Integer groupId,
                                 @RequestParam(value = "userId") Integer userId,
                                 @RequestParam(value = "content") String content,
                                 @RequestParam(value = "img") MultipartFile img
                                 ) throws IOException {
        ResultDTO resultDTO = new ResultDTO();
        if (!img.isEmpty()) {
            QueryWrapper<Ug> ugQueryWrapper = new QueryWrapper<Ug>();
            ugQueryWrapper.eq("user_id", userId);
            ugQueryWrapper.eq("group_id", groupId);
            Ug ug = ugService.getBaseMapper().selectOne(ugQueryWrapper);
            if(content.contains(SUCCESS_FLAG) && ug.getSign() == 1){
                resultDTO.setCode(1005);
                resultDTO.setMessage("你今天已经打过卡啦!");
                return resultDTO;
            }
            String imgName = myFileUpload.uploadImg(img);
            Comment comment = new Comment();
            comment.setCommentator(userId);
            comment.setGroupId(groupId);
            comment.setLikeCount(0);
            comment.setContent(content);
            comment.setImg(imgName);
            comment.setGmtCreate(System.currentTimeMillis());

            if (content.contains(SUCCESS_FLAG)) {
                ug.setSign(1);
                ug.setTotalDay(ug.getTotalDay() + 1);
                ug.setSignTime(System.currentTimeMillis());
                ugService.getBaseMapper().updateById(ug);
            }
            commentService.getBaseMapper().insert(comment);
            resultDTO.setCode(200);
            resultDTO.setMessage("发布成功!");
        } else {
            resultDTO.setCode(1004);
            resultDTO.setMessage("图片不能为空!");
        }
        return resultDTO;
    }
}
