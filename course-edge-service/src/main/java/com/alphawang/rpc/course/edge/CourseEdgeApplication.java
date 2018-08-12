package com.alphawang.rpc.course.edge;

import com.alphawang.rpc.course.edge.filter.CourseFilter;
import com.google.common.collect.Lists;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CourseEdgeApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourseEdgeApplication.class, args);
    }
    
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        CourseFilter courseFilter = new CourseFilter();
        
        filterRegistrationBean.setFilter(courseFilter);
        filterRegistrationBean.setUrlPatterns(Lists.newArrayList("/*"));
        
        return filterRegistrationBean;
    }
}
