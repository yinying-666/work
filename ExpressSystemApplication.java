package com.express.expresssystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
// 直接使用MyBatis自带注解，无需自建类
@MapperScan("com.express.expresssystem.mapper")
public class ExpressSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExpressSystemApplication.class, args);
    }
//调用coze开放API
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}