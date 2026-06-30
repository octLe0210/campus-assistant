package com.campus.controller;

import com.campus.common.Result;
import com.campus.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public Result<?> search(@RequestParam(required = false) String keyword,
                            @RequestParam(required = false) String teacher,
                            @RequestParam(required = false) Integer weekDay) {
        return Result.ok(courseService.search(keyword, teacher, weekDay));
    }
}
