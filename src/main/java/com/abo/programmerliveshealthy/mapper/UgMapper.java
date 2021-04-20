package com.abo.programmerliveshealthy.mapper;

import com.abo.programmerliveshealthy.entities.Ug;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * @author abo
 * @date 2020/6/30 13:54
 * @remarks
 **/
public interface UgMapper extends BaseMapper<Ug> {
    @Update("UPDATE ug SET sign=0,sign_time=0")
    void resetSign();
}
