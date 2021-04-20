package com.abo.programmerliveshealthy.dto;

import com.abo.programmerliveshealthy.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author abo
 * @date 2020/6/30 19:15
 * @remarks
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDTO {

    private String img;
    private String content;
    private Long gmtCreate;
    private User user;
}
