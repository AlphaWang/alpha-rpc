package com.alphawang.rpc.course.service.api;

import com.alphawang.rpc.course.service.api.dto.CourseDto;

import java.util.List;

public interface ICourseService {
    
    List<CourseDto> courseList();
    
}
