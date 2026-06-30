package com.campus.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.entity.News;
import com.campus.mapper.NewsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class NewsService {

    @Autowired
    private NewsMapper newsMapper;

    public IPage<News> page(int current, int size, String keyword, String category) {
        Page<News> page = new Page<>(current, size);
        LambdaQueryWrapper<News> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(News::getTitle, keyword);
        }
        if (StringUtils.hasText(category)) {
            wrapper.eq(News::getCategory, category);
        }
        wrapper.orderByDesc(News::getPublishTime);
        return newsMapper.selectPage(page, wrapper);
    }

    public News getById(Long id) {
        News news = newsMapper.selectById(id);
        if (news != null) {
            news.setViewCount(news.getViewCount() + 1);
            newsMapper.updateById(news);
        }
        return news;
    }
}
