package com.campus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.campus.common.Result;
import com.campus.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @GetMapping
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) String keyword,
                          @RequestParam(required = false) String category) {
        IPage<?> p = newsService.page(page, size, keyword, category);
        Map<String, Object> result = new HashMap<>();
        result.put("records", p.getRecords());
        result.put("total", p.getTotal());
        result.put("pages", p.getPages());
        result.put("current", p.getCurrent());
        result.put("size", p.getSize());
        return Result.ok(result);
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.ok(newsService.getById(id));
    }
}
