package com.abo.programmerliveshealthy.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author abo
 * @date 2020/6/30 13:45
 * @remarks
 **/
@TableName("comment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @TableId
    private Integer id;
    private Integer commentator;
    @TableField("group_id")
    private Integer groupId;
    @TableField("like_count")
    private Integer likeCount;
    private String content;
    private String img;
    @TableField("gmt_create")
    private Long gmtCreate;
}
