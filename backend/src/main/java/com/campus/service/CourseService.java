package com.campus.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.entity.Course;
import com.campus.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseMapper courseMapper;

    public List<Course> search(String keyword, String teacher, Integer weekDay) {
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Course::getName, keyword);
        }
        if (StringUtils.hasText(teacher)) {
            wrapper.like(Course::getTeacher, teacher);
        }
        if (weekDay != null) {
            wrapper.eq(Course::getWeekDay, weekDay);
        }
        wrapper.orderByAsc(Course::getWeekDay, Course::getStartSlot);
        return courseMapper.selectList(wrapper);
    }
}
