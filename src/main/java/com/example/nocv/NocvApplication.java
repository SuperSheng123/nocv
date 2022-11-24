package com.example.nocv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling//定时启动
public class NocvApplication {

    public static void main(String[] args) {
        SpringApplication.run(NocvApplication.class, args);
    }

}
