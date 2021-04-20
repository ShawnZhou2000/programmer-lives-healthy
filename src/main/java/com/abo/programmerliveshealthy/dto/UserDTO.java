package com.abo.programmerliveshealthy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author abo
 * @date 2020/6/28 13:03
 * @remarks
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String username;
    private String bio;
    private String qq;
    private String email;
    private String password;
    private MultipartFile avatar;
    private String verificationCode;
}
