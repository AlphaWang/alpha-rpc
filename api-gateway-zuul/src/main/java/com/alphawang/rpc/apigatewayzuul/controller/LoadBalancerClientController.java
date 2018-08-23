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
public class LoadBalancerClientController {
    
    private static String PATH = "/course/list";
    
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    
    @RequestMapping("loadbalancer-client")
    public String loadBalance() {
        RestTemplate restTemplate = new RestTemplate(); 

        ServiceInstance serviceInstance = loadBalancerClient.choose("course-edge-service");
        String url = String.format("http://%s:%s/%s", serviceInstance.getHost(), serviceInstance.getPort(), PATH);
        String response = restTemplate.getForObject(url, String.class);
        
        log.info("LoadBalancerClient response: {}", response);
        return response;
    }
}
