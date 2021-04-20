package com.abo.programmerliveshealthy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author abo
 * @date 2020/6/30 15:37
 * @remarks
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatisticsDTO {

    private Integer id;
    private String username;
    private String avatar;
    private Integer totalDay;
    private Integer totalMoney;
    private Integer sign;
    private Long signTime;
}
