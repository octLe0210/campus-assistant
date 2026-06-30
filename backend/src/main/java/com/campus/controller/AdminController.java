package com.campus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.campus.common.Result;
import com.campus.entity.News;
import com.campus.entity.Course;
import com.campus.entity.User;
import com.campus.mapper.NewsMapper;
import com.campus.mapper.CourseMapper;
import com.campus.mapper.UserMapper;
import com.campus.mapper.ClassroomMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ClassroomMapper classroomMapper;

    // ===== 校园新闻管理 =====

    @PostMapping("/news")
    public Result<?> createNews(@RequestBody News news) {
        news.setId(null);
        newsMapper.insert(news);
        return Result.ok(news);
    }

    @PutMapping("/news/{id}")
    public Result<?> updateNews(@PathVariable Long id, @RequestBody News news) {
        news.setId(id);
        newsMapper.updateById(news);
        return Result.ok(news);
    }

    @DeleteMapping("/news/{id}")
    public Result<?> deleteNews(@PathVariable Long id) {
        newsMapper.deleteById(id);
        return Result.ok();
    }

    // ===== 课程管理 =====

    @PostMapping("/courses")
    public Result<?> createCourse(@RequestBody Course course) {
        course.setId(null);
        courseMapper.insert(course);
        return Result.ok(course);
    }

    @PutMapping("/courses/{id}")
    public Result<?> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        course.setId(id);
        courseMapper.updateById(course);
        return Result.ok(course);
    }

    @DeleteMapping("/courses/{id}")
    public Result<?> deleteCourse(@PathVariable Long id) {
        courseMapper.deleteById(id);
        return Result.ok();
    }

    // ===== 用户管理 =====

    @GetMapping("/users")
    public Result<?> userList() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(User::getCreatedAt);
        List<User> users = userMapper.selectList(wrapper);
        users.forEach(u -> u.setPassword(null));
        return Result.ok(users);
    }

    // ===== 数据统计 =====

    @GetMapping("/statistics")
    public Result<?> statistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("userCount", userMapper.selectCount(null));
        stats.put("newsCount", newsMapper.selectCount(null));
        stats.put("courseCount", courseMapper.selectCount(null));
        stats.put("classroomCount", classroomMapper.selectCount(null));
        return Result.ok(stats);
    }
}
