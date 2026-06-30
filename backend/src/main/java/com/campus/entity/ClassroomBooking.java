package com.campus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("classroom_booking")
public class ClassroomBooking {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long classroomId;
    private Integer weekDay;
    private Integer startSlot;
    private Integer endSlot;
    private String courseName;
    private String semester;
}
