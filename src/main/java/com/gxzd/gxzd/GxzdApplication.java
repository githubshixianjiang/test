package com.gxzd.gxzd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author Administrator
 */
@SpringBootApplication
@MapperScan("com.gxzd.gxzd.mapper")
public class GxzdApplication {

    public static void main(String[] args) {
        SpringApplication.run(GxzdApplication.class, args);
    }

    /**
     * http请求封装实体
     *
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
