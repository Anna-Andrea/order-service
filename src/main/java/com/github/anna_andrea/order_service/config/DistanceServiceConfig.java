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

    	// TODO Proxy code, need to delete when deploying
//        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
//        factory.setProxy(proxy);
//
//        return new RestTemplate(factory);
    	  return new RestTemplate();	
    }
}
