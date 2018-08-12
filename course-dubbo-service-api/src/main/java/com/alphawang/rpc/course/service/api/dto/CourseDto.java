package com.alphawang.rpc.course.service.api.dto;

import com.alphawang.rpc.thrift.user.service.api.dto.TeacherDto;
import lombok.Data;

import java.io.Serializable;

@Data
public class CourseDto implements Serializable {
    private int id;
    private String title;
    private String desc;
    private TeacherDto teacher;
}
