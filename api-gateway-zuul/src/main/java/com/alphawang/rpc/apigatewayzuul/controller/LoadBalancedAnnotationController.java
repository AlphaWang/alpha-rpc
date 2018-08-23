package com.alphawang.rpc.apigatewayzuul.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
public class LoadBalancedAnnotationController {
    
    private static String PATH = "/course/list";
    
    @Autowired
    private RestTemplate restTemplate;
    
    @RequestMapping("loadbalanced-annotation")
    public String loadBalance() {

        String response = restTemplate.getForObject("http://course-edge-service/" + PATH, String.class);

        log.info("LoadBalancedAnnotation response: {}", response);
        return response;
    }
}
