package com.github.anna_andrea.order_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Kevin
 *
 */
@Configuration
public class DistanceServiceConfig {

    @Bean
    public RestTemplate restTemplate() {

    	return new RestTemplate();	
    
    }
}
