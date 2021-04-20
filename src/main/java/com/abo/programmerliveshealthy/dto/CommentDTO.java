package com.abo.programmerliveshealthy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author abo
 * @date 2020/6/30 16:13
 * @remarks
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {

    private Integer userId;
    private Integer groupId;
    private String content;
    private MultipartFile img;
}
