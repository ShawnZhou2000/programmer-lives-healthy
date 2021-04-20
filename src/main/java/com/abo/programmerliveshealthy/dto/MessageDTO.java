package com.abo.programmerliveshealthy.dto;

import com.abo.programmerliveshealthy.entities.User;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author abo
 * @date 2020/7/5 18:08
 * @remarks
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private Integer id;
    private User fromUser;
    private Integer toUser;
    private String content;
    private String img;
    private Integer type;
    private Long gmtCreate;
    private Integer readFlag;
}
