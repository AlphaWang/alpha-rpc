package com.alphawang.rpc.thrift.user.service.api.dto;

import lombok.Data;

@Data
public class TeacherDto extends UserDto {
    
    private String intro;
    private int starts;
}
