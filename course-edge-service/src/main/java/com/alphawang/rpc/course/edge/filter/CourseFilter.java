package com.alphawang.rpc.course.edge.filter;

import com.alphawang.rpc.thrift.user.service.api.dto.UserDto;
import com.alphawang.user.client.LoginFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class CourseFilter extends LoginFilter {
    @Override 
    protected void login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, UserDto userDto) {
        log.info("FILTER: user logged in.");

        httpServletRequest.setAttribute("user", userDto);
    }
}
