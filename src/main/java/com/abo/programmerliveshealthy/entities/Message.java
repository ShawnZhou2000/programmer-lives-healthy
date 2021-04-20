package com.abo.programmerliveshealthy.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author abo
 * @date 2020/7/5 9:53
 * @remarks
 **/
@TableName("message")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @TableId
    private Integer id;
    @TableField("from_user")
    private Integer fromUser;
    @TableField("to_user")
    private Integer toUser;
    private String content;
    private String img;
    private Integer type;
    @TableField("gmt_create")
    private Long gmtCreate;
    @TableField("read_flag")
    private Integer readFlag;
}
