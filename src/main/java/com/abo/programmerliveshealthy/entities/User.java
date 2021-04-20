package com.abo.programmerliveshealthy.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author abo
 * @date 2020/6/28 12:56
 * @remarks
 **/
@TableName("user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @TableId
    private Integer id;
    private String username;
    private String bio;
    private String qq;
    private String email;
    private String password;
    private String avatar;
}
