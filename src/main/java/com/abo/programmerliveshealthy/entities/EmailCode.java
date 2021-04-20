package com.abo.programmerliveshealthy.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author abo
 * @date 2020/7/4 2:42
 * @remarks
 **/
@TableName("mailcode")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailCode {

    @TableId
    private Integer id;
    private String code;
    @TableField("gmt_create")
    private Long gmtCreate;
    @TableField("is_used")
    private Integer isUsed;
}
