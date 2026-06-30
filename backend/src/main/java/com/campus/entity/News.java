package com.campus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("news")
public class News {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String content;
    private String coverImage;
    private String source;
    private String category;
    private LocalDateTime publishTime;
    private Integer viewCount;
}
