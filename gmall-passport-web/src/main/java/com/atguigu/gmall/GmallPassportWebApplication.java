package com.atguigu.gmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication // 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@MapperScan(basePackages = "com.dudu.dao", markerInterface = Mapper.class)
public class GmallPassportWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallPassportWebApplication.class, args);
    }

}
