package com.alphawang.rpc.apigatewayzuul.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateConfig {
    
    @Bean
    @LoadBalanced  // IMPORTANT !!!
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
