package com.campus.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.entity.StudyPlan;
import com.campus.mapper.StudyPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyPlanService {

    @Autowired
    private StudyPlanMapper studyPlanMapper;

    @Autowired
    private AiService aiService;

    public StudyPlan generate(Long userId, String goal) {
        String aiResult = aiService.generateStudyPlan(userId, goal);

        StudyPlan plan = new StudyPlan();
        plan.setUserId(userId);
        plan.setTitle("学习计划 - " + goal);
        plan.setContent(aiResult);
        plan.setStatus("active");
        studyPlanMapper.insert(plan);
        return plan;
    }

    public List<StudyPlan> listByUser(Long userId) {
        LambdaQueryWrapper<StudyPlan> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StudyPlan::getUserId, userId)
               .orderByDesc(StudyPlan::getCreatedAt);
        return studyPlanMapper.selectList(wrapper);
    }

    public StudyPlan update(Long id, StudyPlan plan) {
        plan.setId(id);
        studyPlanMapper.updateById(plan);
        return studyPlanMapper.selectById(id);
    }

    public void delete(Long id) {
        studyPlanMapper.deleteById(id);
    }
}
