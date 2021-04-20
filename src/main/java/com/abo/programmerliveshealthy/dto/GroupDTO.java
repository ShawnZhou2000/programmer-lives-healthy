package com.abo.programmerliveshealthy.dto;

import com.abo.programmerliveshealthy.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author abo
 * @date 2020/7/23 20:05
 * @remarks
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDTO {

    private Integer id;
    private String groupName;
    private User creator;
    private String intro;
    private String code;
    private String notice;
    private Integer money;
}
