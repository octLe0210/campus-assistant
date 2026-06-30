package com.campus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

@Data
@TableName("course")
public class Course {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String teacher;
    private Integer weekDay;
    private Integer startSlot;
    private Integer endSlot;
    private String classroom;
    private BigDecimal credits;
    private Integer capacity;
    private String semester;
    private String description;
}
