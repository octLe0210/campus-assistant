package com.campus.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.entity.Classroom;
import com.campus.entity.ClassroomBooking;
import com.campus.mapper.ClassroomBookingMapper;
import com.campus.mapper.ClassroomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClassroomService {

    @Autowired
    private ClassroomMapper classroomMapper;

    @Autowired
    private ClassroomBookingMapper bookingMapper;

    public List<Classroom> getAvailable(String building, Integer weekDay, Integer startSlot, Integer endSlot) {
        LambdaQueryWrapper<ClassroomBooking> bookingWrapper = new LambdaQueryWrapper<>();
        bookingWrapper.eq(ClassroomBooking::getWeekDay, weekDay);
        bookingWrapper.le(ClassroomBooking::getStartSlot, startSlot);
        bookingWrapper.ge(ClassroomBooking::getEndSlot, endSlot);
        List<Long> bookedIds = bookingMapper.selectList(bookingWrapper)
                .stream()
                .map(ClassroomBooking::getClassroomId)
                .collect(Collectors.toList());

        LambdaQueryWrapper<Classroom> wrapper = new LambdaQueryWrapper<>();
        if (building != null && !building.isEmpty()) {
            wrapper.eq(Classroom::getBuilding, building);
        }
        wrapper.eq(Classroom::getStatus, "available");
        if (!bookedIds.isEmpty()) {
            wrapper.notIn(Classroom::getId, bookedIds);
        }
        return classroomMapper.selectList(wrapper);
    }
}
