package com.abo.programmerliveshealthy.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author abo
 * @date 2020/6/30 13:49
 * @remarks
 **/
@TableName("ug")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ug {

    @TableId
    private Integer id;
    @TableField("group_id")
    private Integer groupId;
    @TableField("user_id")
    private Integer userId;
    private Integer sign;
    @TableField("total_money")
    private Integer totalMoney;
    @TableField("total_day")
    private Integer totalDay;
    @TableField("sign_time")
    private Long signTime;
}
