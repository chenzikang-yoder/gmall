package com.atguigu.gmall.mange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.atguigu.gmall.mange.mapper")
public class GmallMangeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallMangeServiceApplication.class, args);
    }

}
