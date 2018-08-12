package com.alphawang.rpc.course.service;

import com.alphawang.rpc.course.mapper.CourserMapper;
import com.alphawang.rpc.course.service.api.ICourseService;
import com.alphawang.rpc.course.service.api.dto.CourseDto;
import com.alphawang.rpc.course.thrift.ServiceProvider;
import com.alphawang.rpc.thrift.user.service.api.UserInfo;
import com.alphawang.rpc.thrift.user.service.api.dto.TeacherDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class CourseServiceImpl implements ICourseService {
    
    @Autowired
    private CourserMapper courserMapper;
    
    @Autowired
    private ServiceProvider serviceProvider;
    
    @Override 
    public List<CourseDto> courseList() {
        List<CourseDto> courseDtos = courserMapper.listCourse();
        if (courseDtos != null) {
            for (CourseDto courseDto : courseDtos) {
                Integer teacherId = courserMapper.getCourseTeacher(courseDto.getId());
                if (teacherId != null) {
                    try {
                        UserInfo teacher = serviceProvider.getUserService().getTeacherById(teacherId);
                        courseDto.setTeacher(trans2Teacher(teacher));
                    } catch (TException e) {
                        log.error("=====Failed to call userService.", e);
                    }
                }
            }
        }
        return courseDtos;
    }


    private TeacherDto trans2Teacher(UserInfo userInfo) {
        TeacherDto teacherDTO = new TeacherDto();
        BeanUtils.copyProperties(userInfo, teacherDTO);
        return teacherDTO;
    }
}
