package com.campus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("classroom")
public class Classroom {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String building;
    private Integer floor;
    private Integer capacity;
    private Boolean hasProjector;
    private String status;
}
