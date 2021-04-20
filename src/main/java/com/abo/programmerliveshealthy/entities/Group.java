package com.abo.programmerliveshealthy.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author abo
 * @date 2020/6/30 13:43
 * @remarks
 **/
@TableName("`group`")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {

    @TableId
    private Integer id;
    @TableField("groupname")
    private String groupName;
    private Integer creator;
    private String intro;
    private String code;
    private String notice;
    private Integer money;
}
