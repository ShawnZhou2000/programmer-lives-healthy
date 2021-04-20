package com.abo.programmerliveshealthy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author abo
 * @date 2020/6/30 16:25
 * @remarks
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultDTO<T> {
    private Integer code;
    private String message;
    private T data;

}
