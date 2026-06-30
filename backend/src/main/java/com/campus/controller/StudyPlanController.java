package com.campus.controller;

import com.campus.common.Result;
import com.campus.entity.StudyPlan;
import com.campus.service.StudyPlanService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/study-plans")
public class StudyPlanController {

    @Autowired
    private StudyPlanService studyPlanService;

    @PostMapping("/generate")
    public Result<?> generate(@RequestBody java.util.Map<String, String> body, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.ok(studyPlanService.generate(userId, body.get("goal")));
    }

    @GetMapping
    public Result<?> list(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.ok(studyPlanService.listByUser(userId));
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody StudyPlan plan) {
        return Result.ok(studyPlanService.update(id, plan));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        studyPlanService.delete(id);
        return Result.ok();
    }
}
