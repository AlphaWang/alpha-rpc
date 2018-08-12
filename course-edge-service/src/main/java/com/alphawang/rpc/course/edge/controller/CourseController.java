package com.alphawang.rpc.course.edge.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alphawang.rpc.course.service.api.ICourseService;
import com.alphawang.rpc.course.service.api.dto.CourseDto;
import com.alphawang.rpc.thrift.user.service.api.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
public class CourseController {
    
    @Reference
    private ICourseService courseService;
    
    @GetMapping(value="/course/list")
    @ResponseBody
    public List<CourseDto> courseList(HttpServletRequest request) {
        
        UserDto user = (UserDto) request.getAttribute("user");
        log.info("---Controller: user is {}", user);
        
        return courseService.courseList();
    }
}
