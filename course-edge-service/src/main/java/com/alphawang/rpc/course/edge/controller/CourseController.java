package com.alphawang.rpc.course.edge.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alphawang.rpc.course.service.api.ICourseService;
import com.alphawang.rpc.course.service.api.dto.CourseDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class CourseController {
    
    @Reference
    private ICourseService courseService;
    
    @GetMapping(value="/course/list")
    public List<CourseDto> courseList() {
         return courseService.courseList();
    }
}
