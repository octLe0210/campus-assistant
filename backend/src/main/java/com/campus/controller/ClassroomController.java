package com.campus.controller;

import com.campus.common.Result;
import com.campus.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomController {

    @Autowired
    private ClassroomService classroomService;

    @GetMapping("/available")
    public Result<?> getAvailable(@RequestParam(required = false) String building,
                                  @RequestParam Integer weekDay,
                                  @RequestParam Integer startSlot,
                                  @RequestParam Integer endSlot) {
        return Result.ok(classroomService.getAvailable(building, weekDay, startSlot, endSlot));
    }
}
